package cc.flyee.branchart.util;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Optional;

public class CodeUtil {

    private static final char[] DIGITS_LOWER = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};

    private static final String SECURITY_KEY = "7146442KDH";

    public static String sigature(String sQueryString) {
        String sFullUrl = sQueryString + "|" + SECURITY_KEY;
        return getEncodedMD5(sFullUrl);
    }

    public static String getEncodedMD5(String clearText) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(clearText.getBytes("UTF-8"));
            byte[] source = md.digest();
            return new String(encodeHex(source, DIGITS_LOWER));
        } catch (NoSuchAlgorithmException ex) {
            System.err.println(ex);
            return null;
        } catch (UnsupportedEncodingException ex1) {
            System.err.println(ex1);
            return null;
        }
    }

    public static char[] encodeHex(byte[] data, char[] toDigits) {
        int l = data.length;
        char[] out = new char[l << 1];
        for (int i = 0, j = 0; i < l; i++) {
            out[j++] = toDigits[(0xF0 & data[i]) >>> 4];
            out[j++] = toDigits[0x0F & data[i]];
        }
        return out;
    }

    public static void main(String[] args) {
//        String url1 = "/article/5b8a5ace98f7793a5026ba0a" + "|" + "02e2b9f0-b814-11e8-849c-d1077574833b";
        String url = "123456";
        String sign1 = sigature(url);
        System.out.println(sign1);
    }

}
