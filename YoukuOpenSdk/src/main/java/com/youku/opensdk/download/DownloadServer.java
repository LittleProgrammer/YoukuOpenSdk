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
import java.util.Objects;
import java.util.Queue;
import java.util.concurrent.Callable;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by smy on 2016/3/31.
 */
public class DownloadServer {

    private static DownloadServer sServer;

    private final Context mContext;

    private ExecutorService mExecutorService;

    private DownloadServer(Context context) {
        mContext = context;
        mExecutorService = Executors.newSingleThreadExecutor();
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
        DownloadTask task = new DownloadTask(mContext, url, appName, callback);
        mExecutorService.execute(task);
    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
        mExecutorService.shutdown();
    }
}
