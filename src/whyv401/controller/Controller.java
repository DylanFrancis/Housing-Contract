package whyv401.controller;

import com.squareup.javapoet.ClassName;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import org.web3j.ens.EnsResolutionException;
import org.web3j.protocol.Web3j;
import whyv401.smart_contract.HousingContract;
import whyv401.smart_contract.Housing;

import java.io.IOException;
import java.math.BigInteger;
import java.security.InvalidKeyException;
import java.util.List;
import java.util.logging.Logger;

public class Controller {
    private final static Logger logger = Logger.getLogger(ClassName.class.getName());
    private Web3j web3j;
    private HousingContract housingContract;
    private String contractAddress;
    private String owner;
    private String valuator;
    private String citizen;
    private Housing housing;

    public static void main(String[] args) {

    }

    public Controller() {}

    private static final String SUCCESS = "Success";
    private static final String ERROR = "Error";

    // Start
    public TextField txtContAddr;
    public Button btnConnect;
    public TextArea txtOut;

    public void btnOnConnect(ActionEvent actionEvent){
        String contAddr = txtContAddr.getText();
        try {
            housingContract = HousingContract.getInstance(contAddr);
            contractAddress = contAddr;
            outputNewLine(SUCCESS);
            enableLoad();
        } catch (IOException e) {
            outputNewLine(e.toString());
        }
    }

    private void outputNewLine(String s){
        txtOut.appendText("\n" + s);
    }

    // Owner
    public TextField txtOwnKey;
    public TextField txtOwnHID;
    public TextField txtValAddr;
    public TextField txtOwnRem;
    public Button btnDelOwnKey;
    public Button btnLoadOwnKey;
    public Button btnSeizeHouse;
    public Button btnSeizeHouseRem;
    public Button btnValAppr;

    public void btnOnLoadOwnKey(ActionEvent actionEvent){
        String pk = txtOwnKey.getText();
        txtOwnKey.setText("");
        try {
            owner = housingContract.addCredentials(pk, contractAddress);
            enableOwner();
            outputNewLine(SUCCESS);
        }catch (EnsResolutionException | InvalidKeyException e){
            outputNewLine(e.toString());
            e.printStackTrace();
        }
    }
    public void btnOnDelOwnKey(ActionEvent actionEvent){
        housingContract.removeCredentials(owner);
        owner = "";
        disableOwner();
    }

    public void btnOnSeizeHouse(ActionEvent actionEvent){
        String id = txtOwnHID.getText();
        try {
            housingContract.stealHouse(owner, new BigInteger(id));
            outputNewLine(SUCCESS);
        } catch (Exception e) {
            outputNewLine(e.toString());
            e.printStackTrace();
        }
    }

    public void btnOnSeizeHouseRem(ActionEvent actionEvent){
        String id = txtOwnHID.getText();
        String rem = txtOwnRem.getText();
        try {
            housingContract.stealHouseKindly(owner, new BigInteger(id), rem);
            outputNewLine(SUCCESS);
        } catch (Exception e) {
            outputNewLine(e.toString());
            e.printStackTrace();
        }
    }

    public void btnOnValAppr(ActionEvent actionEvent){
        String val = txtValAddr.getText();
        try {
            housingContract.approveValuator(owner, val);
            outputNewLine(SUCCESS);
        } catch (Exception e) {
            outputNewLine(e.toString());
            e.printStackTrace();
        }
    }

    private void enableLoad(){
        txtOwnKey.setDisable(false);
        txtValKey.setDisable(false);
        txtCitKey.setDisable(false);
        btnLoadOwnKey.setDisable(false);
        btnLoadVal.setDisable(false);
        btnLoadCit.setDisable(false);
    }

    private void enableOwner(){
        txtOwnKey.setDisable(true);
        btnLoadOwnKey.setDisable(true);
        txtOwnHID.setDisable(false);
        txtValAddr.setDisable(false);
        txtOwnRem.setDisable(false);
        btnDelOwnKey.setDisable(false);
        btnSeizeHouse.setDisable(false);
        btnSeizeHouseRem.setDisable(false);
        btnValAppr.setDisable(false);
    }

    private void disableOwner(){
        txtOwnKey.setDisable(false);
        btnLoadOwnKey.setDisable(false);
        txtOwnRem.setDisable(true);
        txtOwnHID.setDisable(true);
        txtValAddr.setDisable(true);
        btnDelOwnKey.setDisable(true);
        btnSeizeHouse.setDisable(true);
        btnSeizeHouseRem.setDisable(true);
        btnValAppr.setDisable(true);
    }

    // Valuator
    public TextField txtValKey;
    public TextField txtValHID;
    public TextField txtValHValue;
    public Button btnLoadVal;
    public Button btnDelValKey;
    public Button btnValAssign;
    public Button btnValApp;

    public void btnOnLoadValKey(ActionEvent actionEvent){
        String pk = txtValKey.getText();
        txtValKey.setText("");
        try {
            valuator = housingContract.addCredentials(pk, contractAddress);
            enableValuator();
            outputNewLine(SUCCESS);
        } catch (InvalidKeyException e) {
            outputNewLine(e.toString());
            e.printStackTrace();
        }
    }

    public void btnOnDelValKey(ActionEvent actionEvent){
        housingContract.removeCredentials(valuator);
        valuator = "";
        disableValuator();
    }

    public void btnOnValAssign(ActionEvent actionEvent){
        try {
            housingContract.assignValue(valuator, new BigInteger(txtValHID.getText()), new BigInteger(txtValHValue.getText()));
            outputNewLine(SUCCESS);
        } catch (Exception e) {
            outputNewLine(e.toString());
            e.printStackTrace();
        }
    }

    public void btnOnValApp(ActionEvent actionEvent){
        try {
            housingContract.applyForValuator(valuator);
            outputNewLine(SUCCESS);
        } catch (Exception e) {
            outputNewLine(e.toString());
            e.printStackTrace();
        }
    }

    private void enableValuator(){
        txtValKey.setDisable(true);
        txtValKey.setDisable(true);
        txtValHID.setDisable(false);
        txtValHValue.setDisable(false);
        btnDelValKey.setDisable(false);
        btnValAssign.setDisable(false);
        btnValApp.setDisable(false);
    }

    private void disableValuator(){
        txtValKey.setDisable(false);
        txtValKey.setDisable(false);
        txtValHID.setDisable(true);
        txtValHValue.setDisable(true);
        btnDelValKey.setDisable(true);
        btnValAssign.setDisable(true);
        btnValApp.setDisable(true);
    }

    // Citizen
    public TextField txtCitKey;
    public TextField txtSellCitHID;
    public TextField txtBuyCitHID;
    public TextField txtHouseAmt;
    public Button btnLoadCit;
    public Button btnRegHouse;
    public Button btnHouseSale;
    public Button btnSellHouse;
    public Button btnBuyHouse;
    public Button btnDelCitKey;

    public void btnOnLoadCitKey(ActionEvent actionEvent){
        String pk = txtCitKey.getText();
        txtCitKey.setText("");
        try {
            citizen = housingContract.addCredentials(pk, contractAddress);
            enableCitizen();
            outputNewLine(SUCCESS);
        } catch (InvalidKeyException e) {
            outputNewLine(e.toString());
            e.printStackTrace();
        }
    }

    public void btnOnDelCitKey(ActionEvent actionEvent){
        housingContract.removeCredentials(citizen);
        citizen = "";
        disableCitizen();
    }

    public void btnOnRegHouse(ActionEvent actionEvent){
        try {
            housingContract.registerHome(citizen);
            outputNewLine(SUCCESS);
        } catch (Exception e) {
            outputNewLine(e.toString());
            e.printStackTrace();
        }
    }

    public void btnOnHouseSale(ActionEvent actionEvent){
        try {
            List list = housingContract.getHousesForSale(citizen);
            outputNewLine(SUCCESS);
            list.forEach(obj -> outputNewLine(obj.toString()));
        } catch (Exception e) {
            outputNewLine(e.toString());
            e.printStackTrace();
        }
    }

    public void btnOnSellHouse(ActionEvent actionEvent){
        try {
            housingContract.sellHouse(citizen, new BigInteger(txtSellCitHID.getText()));
            outputNewLine(SUCCESS);
        } catch (Exception e) {
            outputNewLine(e.toString());
            e.printStackTrace();
        }
    }

    public void btnOnBuyHouse(ActionEvent actionEvent){
        try {
            housingContract.buyHouse(citizen, new BigInteger(txtBuyCitHID.getText()), txtHouseAmt.getText());
            outputNewLine(SUCCESS);
        } catch (Exception e) {
            outputNewLine(e.toString());
            e.printStackTrace();
        }
    }

    private void enableCitizen(){
        txtCitKey.setDisable(true);
        btnLoadCit.setDisable(true);
        txtSellCitHID.setDisable(false);
        txtBuyCitHID.setDisable(false);
        txtHouseAmt.setDisable(false);
        btnRegHouse.setDisable(false);
        btnHouseSale.setDisable(false);
        btnSellHouse.setDisable(false);
        btnBuyHouse.setDisable(false);
        btnDelCitKey.setDisable(false);
    }

    private void disableCitizen(){
        txtCitKey.setDisable(false);
        btnLoadCit.setDisable(false);
        txtSellCitHID.setDisable(true);
        txtBuyCitHID.setDisable(true);
        txtHouseAmt.setDisable(true);
        btnRegHouse.setDisable(true);
        btnHouseSale.setDisable(true);
        btnSellHouse.setDisable(true);
        btnBuyHouse.setDisable(true);
        btnDelCitKey.setDisable(true);
    }

}