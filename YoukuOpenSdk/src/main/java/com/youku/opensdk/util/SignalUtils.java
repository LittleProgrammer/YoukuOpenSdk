package com.youku.opensdk.util;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Map;

import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

/**
 * Created by smy on 2016/4/1.
 */
public class SignalUtils {

    enum Method {
        MD5, HMACSHA256
    }

    public static String signal(Map<String, String> params, String secret) {
        return signal(params, secret, Method.MD5);
    }

    //确保params中的参数值进行了UTF-8的URLEncode。参数值为空的参数，也要加入到签名字符串中。
    public static String signal(Map<String, String> params, String secret, Method signMethod) {

        // 第一步：检查参数是否已经排序
        String[] keys = params.keySet().toArray(new String[0]);
        Arrays.sort(keys);

        // 第二步：把所有参数名和参数值串在一起
        StringBuilder query = new StringBuilder();
        for (String key : keys) {
            String value = params.get(key);
            query.append(key).append(value);
        }

        // 第三步：使用MD5/HMAC加密
        switch (signMethod) {
            case MD5:
                query.append(secret);
                //32位小写md5加密
                return Md5.getMd5(query.toString()).toLowerCase();
            case HMACSHA256:
                byte[] bytes = encryptHMAC(query.toString(), secret);
                // 第四步：把二进制转化为大写的十六进制
                return byteToHex(bytes);
            default:
                return null;
        }
    }

    private static byte[] encryptHMAC(String data, String secret) {
        byte[] bytes = null;
        try {
            SecretKey secretKey = new SecretKeySpec(secret.getBytes("UTF-8"), "HmacSHA256");
            Mac mac = Mac.getInstance(secretKey.getAlgorithm());
            mac.init(secretKey);
            bytes = mac.doFinal(data.getBytes("UTF-8"));
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return bytes;
    }

    private static String byteToHex(byte[] bytes) {
        StringBuilder sign = new StringBuilder();
        for (int i = 0; i < bytes.length; i++) {
            String hex = Integer.toHexString(bytes[i] & 0xFF);
            if (hex.length() == 1) {
                sign.append("0");
            }
            sign.append(hex.toUpperCase());
        }
        return sign.toString();
    }
}
