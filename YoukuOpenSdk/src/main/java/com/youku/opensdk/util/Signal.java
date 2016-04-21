package com.youku.opensdk.util;

/**
 * Created by smy on 2016/4/21.
 */
public class Signal {
    static {
        System.loadLibrary("OpenSDk");
    }

    private native String getSignWithMd5(String jsonParams, String secret);

    private native String getSignWithHmacSHA256(String jsonParams, String secret);
}
