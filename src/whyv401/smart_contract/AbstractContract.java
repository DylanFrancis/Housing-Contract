package whyv401.smart_contract;

import com.squareup.javapoet.ClassName;
import org.web3j.ens.EnsResolutionException;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.methods.response.Web3ClientVersion;
import org.web3j.protocol.http.HttpService;
import org.web3j.tx.Contract;

import java.io.IOException;
import java.math.BigInteger;
import java.security.InvalidKeyException;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

public abstract class AbstractContract <T extends Contract>{
    protected final static Logger logger = Logger.getLogger(ClassName.class.getName());
    protected static AbstractContract contract;
    protected static Web3j web3j;
    protected static String contractAddress;
    protected HashMap<String, T> contractMap;

    protected static final String CONNECTED     = "Connected to web3j.";
    protected static final String FAIL_CONNECT  = "Failed to connect to web3j.";
    protected static final String KEY_LOCKED    = "Key not unlocked.";
    protected static final String KEY_UNLOCKED  = "Key unlocked.";
    protected static final String CON_LOADED    = "Contract loaded.";
    protected static final String CON_LOAD_ERR  = "Failed to load contract.";
    protected static final String INVALID_KEY   = "Invalid key.";
    protected static final String INVALID_WEI   = "Invalid initial wei.";
    protected static final String CONT_SUCCESS  = "Contract successful.";
    protected static final String CONT_FAIL     = "Contract failed.";

    protected AbstractContract(String contractAddr) throws IOException {
        contractAddress = contractAddr;
        contractMap = new HashMap<>();
        connect();
    }

    public abstract String addCredentials(String privateKey, String contractAddress) throws EnsResolutionException, InvalidKeyException;

    public final void removeCredentials(String address){ contractMap.remove(address); }

    private void connect() throws IOException {
        try {
            web3j = Web3j.build(new HttpService()); //8545
            logger.log(Level.INFO, CONNECTED);
            Web3ClientVersion web3ClientVersion = web3j.web3ClientVersion().send();
            String clientVersion = web3ClientVersion.getWeb3ClientVersion();
            logger.log(Level.INFO, clientVersion);
        } catch (IOException e) {
            logger.log(Level.SEVERE, FAIL_CONNECT);
            logger.log(Level.SEVERE, e.toString(), e);
            throw e;
        }
    }

    /**
     * Determines if string can be converted to BigInteger
     * @param w wei to be validated
     * @return wei in BigInt
     */
    protected final BigInteger validateWei(String w)throws NumberFormatException{
        if(w.equals("")) {
            logger.log(Level.WARNING, INVALID_WEI);
            throw new NumberFormatException();
        }
        BigInteger wei;
        try {
            wei = new BigInteger(w);
        }catch (NumberFormatException n) {
            logger.log(Level.WARNING, INVALID_WEI);
            throw n;
        }
        return wei;
    }

    protected final boolean contains(String sender){
        if(!contractMap.containsKey(sender)){
            logger.log(Level.WARNING, KEY_LOCKED);
            return false;
        }
        logger.log(Level.INFO, KEY_UNLOCKED);
        return true;
    }
}
