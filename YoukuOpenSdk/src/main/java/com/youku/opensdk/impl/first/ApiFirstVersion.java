package com.youku.opensdk.impl.first;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;

import com.youku.opensdk.YoukuOpenAPI;
import com.youku.opensdk.download.DownloadCallback;
import com.youku.opensdk.download.DownloadServer;
import com.youku.opensdk.util.Constants;
import com.youku.opensdk.util.Logger;
import com.youku.opensdk.util.Utils;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by smy on 2016/3/30.
 */
public class ApiFirstVersion implements YoukuOpenAPI {

    private final Context mContext;
    private String mYoukuPackageName;
    private int mYoukuAppVersion;
    private int mYoukuOpenApiVersion;

    ApiFirstVersion(Context context, String appKey) {
        mContext = context;
        queryYoukuApp(mContext);
    }

    @Override
    public boolean hasYoukuApp() {
        if (mYoukuAppVersion < 0 || TextUtils.isEmpty(mYoukuPackageName)) {
            return false;
        }
        return true;
    }

    @Override
    public boolean isSupportShare() {
        if (mYoukuOpenApiVersion > 0) {
            return true;
        }
        return false;
    }

    @Override
    public int getVersion() {
        return Constants.OPEN_SDK_VERSION;
    }

    @Override
    public String getYoukuAppDownloadUrl() {
        return null;
    }


    @Override
    public boolean share(Context context, Bundle params) {
        if (!hasYoukuApp()) {
            Logger.d("haven`t install youku app !");
            return false;
        }
        if (!isSupportShare()) {
            Logger.d("youku app doesn`t support share api !");
            return false;
        }
        if (null == context) {
            Logger.d("context is null !");
            return false;
        }
        if (null == params || params.size() <= 0) {
            Logger.d("params is empty !");
            return false;
        }
        if (!params.containsKey("sdk_version")) {
            params.putInt("sdk_version", Constants.OPEN_SDK_VERSION);
        }
        if (!params.containsKey("caller_package_name")) {
            params.putString("caller_package_name", Utils.getPackageName(mContext));
        }
        if (!params.containsKey("caller_app_name")) {
            params.putString("caller_app_name", Utils.getAppName(mContext));
        }
        Intent intent = new Intent();
        intent.setAction(Constants.OPEN_API_ACTION_UPLOAD);
        intent.setPackage(mYoukuPackageName);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtras(params);
        mContext.startActivity(intent);
        return true;
    }

    private void queryYoukuApp(Context context) {
        Cursor c = context.getContentResolver().query(Uri.parse(Constants.OPEN_SDK_PROVIDER_URI), null, null, null, null);
        if (null == c || c.getCount() <= 0) {
            mYoukuAppVersion = -1;
            mYoukuPackageName = null;
            mYoukuOpenApiVersion = -1;
        } else {
            c.moveToFirst();
            mYoukuAppVersion = c.getInt(c.getColumnIndex("app_version"));
            mYoukuPackageName = c.getString(c.getColumnIndex("package_name"));
            mYoukuOpenApiVersion = c.getInt(c.getColumnIndex("api_version"));
        }
        if (null != c && !c.isClosed())
            c.close();
    }

    @Override
    public void downloadYoukuApp(final DownloadCallback callback) {
        DownloadServer server = DownloadServer.getInstance(mContext);
        IntentFilter filter = new IntentFilter(Intent.ACTION_PACKAGE_ADDED);
        mContext.registerReceiver(mReceiver, filter);
        server.download(Constants.YOUKU_APP_DOWNLOAD_URL, "youku.apk", new DownloadCallback() {
            @Override
            public void onPreDownload() {
                if (null != callback) {
                    callback.onPreDownload();
                }
            }

            @Override
            public void doDownloading(int progress) {
                if (null != callback) {
                    callback.doDownloading(progress);
                }
            }

            @Override
            public void onPostDownload() {
                mContext.unregisterReceiver(mReceiver);
                if (null != callback) {
                    callback.onPostDownload();
                }
            }

            @Override
            public void onFailed(Exception e) {
                mContext.unregisterReceiver(mReceiver);
                if (null != callback) {
                    callback.onFailed(e);
                }
            }
        });
    }

    private BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String data = intent.getDataString();
            Logger.d("receiver action = " + intent.getAction() + ", data = " + data);
            String packageName = data.substring(8);
            if (null != packageName && packageName.contains("youku")) {
                Utils.startAppFromPackage(mContext, packageName);
            }
        }
    };

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
        mContext.unregisterReceiver(mReceiver);
    }
}
