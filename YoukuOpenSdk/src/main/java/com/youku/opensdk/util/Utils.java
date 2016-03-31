package com.youku.opensdk.util;

import android.content.Context;
import android.os.Environment;
import android.widget.Toast;

import java.io.File;

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
}
