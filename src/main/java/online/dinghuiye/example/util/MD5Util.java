package online.dinghuiye.example.util;

import sun.misc.BASE64Encoder;

import java.security.MessageDigest;
import java.util.Base64;

/**
 * @author Strangeen on 2017/08/30
 */
public class MD5Util {

    public static String encode(String source) {

        if (source == null) return null;
        try {
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            BASE64Encoder base64 = new BASE64Encoder();
            return base64.encode(md5.digest(source.getBytes("utf-8")));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
