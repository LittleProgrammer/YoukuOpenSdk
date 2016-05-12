package com.youku.opensdk.impl.first;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;

import com.youku.opensdk.YoukuAPIAuthCallback;
import com.youku.opensdk.YoukuOpenAPI;
import com.youku.opensdk.download.DownloadCallback;
import com.youku.opensdk.download.DownloadServer;
import com.youku.opensdk.util.Constants;
import com.youku.opensdk.util.HttpUtils;
import com.youku.opensdk.util.Logger;
import com.youku.opensdk.util.SignalUtils;
import com.youku.opensdk.util.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.SortedMap;
import java.util.TreeMap;

/**
 * Created by smy on 2016/3/30.
 */
public class ApiFirstVersion implements YoukuOpenAPI {

    private final Context mContext;
    private String mYoukuPackageName;
    private int mYoukuAppVersion;
    private int mYoukuOpenApiVersion;

    private boolean mShareEnabled;

    ApiFirstVersion(Context context) {
        mContext = context;
        if (queryYoukuAppFromPM(mContext)) {
            startYoukuEmptyActivity(mContext);
        }
        queryYoukuApp(mContext);
    }

    @Override
    public void authorize(final String appKey, final String secretKey, final YoukuAPIAuthCallback callback) {
        if (!hasYoukuApp()) {
            return;
        }
        if (TextUtils.isEmpty(appKey)) return;
        if (TextUtils.isEmpty(secretKey)) return;
        if (null == callback) return;
        new Thread() {
            @Override
            public void run() {
                long curTime = System.currentTimeMillis() / 1000;
                SortedMap<String, String> map = new TreeMap<>();
                String uri = HttpUtils.URI_GET_APP_AUTHORIZE;
                map.put("action", uri);
                map.put("client_id", appKey);
                map.put("timestamp", curTime + "");
                map.put("version", "3.0");
                map.put("clientid", appKey);
                try {
                    JSONObject json = SignalUtils.signal(map, secretKey);
                    String url = HttpUtils.OPEN_API_REST_JSON + "?" + "opensysparams=" +
                            Utils.urlEncode(json.toString()) + "&clientid=" + appKey;
                    String result = HttpUtils.get(url);
                    if (TextUtils.isEmpty(result)) {
                        mShareEnabled = false;
                        callback.failed();
                        return;
                    }
                    JSONObject resJson = new JSONObject(result);
                    JSONArray array = resJson.optJSONArray("rtn_data");
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject obj = array.optJSONObject(i);
                        String authCode = obj.optString("gid");
                        if (Constants.OPEN_SDK_SHARE_CODE.equals(authCode)) {
                            mShareEnabled = true;
                            callback.success();
                            return;
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    mShareEnabled = false;
                    callback.failed();
                } catch (IOException e) {
                    e.printStackTrace();
                    mShareEnabled = false;
                    callback.failed();
                }
            }

        }.start();
    }

    @Override
    public boolean hasYoukuApp() {
        queryYoukuApp(mContext);
        if (mYoukuAppVersion < 0 || TextUtils.isEmpty(mYoukuPackageName)) {
            return false;
        }
        return true;
    }

    @Override
    public boolean isSupportShare() {
        if (mYoukuOpenApiVersion > 1) {
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
        if (null == context) {
            return false;
        }
        if (!mShareEnabled) {
            Logger.d("before share you must auth your app !");
            return false;
        }
        if (!hasYoukuApp()) {
            Logger.d("haven`t install youku app !");
            return false;
        }
        if (!isSupportShare()) {
            Logger.d("youku app doesn`t support share api !");
            return false;
        }
        if (null == params || params.size() <= 0) {
            Logger.d("params is empty !");
            return false;
        }
        buildParams(params);
        Intent intent = new Intent();
        intent.setAction(Constants.OPEN_API_ACTION_UPLOAD);
        intent.setPackage(mYoukuPackageName);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtras(params);
        mContext.startActivity(intent);
        return true;
    }

    @Override
    public boolean share(Activity a, int requestCode, Bundle params) {
        if (null == a) {
            return false;
        }
        if (!mShareEnabled) {
            Logger.d("before share you must auth your app !");
            return false;
        }
        if (!hasYoukuApp()) {
            Logger.d("haven`t install youku app !");
            return false;
        }
        if (!isSupportShare()) {
            Logger.d("youku app doesn`t support share api !");
            return false;
        }
        if (null == params || params.size() <= 0) {
            Logger.d("params is empty !");
            return false;
        }
        buildParams(params);
        Intent intent = new Intent();
        intent.setAction(Constants.OPEN_API_ACTION_UPLOAD);
        intent.setPackage(mYoukuPackageName);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtras(params);
        a.startActivityForResult(intent, requestCode);
        return true;
    }

    private void buildParams(Bundle params) {
        if (!params.containsKey("sdk_version")) {
            params.putInt("sdk_version", Constants.OPEN_SDK_VERSION);
        }
        if (!params.containsKey("caller_package_name")) {
            params.putString("caller_package_name", Utils.getPackageName(mContext));
        }
        if (!params.containsKey("caller_app_name")) {
            params.putString("caller_app_name", Utils.getAppName(mContext));
        }
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

    private boolean queryYoukuAppFromPM(Context context) {

        return Utils.isPackageExist(context, Constants.YOUKU_APP_PACKAGE);
    }

    private void startYoukuEmptyActivity(Context context) {
        Intent intent = new Intent();
        intent.setClassName("com.youku.phone", "com.youku.service.push.EmptyActivity");
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    @Override
    public void downloadYoukuApp(final DownloadCallback callback) {
        DownloadServer server = DownloadServer.getInstance(mContext);
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
                registerReceiver();
                if (null != callback) {
                    callback.onPostDownload();
                }
            }

            @Override
            public void onFailed(Exception e) {
                if (null != callback) {
                    callback.onFailed(e);
                }
            }
        });
    }

    public void registerReceiver() {
        Logger.d("register receiver !!!");
        final IntentFilter filter = new IntentFilter(Intent.ACTION_PACKAGE_ADDED);
        filter.addDataScheme("package");
        mContext.registerReceiver(mReceiver, filter);
    }

    private BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String data = intent.getDataString();
            Logger.d("receiver action = " + intent.getAction() + ", data = " + data);
            String packageName = data.substring(8);
            if (null != packageName && packageName.contains("youku")) {
                Utils.startAppFromPackage(mContext, packageName);
                mContext.unregisterReceiver(mReceiver);
            }
        }
    };

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
        Logger.d("ApiFirstVersion is finalized !!!");
    }
}
