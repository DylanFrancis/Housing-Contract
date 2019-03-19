package whyv401.smart_contract;

import com.squareup.javapoet.ClassName;
import org.web3j.protocol.Web3j;
import org.web3j.tx.Contract;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

public abstract class AbstractContract <T extends Contract>{
    protected final static Logger logger = Logger.getLogger(ClassName.class.getName());
    protected static AbstractContract contract;
    protected HashMap<String, T> contractMap;

    protected static final String KEY_LOCKED    = "Key not unlocked.";
    protected static final String KEY_UNLOCKED  = "Key unlocked.";
    protected static final String CON_LOADED    = "Contract loaded.";
    protected static final String CON_LOAD_ERR  = "Failed to load contract.";
    protected static final String INVALID_KEY   = "Invalid key.";
    protected static final String INVALID_WEI   = "Invalid initial wei.";

    public abstract boolean addCredentials(String privateKey, String contractAddress, Web3j web3j);

    public final void removeCredentials(String address){ contractMap.remove(address); }

    /**
     * Determines if string can be converted to BigInteger
     * @param w wei to be validated
     * @return wei in BigInt
     */
    protected final BigInteger validateWei(String w){
        if(w.equals("")) {
            logger.log(Level.WARNING, INVALID_WEI);
            return null;
        }
        BigInteger wei;
        try {
            wei = new BigInteger(w);
        }catch (NumberFormatException n) {
            logger.log(Level.WARNING, INVALID_WEI);
            return null;
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
