package whyv401.controller;

import com.squareup.javapoet.ClassName;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.ECKeyPair;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.methods.response.EthGetTransactionCount;
import org.web3j.protocol.core.methods.response.Web3ClientVersion;
import org.web3j.protocol.http.HttpService;
import org.web3j.tx.gas.ContractGasProvider;
import org.web3j.tx.gas.DefaultGasProvider;
import whyv401.smart_contract.HousingContract;
import whyv401.smart_contract.Housing;

import java.math.BigInteger;
import java.util.concurrent.ExecutionException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Controller {
    private final static Logger logger = Logger.getLogger(ClassName.class.getName());
    private Web3j web3j;
    private HousingContract housingContract = HousingContract.getInstance();
    private Housing housing;

    public static void main(String[] args) {

    }

    /**
     * Connects to blockchain at port 8545
     */
    public void connect(){
        try {
            web3j = Web3j.build(new HttpService()); //8545
            logger.log(Level.INFO, "Connected to web3j");
            Web3ClientVersion web3ClientVersion = web3j.web3ClientVersion().send();
            String clientVersion = web3ClientVersion.getWeb3ClientVersion();
            System.out.println(clientVersion);
            // (String contractAddress, Web3j web3j, Credentials credentials, ContractGasProvider contractGasProvider
//            Housing housing = Housing.load("0xAbc9F723eF62919326ca11627fd26d67Cb29E1fd", web3j, credentials, gasProvider);
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Failed to connect to web3j");
            logger.log(Level.SEVERE, e.toString(), e);
        }
    }

    public Controller() {
        connect();
    }

    /**
     *
     * @param address
     */
    private void transaction(String address){}

    /**
     * Get the nonce
     * Used for transactions, can only be used once for a transaction
     * Until a transaction is minced, any subsequent transactions will be rejected
     * @param address
     * @return
     */
    private BigInteger getNonce(String address){
        try {
            // (address, DefaultBlockParameter)
            EthGetTransactionCount transactionCount = web3j.ethGetTransactionCount(address, DefaultBlockParameterName.LATEST)
                    .sendAsync()
                    .get();
            logger.log(Level.INFO, "Got nonce");
            return transactionCount.getTransactionCount();
        } catch (InterruptedException | ExecutionException e) {
            logger.log(Level.SEVERE, "Failed to retrieve nonce");
            logger.log(Level.SEVERE, e.toString(), e);
        }
        return null;
    }
    //    public Button btnDeploy;
    public Button btnCitCon;
    public Button btnValCon;
    public TextField txtOwnKey;
    public TextField txtCitKey;
    public TextField txtContAddr;
    public TextField txtValKey;

    //TODO: initialise
    public TextField txtPOwnKey;
    public TextField txtIniWei;

    //TODO: remove
//    public void onBtnDeploy(ActionEvent e){
//        BigInteger wei = validateWei(txtIniWei.getText());
//        if(wei == null){
//            logger.log(Level.WARNING, "Invalid initial wei");
//            return;
//        }
//        if(!WalletUtils.isValidPrivateKey(txtOwnKey.getText())) {
//            logger.log(Level.WARNING, "Invalid private key");
//            return;
//        }
//        deployContract(txtOwnKey.getText(), txtOwnKey.getText(), wei);
//        logger.log(Level.INFO, "HousingContract deployed");
//    }

    public void onBtnLoad(ActionEvent e){
        housingContract.addCredentials(txtOwnKey.getText(), txtContAddr.getText(), web3j);
    }

    //TODO: remove
    /**
     * Deploy a new housingContract to blockchain
     * Doesn't work, throws array out of bound exception, pre-load contact
     * @param publicKey
     * @param privateKey
     * @param initialWei
     */
    // https://ethereum.stackexchange.com/questions/25490/using-web3j-to-load-a-smart-contract-how-do-i-specify-my-credentials
    private void deployContract(String publicKey, String privateKey, BigInteger initialWei){
        //TODO: fix credentials bug
//        Credentials credentials = Credentials.create(getPrivateKey(publicKey), publicKey);
        String hexPrivateKey = String.format("%040x", new BigInteger(1, privateKey.getBytes()));
        String hexPublicKey = String.format("%040x", new BigInteger(1, publicKey.getBytes()));
//        Credentials credentials = Credentials.create(hexPrivateKey, hexPublicKey);
        ECKeyPair keyPair = new ECKeyPair(new BigInteger(hexPrivateKey), new BigInteger(hexPublicKey));

        Credentials credentials = Credentials.create(keyPair);

//        Credentials credentials = Credentials.create(privateKey);
//        Credentials credentials = Credentials.create(encodeHex(privateKey.getBytes()), encodeHex(publicKey.getBytes()));

        ContractGasProvider gasProvider = new DefaultGasProvider();
        try {
            // (Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit, BigInteger initialWeiValue)
//            Housing house = Housing.deploy(web3j, credentials, gasProvider, initialWei).send();

            System.out.println("sent housing");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}