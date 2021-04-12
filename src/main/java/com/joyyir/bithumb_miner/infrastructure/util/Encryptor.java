package com.joyyir.bithumb_miner.infrastructure.util;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.Hex;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

public class Encryptor {
    private static final String HMAC_SHA512 = "HmacSHA512";

    public enum EncodeType { HEX, BASE64 }

    public static String getHmacSha512(String key, String data) {
        return getHmacSha512(key, data, EncodeType.HEX);
    }

    public static String getHmacSha512(String key, String data, EncodeType encodeType) {
        Mac sha512_HMAC;
        String result = null;

        try {
            byte[] byteKey = key.getBytes("UTF-8");
            sha512_HMAC = Mac.getInstance(HMAC_SHA512);
            SecretKeySpec keySpec = new SecretKeySpec(byteKey, HMAC_SHA512);
            sha512_HMAC.init(keySpec);
            byte[] macData = sha512_HMAC.doFinal(data.getBytes("UTF-8"));

            switch (encodeType) {
                case HEX:
                    result = bytesToHex(macData);
                    break;
                case BASE64:
                    byte[] hex = new Hex().encode(macData);
                    result = bytesToBase64(hex);
                    break;
                default:
                    throw new Exception();
            }
        }
        catch(Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public static String bytesToHex(byte[] bytes) {
        final char[] hexArray = "0123456789ABCDEF".toCharArray();
        char[] hexChars = new char[bytes.length * 2];
        for ( int j = 0; j < bytes.length; j++ ) {
            int v = bytes[j] & 0xFF;
            hexChars[j * 2] = hexArray[v >>> 4];
            hexChars[j * 2 + 1] = hexArray[v & 0x0F];
        }
        return new String(hexChars);
    }

    public static String bytesToBase64(byte[] bytes){
        return new String(Base64.encodeBase64(bytes));
    }
}