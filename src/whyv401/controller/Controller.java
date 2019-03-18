package whyv401.controller;

import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.ECKeyPair;
import org.web3j.crypto.Wallet;
import org.web3j.crypto.WalletUtils;
import org.web3j.ens.EnsResolutionException;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.methods.response.EthGetTransactionCount;
import org.web3j.protocol.core.methods.response.Web3ClientVersion;
import org.web3j.protocol.http.HttpService;
import org.web3j.tx.gas.ContractGasProvider;
import org.web3j.tx.gas.DefaultGasProvider;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.soap.Node;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import java.io.IOException;
import java.math.BigInteger;
import java.util.concurrent.ExecutionException;

public class Controller {
    //TODO: remove
    public static void main(String[] args) {
        Controller controller = new Controller();
//        controller.connect();
        controller.loadContract("0xcA0496f6345c2C8FFbaBC21F2af9884F1a1e4734", "8e0ff9398ccd8025d8b6f7d1e02757cfe9520368fa92c940540d676b0a3ba9db");
        controller.registerHome();
    }

    public Controller() {
        connect();
    }

    private Web3j web3j;

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
            //TODO: log
            return transactionCount.getTransactionCount();
        } catch (InterruptedException | ExecutionException e) {
            //TODO: log
            e.printStackTrace();
        }
        //TODO: log
        return null;
    }

    private void connect(){
        try {
            web3j = Web3j.build(new HttpService()); //8545
            Web3ClientVersion web3ClientVersion = web3j.web3ClientVersion().send();
            String clientVersion = web3ClientVersion.getWeb3ClientVersion();
            System.out.println(clientVersion);
            //TODO: log

            // (String contractAddress, Web3j web3j, Credentials credentials, ContractGasProvider contractGasProvider
//            Housing housing = Housing.load("0xAbc9F723eF62919326ca11627fd26d67Cb29E1fd", web3j, credentials, gasProvider);

        } catch (Exception e) {
            //TODO: log
            e.printStackTrace();
        }


    }

    public Button btnDeploy;
    public Button btnCitCon;
    public Button btnValCon;
    public TextField txtOwnKey;
    public TextField txtPOwnKey;
    public TextField txtValKey;
    public TextField txtCitKey;
    public TextField txtIniWei;

    //TODO: initialise
    public TextField txtContAddr;

    //TODO: learn to log
    public void onBtnDeploy(ActionEvent e){
        BigInteger wei = validateWei(txtIniWei.getText());
        if(wei == null){
            //TODO: log
            return;
        }
        if(!WalletUtils.isValidPrivateKey(txtPOwnKey.getText())) {
            //TODO: log
            return;
        }
        deployContract(txtOwnKey.getText(), txtPOwnKey.getText(), wei);
        //TODO: log
    }

    public void onBtnLoad(ActionEvent e){
//        BigInteger wei = validateWei(txtIniWei.getText());
        if(!WalletUtils.isValidPrivateKey(txtPOwnKey.getText())) {
            //TODO: log
            return;
        }
        loadContract(txtContAddr.getText(), Utils.toHex(txtPOwnKey.getText()));
        //TODO: log
    }

    /**
     * Determines if string can be converted to BigInteger
     * @param w
     * @return
     */
    private BigInteger validateWei(String w){
        if(w.equals(""))
            return null;
        BigInteger wei;
        try {
            wei = new BigInteger(txtIniWei.getText());
        }catch (NumberFormatException n) {
            System.out.println("Invalid wei amount");
            return null;
        }
        return wei;
    }

    private Housing housing;

    /**
     * Load a contract that's already deployed.
     * @param contractAddress
     * @param privateKey must arrive in hex format
     */
    private void loadContract(String contractAddress, String privateKey) {
        //TODO: no wei when loading???
        //TODO: load contract from blockchain
        // (String contractAddress, Web3j web3j, Credentials credentials, ContractGasProvider contractGasProvider)
        try {
            housing = Housing.load(contractAddress, web3j, Credentials.create(privateKey), new DefaultGasProvider());
            //TODO: log
        }catch (EnsResolutionException e){
            //TODO: log
            System.out.println("contract load error");
        }
    }

    private void registerHome(){
        housing.registerHome();
        //TODO: log
    }

    /**
     * Deploy a new contract to blockchain
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


    private void newWallet(String privateKey, String publicKey, String password){

    }

    // not needed
    private String encodeHex(byte[] bytes){
        char[] hexArray = "0123456789ABCDEF".toCharArray();
        char[] hexChars = new char[bytes.length * 2];
        for ( int j = 0; j < bytes.length; j++ ) {
            int v = bytes[j] & 0xFF;
            hexChars[j * 2] = hexArray[v >>> 4];
            hexChars[j * 2 + 1] = hexArray[v & 0x0F];
        }
        return new String(hexChars);
//        return Base64.getEncoder().encodeToString(bytes);
    }

    // TODO: find way of securing storing wallets
    private String getPrivateKey(String publicKey){
        try {
            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
            InputStream IS = Main.class.getResourceAsStream("/wallets.xml");
            Document doc = documentBuilder.parse(IS);

            XPathFactory xPathFactory = XPathFactory.newInstance();
            XPath xPath = xPathFactory.newXPath();

//            String expr = "/wallets/wallet/publicKey[text()=\"0x3331a0f111FeCf3C4EdAeED5deC18D3a11061f45\"]/../privateKey[text()]";
            String expr = "/wallets/wallet/publicKey[text()='" + publicKey + "']/../privateKey[text()]";
            String string = (String) xPath.compile(expr).evaluate(doc, XPathConstants.STRING);
            return string;
        } catch (ParserConfigurationException | SAXException | IOException | XPathExpressionException e) {
            e.printStackTrace();
        }
        return null;
    }
}