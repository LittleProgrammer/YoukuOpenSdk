package com.youku.opensdk;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;

import com.youku.opensdk.download.DownloadCallback;

/**
 * Created by smy on 2016/3/30.
 */
public interface YoukuOpenAPI {

    void authorize(String appKey, String secretKey, YoukuAPIAuthCallback callback);

    /**
     * 判断是否安装YoukuApp
     * */
    boolean hasYoukuApp();

    /**
     * 判断YoukuApp版本是否支持分享
     * */
    boolean isSupportShare();

    /**
     * 获取当前API版本
     * */
    int getVersion();

    /**
     * 第三方应用需要传递一些参数加入params里（key, value）形式，如下：
     * result_action            回调第三方应用时的anction(String,必须)
     * file_path                视频文件绝对路径(String, 必须 现阶段支持file:// ,content:// 协议文件路径)
     * title                    视频标题(String,必须)
     * description              视频说明(String,必须)
     * topicName                带的话题(String,非必须)
     */
    boolean
    share(Context context, Bundle params);

    /**
     * 功能同上
     * @param a 需要分享的Activity
     * @param requestCode 基于startActivityForResult的requestCode
     * @param params 同上，只是不必添加result_action参数
     * @return 是否成功
     */
    boolean share(Activity a,  int requestCode, Bundle params);
    /**
     * 获取优酷App下载地址
     * */
    String getYoukuAppDownloadUrl();

    void downloadYoukuApp(DownloadCallback callback);
}
