package com.youku.opensdk.util;

import android.text.TextUtils;

import java.io.IOException;

import cz.msebera.android.httpclient.HttpResponse;
import cz.msebera.android.httpclient.client.HttpClient;
import cz.msebera.android.httpclient.client.methods.HttpGet;
import cz.msebera.android.httpclient.client.methods.HttpRequestBase;
import cz.msebera.android.httpclient.impl.client.DefaultHttpClient;
import cz.msebera.android.httpclient.util.EntityUtils;

/**
 * Created by smy on 2016/4/1.
 */
public class HttpUtils {

    public static final String OPEN_API_REST_JSON = "http://openapi.youku.com/router/rest.json";
    public static final String URI_GET_APP_AUTHORIZE = "youku.default.ability.search";


    public static String get(String url) throws IOException {
        if (TextUtils.isEmpty(url)) {
            return null;
        }
        Logger.d("get url =  " + url);
        HttpClient client = new DefaultHttpClient();
        HttpRequestBase request = new HttpGet(url);
        HttpResponse resp = client.execute(request);
        String result = EntityUtils.toString(resp.getEntity());
        Logger.d("get result = " + result);
        return result;
    }

}
