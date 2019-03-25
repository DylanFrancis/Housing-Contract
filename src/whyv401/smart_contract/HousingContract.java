package whyv401.smart_contract;

import org.web3j.crypto.Credentials;
import org.web3j.crypto.WalletUtils;
import org.web3j.ens.EnsResolutionException;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.tx.gas.DefaultGasProvider;

import java.io.IOException;
import java.math.BigInteger;
import java.security.InvalidKeyException;
import java.util.List;
import java.util.logging.Level;

public class HousingContract extends AbstractContract<Housing>{
    protected static final String HOME_REGIS = "Home registered";
    protected static final String FAIL_HOME_REGIS = "Failed to register home";

    protected HousingContract(String contractAddr) throws IOException { super(contractAddr); }

    public static synchronized HousingContract getInstance(String contractAddr) throws IOException {
        if(contract == null)
            contract = new HousingContract(contractAddr);
        return (HousingContract) contract;
    }

    public String addCredentials(String privateKey, String contractAddress) throws EnsResolutionException, InvalidKeyException {
        if(!WalletUtils.isValidPrivateKey(privateKey)) {
            logger.log(Level.WARNING, INVALID_KEY);
            throw new InvalidKeyException(INVALID_KEY);
        }
        try {
            Credentials credentials = Credentials.create(privateKey);
            if(contractMap.containsKey(credentials.getAddress()))
                throw new InvalidKeyException("Key already exists");
            Housing housing = Housing.load(contractAddress, web3j, credentials, new DefaultGasProvider());
            contractMap.put(credentials.getAddress(), housing);
            logger.log(Level.INFO, CON_LOADED);
            return credentials.getAddress();
        }catch (EnsResolutionException e){
            logger.log(Level.SEVERE, CON_LOAD_ERR);
            throw e;
        }
    }

    public String[] stealHouse(String sender, BigInteger houseId) throws Exception{
        if(!contains(sender)) throw new Exception(KEY_LOCKED);
        try {
            TransactionReceipt r = contractMap.get(sender).stealHouse(houseId).send();
            return receipt(r);
        } catch (Exception e) {
            logger.log(Level.WARNING, e.toString(), e);
            throw e;
        }
    }

    public String[] stealHouseKindly(String sender, BigInteger houseId, String weiValue) throws Exception{
        if(!contains(sender)) throw new Exception(KEY_LOCKED);
        BigInteger w = validateWei(weiValue);
        try {
            TransactionReceipt r = contractMap.get(sender).stealHouseKindly(houseId, w).send();
            return receipt(r);
        } catch (Exception e) {
            logger.log(Level.WARNING, e.toString(), e);
            throw e;
        }
    }

    public String[] approveValuator(String sender, String valuator)throws Exception{
        if(!contains(sender)) throw new Exception(KEY_LOCKED);
        try {
            TransactionReceipt r = contractMap.get(sender).approveValuator(valuator).send();
            return receipt(r);
        } catch (Exception e) {
            logger.log(Level.WARNING, e.toString(), e);
            throw e;
        }
    }

    public String[] applyForValuator(String sender) throws Exception{
        if(!contains(sender)) throw new Exception(KEY_LOCKED);
        try {
            TransactionReceipt r = contractMap.get(sender).applyForValuator().send();
            return receipt(r);
        } catch (Exception e) {
            logger.log(Level.WARNING, e.toString(), e);
            throw e;
        }
    }

    public String[] assignValue(String sender, BigInteger houseId, BigInteger value) throws Exception {
        if(!contains(sender)) throw new Exception(KEY_LOCKED);
        try {
            TransactionReceipt r = contractMap.get(sender).assignValue(houseId, value).send();
            return receipt(r);
        } catch (Exception e) {
            logger.log(Level.WARNING, e.toString(), e);
            throw e;
        }
    }

    public String[] registerHome(String sender) throws Exception {
        if(!contains(sender)) throw new Exception(KEY_LOCKED);
        try {
            TransactionReceipt r = contractMap.get(sender).registerHome().send();
            logger.log(Level.INFO, HOME_REGIS);
            return receipt(r);
        } catch (Exception e) {
            logger.log(Level.WARNING, FAIL_HOME_REGIS);
            logger.log(Level.WARNING, e.toString(), e);
            throw e;
        }

    }

    public List getHousesForSale(String sender) throws Exception {
        if(!contains(sender)) return null;
        try {
            List list = contractMap.get(sender).getAllHousesForSale().send();
            return list;
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    public String[] sellHouse(String sender, BigInteger houseId) throws Exception {
        if(!contains(sender)) throw new Exception(KEY_LOCKED);
        try {
            TransactionReceipt r = contractMap.get(sender).sellHouse(houseId).send();
            return receipt(r);
        } catch (Exception e) {
            logger.log(Level.WARNING, e.toString(), e);
            throw e;
        }

    }

    public String[] buyHouse(String sender, BigInteger houseId, String weiValue) throws Exception {
        if(!contains(sender)) throw new Exception(KEY_LOCKED);
        BigInteger w = validateWei(weiValue);
        try {
            TransactionReceipt r = contractMap.get(sender).buyHouse(houseId, w).send();
            return receipt(r);
        } catch (Exception e) {
            logger.log(Level.WARNING, e.toString(), e);
            throw e;
        }

    }
}
