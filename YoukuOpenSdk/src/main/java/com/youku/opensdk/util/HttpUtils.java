package com.youku.opensdk.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.Map;

/**
 * Created by smy on 2016/4/1.
 */
public class HttpUtils {

    public static final String OPEN_API_REST_JSON = "http://openapi.youku.com/router/rest.json";
    public static final String URI_GET_APP_AUTHORIZE = "openapiv3/get_app_authorize";

    public static String get(String strUrl) {
        Logger.d("get url =  " + strUrl);
        BufferedReader br = null;
        StringBuilder sb = new StringBuilder();
        try {
            URL url = new URL(strUrl);
            URLConnection conn = url.openConnection();
            br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line = null;
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (null != br) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        String result = sb.toString();
        Logger.d("http get result : " + result);
        return result;
    }

    public static String post(String strUrl, Map<String, String> params) {
        StringBuilder sb = new StringBuilder();
        if (params != null & params.size() > 0) {
            for (Map.Entry<String, String> entry : params.entrySet()) {
                sb.append(entry.getKey()).append("=").append(entry.getValue());
                sb.append("&");

            }
            sb.deleteCharAt(sb.length() - 1);
        }
        String paramsStr = sb.toString();
        Logger.d("post url = " + strUrl + ", params = " + paramsStr);
        byte[] entity = paramsStr.getBytes();
        OutputStream out = null;
        InputStream is = null;
        try {
            URL url = new URL(strUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setConnectTimeout(2000);
            conn.setRequestMethod("POST");
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            conn.setRequestProperty("Content-Length", entity.length + "");
            out = conn.getOutputStream();
            out.write(entity);
            out.flush();
            is = conn.getInputStream();
            sb.delete(0, sb.length() - 1);
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            String line = null;
            while (null != (line = br.readLine())) {
                sb.append(line);
            }
            String result = sb.toString();
            Logger.d("post result = " + result);
            return result;
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Logger.d("post result = " + sb.toString());
        return null;
    }


}
