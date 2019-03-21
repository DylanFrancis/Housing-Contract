package whyv401.test;

import org.web3j.crypto.Credentials;
import whyv401.smart_contract.HousingContract;

import java.math.BigInteger;
import java.util.List;

public class Test {
    public static void main(String[] args) {
        new Test(args);
    }

    private static HousingContract housingContract;
    private String contractAddress;
    private Credentials owner;
    private Credentials citizen;
    private Credentials valuator;

    public Test(String[] args) {
        housingContract = HousingContract.getInstance(args[0]);
        initialiseCredentials(args);

        runTests();
    }

    private void initialiseCredentials(String[] args){
        contractAddress = args[0];
        owner = Credentials.create(args[1]);
        valuator = Credentials.create(args[2]);
        citizen = Credentials.create(args[3]);

        housingContract.addCredentials(args[1], contractAddress);
        housingContract.addCredentials(args[2], contractAddress);
        housingContract.addCredentials(args[3], contractAddress);
    }

    private void runTests(){
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
