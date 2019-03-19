package whyv401.smart_contract;

import org.web3j.crypto.Credentials;
import org.web3j.crypto.WalletUtils;
import org.web3j.ens.EnsResolutionException;
import org.web3j.protocol.Web3j;
import org.web3j.tx.gas.DefaultGasProvider;
import whyv401.controller.Utils;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.logging.Level;

public class HousingContract extends AbstractContract<Housing>{
    protected static final String HOME_REGIS = "Home registered";
    protected static final String FAIL_HOME_REGIS = "Failed to register home";

    private HousingContract(){
        contractMap = new HashMap<>();
    }

    public static synchronized HousingContract getInstance() {
        if(contract == null)
            contract = new HousingContract();
        return (HousingContract) contract;
    }

    public boolean addCredentials(String privateKey, String contractAddress, Web3j web3j){
        if(!WalletUtils.isValidPrivateKey(privateKey)) {
            logger.log(Level.WARNING, INVALID_KEY);
            return false;
        }
        try {
            Credentials credentials = Credentials.create(Utils.toHex(privateKey));
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
    public boolean getHousesForSale(String sender){
        if(!contains(sender)) return false;

        return false;
    }

    private boolean sellHouse(String sender, BigInteger houseId){
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
