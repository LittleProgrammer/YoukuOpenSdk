package com.youku.opensdk.util;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Collections;
import java.util.Iterator;
import java.util.SortedMap;

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

    public static JSONObject signal(SortedMap<String, String> params, String secret) throws JSONException {
        return signal(params, secret, Method.MD5);
    }

    //确保params中的参数值进行了UTF-8的URLEncode。参数值为空的参数，也要加入到签名字符串中。
    public static JSONObject signal(SortedMap<String, String> params, String secret, Method signMethod) throws JSONException {

        // 第一步：检查参数是否已经排序
        SortedMap<String, String> sMap = Collections.synchronizedSortedMap(params);
        // 第二步：把所有参数名和参数值串在一起
        StringBuilder query = new StringBuilder();
        Iterator<String> it = sMap.keySet().iterator();
        JSONObject json = new JSONObject();
        while (it.hasNext()) {
            String key = it.next();
            String value = sMap.get(key);
            query.append(key).append(value);
            json.put(key, value);
        }

        // 第三步：使用MD5/HMAC加密
        switch (signMethod) {
            case MD5:
                query.append(secret);
                //32位小写md5加密
                String md5 = Md5.getMd5(query.toString()).toLowerCase();
                json.put("sign", md5);
                break;
            case HMACSHA256:
                byte[] bytes = encryptHMAC(query.toString(), secret);
                // 第四步：把二进制转化为大写的十六进制
                String hMac = byteToHex(bytes);
                json.put("sign", hMac);
                break;
            default:
                break;
        }
        return json;
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
