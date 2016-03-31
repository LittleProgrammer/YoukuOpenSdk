package com.youku.opensdk.download;

import android.content.Context;
import android.text.TextUtils;

import com.youku.opensdk.util.Logger;

import org.w3c.dom.Text;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.LinkedList;
import java.util.Queue;

/**
 * Created by smy on 2016/3/31.
 */
public class DownloadServer {

    private static DownloadServer sServer;

    private final Context mContext;
    private DownloadCallback mDownloadCallback;

    private String mUrl;
    private String mAppName;

    private Queue<DownloadTask> mQueue = new LinkedList<>();
    private DownloadThread mDownloadThread;

    private final int TIMEOUT = 6 * 1000;

    private DownloadServer(Context context) {
        mContext = context;
    }

    public static synchronized DownloadServer getInstance(Context context) {
        if (null == sServer) {
            sServer = new DownloadServer(context);
        }
        return sServer;
    }

    public void download(String url, String appName, DownloadCallback callback) {
        if (TextUtils.isEmpty(url)) {
            Logger.d("url is empty !");
            return;
        }
        if (TextUtils.isEmpty(appName)) {
            Logger.d("appName is empty !");
            return;
        }
        mDownloadCallback = callback;
        mUrl = url;
        mAppName = appName;
    }

    private class DownloadThread extends Thread {

        private final String mUrl;
        private final String mFileName;

        public DownloadThread(String url, String fileName) {
            mUrl = url;
            mFileName = fileName;
        }

        @Override
        public void run() {
            FileInputStream fis = null;
            try {
                URL url = new URL(mUrl);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setConnectTimeout(TIMEOUT);
                conn.setReadTimeout(TIMEOUT);
                InputStream is = conn.getInputStream();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {

            }
        }
    }


}
