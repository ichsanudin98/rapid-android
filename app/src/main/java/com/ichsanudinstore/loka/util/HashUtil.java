package com.ichsanudinstore.loka.util;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * @author Ichsanudin_Chairin
 * @since Wednesday 8/14/2019 6:45 PM
 */
public class HashUtil {
    private static String getHash(String text, String hashType) {
        try {
            MessageDigest messageDigest = MessageDigest.getInstance(hashType);
            byte[] array = new byte[0];
            array = messageDigest.digest(text.getBytes(StandardCharsets.UTF_8));

            StringBuilder mStringBuilder = new StringBuilder();
            for (byte anArray : array) {
                mStringBuilder.append(Integer.toHexString((anArray & 0xFF) | 0x100)
                        .substring(1, 3));
            }

            return mStringBuilder.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static String MD2(String text) {
        return HashUtil.getHash(text, "MD2");
    }

    public static String MD5(String text) {
        return HashUtil.getHash(text, "MD5");
    }

    public static String SHA1(String text) {
        return HashUtil.getHash(text, "SHA1");
    }

    public static String SHA224(String text) {
        return HashUtil.getHash(text, "SHA-224");
    }

    public static String SHA256(String text) {
        return HashUtil.getHash(text, "SHA-256");
    }

    public static String SHA384(String text) {
        return HashUtil.getHash(text, "SHA-384");
    }

    public static String SHA512(String text) {
        return HashUtil.getHash(text, "SHA-512");
    }
}
