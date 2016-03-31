package com.youku.opensdk.impl.first;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;

import com.youku.opensdk.YoukuOpenAPI;
import com.youku.opensdk.util.Constants;
import com.youku.opensdk.util.Logger;

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
    public void downloadYoukuApp() {
        try {
            URL url = new URL(Constants.YOUKU_APP_DOWNLOAD_URL);

        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }
}
