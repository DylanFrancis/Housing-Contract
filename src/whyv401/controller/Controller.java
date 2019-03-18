package whyv401.controller;

import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.WalletUtils;
import org.web3j.protocol.Web3j;
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
import java.io.IOException;
import java.io.InputStream;

import java.io.IOException;
import java.math.BigInteger;
import java.net.URL;
import java.util.ResourceBundle;

public class Controller implements Initializable {
    public static void main(String[] args) {
        Controller controller = new Controller();
        controller.connect();
    }

    public Controller() {
        connect();
    }

    private Web3j web3j;

    private void connect(){
        try {
            web3j = Web3j.build(new HttpService());
            Web3ClientVersion web3ClientVersion = web3j.web3ClientVersion().send();
            String clientVersion = web3ClientVersion.getWeb3ClientVersion();
            System.out.println(clientVersion);

            // (String contractAddress, Web3j web3j, Credentials credentials, ContractGasProvider contractGasProvider
//            Housing housing = Housing.load("0xAbc9F723eF62919326ca11627fd26d67Cb29E1fd", web3j, credentials, gasProvider);




        } catch (Exception e) {
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

    public void onBtnDeploy(ActionEvent e){
        if(txtOwnKey.getText().equals("") || txtIniWei.getText().equals(""))
            return;
        deployContract(txtOwnKey.getText(), txtPOwnKey.getText(), new BigInteger(txtIniWei.getText()));
    }

    // https://ethereum.stackexchange.com/questions/25490/using-web3j-to-load-a-smart-contract-how-do-i-specify-my-credentials
    private void deployContract(String publicKey, String privateKey, BigInteger initialWei){
//        Credentials credentials = Credentials.create(getPrivateKey(publicKey), publicKey);
        String hexPrivateKey = String.format("%040x", new BigInteger(1, privateKey.getBytes()));
        String hexPublicKey = String.format("%040x", new BigInteger(1, publicKey.getBytes()));
        Credentials credentials = Credentials.create(hexPrivateKey, hexPublicKey);
//        Credentials credentials = Credentials.create(encodeHex(privateKey.getBytes()), encodeHex(publicKey.getBytes()));

        ContractGasProvider gasProvider = new DefaultGasProvider();
        try {
            // (Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit, BigInteger initialWeiValue)
            Housing.deploy(web3j, credentials, gasProvider, initialWei).send();
            System.out.println("sent housing");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

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

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }
}