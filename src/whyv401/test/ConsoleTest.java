package whyv401.test;

import org.web3j.crypto.Credentials;
import whyv401.smart_contract.HousingContract;

import java.io.IOException;
import java.math.BigInteger;
import java.security.InvalidKeyException;
import java.util.List;

public class ConsoleTest {
    public static void main(String[] args) {
        new ConsoleTest(args);
    }

    private static HousingContract housingContract;
    private String contractAddress;
    private Credentials owner;
    private Credentials citizen;
    private Credentials valuator;

    public ConsoleTest(String[] args) {
        try {
            housingContract = HousingContract.getInstance(args[0]);
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            initialiseCredentials(args);
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        }

        try {
            runTests();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initialiseCredentials(String[] args) throws InvalidKeyException {
        contractAddress = args[0];
        owner = Credentials.create(args[1]);
        valuator = Credentials.create(args[2]);
        citizen = Credentials.create(args[3]);

        housingContract.addCredentials(args[1], contractAddress);
        housingContract.addCredentials(args[2], contractAddress);
        housingContract.addCredentials(args[3], contractAddress);
    }

    private void runTests() throws Exception {
        housingContract.registerHome(citizen.getAddress());
        housingContract.registerHome(citizen.getAddress());
        housingContract.registerHome(citizen.getAddress());
        housingContract.registerHome(citizen.getAddress());

        housingContract.sellHouse(citizen.getAddress(), new BigInteger("1"));
        housingContract.sellHouse(citizen.getAddress(), new BigInteger("2"));
        housingContract.sellHouse(valuator.getAddress(), new BigInteger("0"));

        List list = housingContract.getHousesForSale(valuator.getAddress());
        list.forEach(System.out::println);


    }
}
