package whyv401.smart_contract;

import org.web3j.abi.FunctionReturnDecoder;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.DynamicArray;
import org.web3j.abi.datatypes.Event;
import org.web3j.abi.datatypes.Type;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.WalletUtils;
import org.web3j.ens.EnsResolutionException;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.RemoteCall;
import org.web3j.protocol.core.methods.request.EthFilter;
import org.web3j.protocol.core.methods.response.EthCall;
import org.web3j.protocol.core.methods.response.Log;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.protocol.core.methods.response.Web3ClientVersion;
import org.web3j.protocol.http.HttpService;
import org.web3j.tx.gas.DefaultGasProvider;
import org.web3j.tx.gas.StaticGasProvider;
import org.web3j.utils.Convert;
import whyv401.controller.Utils;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.function.Consumer;
import java.util.logging.Level;

public class HousingContract extends AbstractContract<Housing>{
    protected static final String HOME_REGIS = "Home registered";
    protected static final String FAIL_HOME_REGIS = "Failed to register home";

    protected HousingContract(String contractAddr) {
        super(contractAddr);
    }

    public static synchronized HousingContract getInstance(String contractAddr) {
        if(contract == null)
            contract = new HousingContract(contractAddr);
        return (HousingContract) contract;
    }

    public boolean addCredentials(String privateKey, String contractAddress){
        if(!WalletUtils.isValidPrivateKey(privateKey)) {
            logger.log(Level.WARNING, INVALID_KEY);
            return false;
        }
        try {
            Credentials credentials = Credentials.create(privateKey);
            Housing housing = Housing.load(contractAddress, web3j, credentials, new DefaultGasProvider());
            //TODO: hex public key??
            contractMap.put(credentials.getAddress(), housing);
            logger.log(Level.INFO, CON_LOADED);
            return true;
        }catch (EnsResolutionException e){
            logger.log(Level.SEVERE, CON_LOAD_ERR);
        }
        return false;
    }

    public boolean stealHouse(String sender, BigInteger houseId){
        if(!contains(sender)) return false;
        try {
            contractMap.get(sender).stealHouse(houseId).send();
            return true;
        } catch (Exception e) {
            logger.log(Level.WARNING, e.toString(), e);
        }
        return false;
    }

    //TODO: Payment
    public boolean stealHouseKindly(String sender, BigInteger houseId){
        if(!contains(sender)) return false;
        // TODO: Add method to contract
//        contractMap.get(sender).stealHouseKindly(houseId).send();
        return false;
    }

    public boolean approveValuator(String sender, String valuator){
        if(!contains(sender)) return false;
        try {
            contractMap.get(sender).approveValuator(valuator).send();
            return true;
        } catch (Exception e) {
            logger.log(Level.WARNING, e.toString(), e);
        }
        return false;
    }

    public boolean applyForValuator(String sender){
        if(!contains(sender)) return false;
        try {
            contractMap.get(sender).applyForValuator().send();
            return true;
        } catch (Exception e) {
            logger.log(Level.WARNING, e.toString(), e);
        }
        return false;
    }

    public boolean assignValue(String sender, BigInteger houseId, BigInteger value){
        if(!contains(sender)) return false;
        try {
            contractMap.get(sender).assignValue(houseId, value).send();
            return true;
        } catch (Exception e) {
            logger.log(Level.WARNING, e.toString(), e);
        }
        return false;
    }

    public boolean registerHome(String sender){
        if(!contains(sender)) return false;
        try {
            contractMap.get(sender).registerHome().send();
            logger.log(Level.INFO, HOME_REGIS);
            return true;
        } catch (Exception e) {
            logger.log(Level.WARNING, FAIL_HOME_REGIS);
            logger.log(Level.WARNING, e.toString(), e);
        }
        return false;
    }

    //TODO: finish
    public List getHousesForSale(String sender){
        if(!contains(sender)) return null;
        try {
            List list = contractMap.get(sender).getAllHousesForSale().send();
            return list;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean sellHouse(String sender, BigInteger houseId){
        if(!contains(sender)) return false;
        try {
            contractMap.get(sender).sellHouse(houseId).send();
            return true;
        } catch (Exception e) {
            logger.log(Level.WARNING, e.toString(), e);
        }
        return false;
    }

    //TODO: Payment
    public boolean buyHouse(String sender, BigInteger houseId, String weiValue){
        if(!contains(sender)) return false;
        BigInteger w = validateWei(weiValue);
        if(w == null) return false;
        try {
            contractMap.get(sender).buyHouse(houseId, w).send();
            return true;
        } catch (Exception e) {
            logger.log(Level.WARNING, e.toString(), e);
        }
        return false;
    }
}
