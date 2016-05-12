package com.youku.opensdk.demo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.youku.opensdk.YoukuAPIAuthCallback;
import com.youku.opensdk.YoukuAPIFactory;
import com.youku.opensdk.YoukuOpenAPI;
import com.youku.opensdk.download.DownloadCallback;


public class MainActivity extends Activity implements View.OnClickListener {

    private final String TAG = "YoukuOpenSdkDemo";

    private EditText mVideoAddressView;

    private YoukuOpenAPI mYoukuOpenAPI;
    private final int REQUEST_CODE = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mVideoAddressView = (EditText) findViewById(R.id.video_address_view);

        findViewById(R.id.auth).setOnClickListener(this);
        findViewById(R.id.share).setOnClickListener(this);
        findViewById(R.id.download).setOnClickListener(this);

        // 创建YoukuOpenAPI实例
        mYoukuOpenAPI = YoukuAPIFactory.createYoukuApi(this);
    }

    //授权接口
    private void auth() {
        mYoukuOpenAPI.authorize(Constants.TEST_APPKEY, Constants.TEST_SECRET, new YoukuAPIAuthCallback() {
            @Override
            public void success() {
                Log.d(TAG, "auth success !!!");
                showText("获取授权成功！");
            }

            @Override
            public void failed() {
                Log.d(TAG, "auth failed !!!");
                showText("获取授权失败！");
            }
        });
    }

    // 调用分享接口
    private void share() {
        String videoAddr = mVideoAddressView.getText().toString();
        videoAddr = "file:///mnt/sdcard/Download/720p.mp4";
//        if (TextUtils.isEmpty(videoAddr)) {
//            showText("请输入视频文件地址。。。");
//            return;
//        }
        final Bundle bundle = new Bundle();
        /*
         * 第三方应用需要传递一些参数加入params里（key, value）形式，如下：
         * result_action            回调第三方应用时的anction(String,必须)
         * file_path                视频文件绝对路径(String, 必须 现阶段支持file:// ,content:// 协议文件路径)
         * title                    视频标题(String,必须)
         * description              视频说明(String,必须)
         * topicName               带的话题(String,非必须)
         */
        bundle.putString("result_action", "com.youku.opensdk.DEMO");
        bundle.putString("file_path", videoAddr);
        bundle.putString("title", "优酷上传测试");
        bundle.putString("description", "优酷上传测试");
        bundle.putString("topicName", "优酷上传测试");
        if (mYoukuOpenAPI.hasYoukuApp()) {

            /*  两种分享接口调用方式  */
            /*
            if (!mYoukuOpenAPI.share(MainActivity.this, bundle)) {
                showText("请先获取授权！");
            }
            */
            /* 基于startActivityForResult的方法，所以需要传递activity，并且参数bundle
            *  中不再需要加入result_action，并需要该activity重写onActivityResult方法*/
            if (!mYoukuOpenAPI.share(MainActivity.this, REQUEST_CODE, bundle)) {
                showText("请先获取授权！");
            }
        }
    }

    private void downloadYoukuApp() {
        mYoukuOpenAPI.downloadYoukuApp(new DownloadCallback() {
            @Override
            public void onPreDownload() {

            }

            @Override
            public void doDownloading(int progress) {
                Log.d(TAG, "download youku app progress = " + progress);
            }

            @Override
            public void onPostDownload() {
                showText("下载成功！");
            }

            @Override
            public void onFailed(Exception e) {
                showText("下载失败，请检查后重试！");
            }
        });
    }

    private void showText(final String text) {
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(MainActivity.this, text, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.auth:
                auth();
                break;
            case R.id.share:
                share();
                break;
            case R.id.download:
                downloadYoukuApp();
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQUEST_CODE:
                Log.d("TestOpenSdk", "resultCode = " + resultCode + "\ntest open sdk intent = " + data);
                break;
        }
    }
}
