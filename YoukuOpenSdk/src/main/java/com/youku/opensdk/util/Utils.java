package com.youku.opensdk.util;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Environment;
import android.text.TextUtils;
import android.widget.Toast;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;

/**
 * Created by smy on 2016/3/31.
 */
public class Utils {

    public static void showText(Context context, int resId) {
        Toast.makeText(context, context.getText(resId), Toast.LENGTH_SHORT).show();
    }

    public static void showText(Context context, String text) {
        Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
    }

    public static String getDownloadPath(Context context) {
        if (Environment.getExternalStorageState()
                .equals(android.os.Environment.MEDIA_MOUNTED)) {
            File downloadPath = new File(Environment.getExternalStorageDirectory() + "/Download");
            if (!downloadPath.exists()) {
                downloadPath.mkdirs();
            }
            return downloadPath.getAbsolutePath();
        } else {
            return Environment.getDownloadCacheDirectory().getAbsolutePath();
        }
    }

    public static String getPackageName(Context context) {
        return context.getPackageName();
    }

    public static String getAppName(Context context) {
        try {
            PackageManager pm = context.getPackageManager();
            PackageInfo info = pm.getPackageInfo(getPackageName(context), 0);
            return info.applicationInfo.loadLabel(pm).toString();
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return "";
    }

    public static void installApplication(Context context, String absolutePath) {
        File f = new File(absolutePath);
        if (!f.exists())
            return;
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setDataAndType(Uri.fromFile(f), "application/vnd.android.package-archive");
        context.startActivity(intent);
    }

    public static boolean startAppFromPackage(Context context, String packageName) {
        if (null == context || TextUtils.isEmpty(packageName)) {
            return false;
        }
        PackageManager pm = context.getPackageManager();
        Intent intent = pm.getLaunchIntentForPackage(packageName);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
        return true;
    }

    public static String urlEncode(String str) {
        try {
            return URLEncoder.encode(str, Constants.CHARSET);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return "";
    }

    public static String urlDecode(String str) {
        try {
            return URLDecoder.decode(str, Constants.CHARSET);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return "";
    }
}
