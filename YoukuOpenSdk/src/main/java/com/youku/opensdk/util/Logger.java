package com.youku.opensdk.util;

import android.util.Log;

/**
 * Created by smy on 16-1-6.
 */
public class Logger {

    private Logger() {

    }

    public static final String TAG = "YoukuOpenSdk";

    private static boolean sDebug = false;

    public static void setDebugEnabled(boolean debug) {
        sDebug = debug;
    }

    public static boolean isDebugEnabled() {
        return sDebug;
    }

    public static void v(String msg) {
        v(TAG, msg);
    }

    public static void v(String tag, String msg) {
        if (sDebug)
            Log.v(tag, msg);
    }

    public static void d(String msg) {
        d(TAG, msg);
    }

    public static void d(String tag, String msg) {
        if (sDebug)
            Log.d(tag, msg);
    }

    public static void i(String msg) {
        i(TAG, msg);
    }

    public static void i(String tag, String msg) {
        if (sDebug)
            Log.i(tag, msg);
    }

    public static void e(String msg) {
        e(TAG, msg);
    }

    public static void e(String tag, String msg) {
        if (sDebug)
            Log.e(tag, msg);
    }

    public static void e(String tag, boolean debug, String msg, Exception e) {
        if (sDebug && debug)
            Log.e(tag, msg, e);
    }

}
