package whyv401.controller;

import java.math.BigInteger;

public class Utils {

    public static String toHex(String s){
        return String.format("%040x", new BigInteger(1, s.getBytes()));
    }
}
