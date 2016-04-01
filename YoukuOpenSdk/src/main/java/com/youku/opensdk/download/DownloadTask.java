package com.youku.opensdk.download;

import android.content.Context;

import com.youku.opensdk.util.Utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by smy on 2016/3/31.
 */
public class DownloadTask implements Runnable {

    private final int TIMEOUT = 6 * 1000;
    private final int BUFFER_SIZE = 2 * 1024;
    private final String SUFFiX = ".tmp";


    private final Context mContext;
    private String mFileName;
    private String mUrl;
    private DownloadCallback mCallback;

    DownloadTask(Context context, String url, String fileName, DownloadCallback callback) {
        mContext = context;
        mUrl = url;
        mFileName = fileName;
        mCallback = callback;
    }

    public String getFileName() {
        return mFileName;
    }

    public void setFileName(String fileName) {
        mFileName = fileName;
    }

    public String getUrl() {
        return mUrl;
    }

    public void setUrl(String url) {
        mUrl = url;
    }

    public DownloadCallback getCallback() {
        return mCallback;
    }

    public void setCallback(DownloadCallback callback) {
        mCallback = callback;
    }

    @Override
    public void run() {
        if (null != mCallback) {
            mCallback.onPreDownload();
        }
        InputStream is = null;
        FileOutputStream fos = null;
        try {
            URL url = new URL(mUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setConnectTimeout(TIMEOUT);
            conn.setReadTimeout(TIMEOUT);
            long total = conn.getContentLength();
            is = conn.getInputStream();
            String downloadPath = Utils.getDownloadPath(mContext);
            File f = new File(downloadPath + File.separator + mFileName + SUFFiX);
            fos = new FileOutputStream(f);
            byte[] buf = new byte[BUFFER_SIZE];
            int len = 0;
            long sum = 0;
            while ((len = is.read(buf)) > 0) {
                fos.write(buf, 0, len);
                sum += len;
                if (null != mCallback) {
                    mCallback.doDownloading((int) (sum * 100 / total));
                }
            }
            fos.flush();
            if (f.renameTo(new File(downloadPath + File.separator + mFileName))) {
                Utils.installApplication(mContext, downloadPath + File.separator + mFileName);
            }
            if (null != mCallback) {
                mCallback.onPostDownload();
            }
        } catch (MalformedURLException e) {
            if (null != mCallback) {
                mCallback.onFailed(e);
            }
            e.printStackTrace();
        } catch (IOException e) {
            if (null != mCallback) {
                mCallback.onFailed(e);
            }
            e.printStackTrace();
        } finally {
            try {
                if (null != is) {
                    is.close();
                }
                if (null != fos) {
                    fos.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
