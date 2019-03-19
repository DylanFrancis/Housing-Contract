package whyv401;

import org.web3j.crypto.Credentials;
import whyv401.controller.Controller;
import whyv401.smart_contract.Housing;

import java.math.BigInteger;

public class Test {
    public static void main(String[] args) {
        new Test(args);
    }

    private static Controller controller = new Controller();
    private String contractAddress;
    private Credentials owner;
    private Credentials citizen;
    private Credentials valuator;
    private Housing ownerContract;
    private Housing citizenContract;
    private Housing valuatorContract;

    public Test(String[] args) {
        controller.connect();
        initialiseCredentials(args);
        initialiseContracts();

        runTests();
    }

    private void initialiseCredentials(String[] args){
        contractAddress = args[0];
        owner = Credentials.create(args[1]);
        valuator = Credentials.create(args[2]);
        citizen = Credentials.create(args[3]);
    }

    private void initialiseContracts(){
        ownerContract = controller.loadContract(contractAddress, owner);
        citizenContract = controller.loadContract(contractAddress, citizen);
        valuatorContract = controller.loadContract(contractAddress, valuator);
    }

    private void runTests(){
//        approveValuator();
//        controller.assignValue(valuatorContract, new BigInteger("1"), new BigInteger("1000"));
        controller.assignValue(citizenContract, new BigInteger("1"), new BigInteger("1000"));
    }

    private void approveValuator(){
        controller.approveValuator(ownerContract, valuator);
    }
}
