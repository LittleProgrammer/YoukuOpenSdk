package com.youku.opensdk.download;

/**
 * Created by smy on 2016/3/31.
 */
public interface DownloadCallback {

    void onPreDownload();

    void doDownloading(int progress);

    void onPostDownload();

    void onFailed(Exception e);

}
