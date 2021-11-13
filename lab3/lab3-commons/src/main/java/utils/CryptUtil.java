package utils;

import org.apache.commons.codec.digest.DigestUtils;

public class CryptUtil {

        public static double add(double a, double b){
                return a + b;
        }

        public static String encryptThisString(String data){
                return DigestUtils.sha512Hex(data);
        }

}
