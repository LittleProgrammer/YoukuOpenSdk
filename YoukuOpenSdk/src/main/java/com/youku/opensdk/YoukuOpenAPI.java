package com.youku.opensdk;

import android.content.Context;
import android.os.Bundle;

/**
 * Created by smy on 2016/3/30.
 */
public interface YoukuOpenAPI {

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
     * 获取优酷App下载地址
     * */
    String getYoukuAppDownloadUrl();

    /**
     * 第三方应用需要传递一些参数加入params里（key, value）形式，如下：
     * caller_package_name      第三方应用报名(String,必须)
     * result_action            回调第三方应用时的anction(String,必须)
     * caller_app_name          第三方应用名字(String,必须)
     * sdk_version              使用opensdk的版本(int,必须)
     * file_path                视频文件绝对路径(String, 必须 现阶段支持file:// ,content:// 协议文件路径)
     * title                    视频标题(String,必须)
     * description              视频说明(String,必须)
     * topicName                带的话题(String,非必须)
     */
    boolean share(Context context, Bundle params);

    void downloadYoukuApp();
}
