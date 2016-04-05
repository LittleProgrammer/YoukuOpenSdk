package com.youku.opensdk.demo;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.youku.opensdk.YoukuAPIAuthCallback;
import com.youku.opensdk.YoukuAPIFactory;
import com.youku.opensdk.YoukuOpenAPI;
import com.youku.opensdk.download.DownloadCallback;
import com.youku.opensdk.util.Logger;
import com.youku.opensdk.util.Utils;

import java.io.File;


public class MainActivity extends AppCompatActivity {

    private final String TAG = "YoukuOpenSdkDemo";
    private YoukuOpenAPI mYoukuOpenAPI;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mYoukuOpenAPI = YoukuAPIFactory.createYoukuApi(this);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();

//                Utils.installApplication(MainActivity.this, "/mnt/sdcard/Download/youku.apk");

                mYoukuOpenAPI.authorize(Constants.TEST_APPKEY, Constants.TEST_SECRET, new YoukuAPIAuthCallback() {
                    @Override
                    public void success() {
                        Log.d(TAG, "auth success !!!");
                        share();
                    }

                    @Override
                    public void failed() {
                        Log.d(TAG, "auth failed !!!");
                    }
                });
            }
        });
    }

    // 调用分享接口
    public void share() {
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
        bundle.putString("file_path", "file:///mnt/sdcard/720p.mp4");
        bundle.putString("title", "优酷上传测试");
        bundle.putString("description", "优酷上传测试");
        bundle.putString("topicName", "优酷上传测试");
        if (mYoukuOpenAPI.hasYoukuApp()) {
                    /*  调用分享接口  */
            mYoukuOpenAPI.share(MainActivity.this, bundle);
        } else {
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

                }

                @Override
                public void onFailed(Exception e) {

                }
            });
        }
    }

//    private void registerReceiver() {
//        IntentFilter filter = new IntentFilter(Intent.ACTION_PACKAGE_ADDED);
//        filter.addDataScheme("package");
//        registerReceiver(mReceiver, filter);
//    }

//    private BroadcastReceiver mReceiver = new BroadcastReceiver() {
//        @Override
//        public void onReceive(Context context, Intent intent) {
//            String data = intent.getDataString();
//            Logger.d("receiver action = " + intent.getAction() + ", data = " + data);
//            String packageName = data.substring(8);
//            if (null != packageName && packageName.contains("youku")) {
//                Utils.startAppFromPackage(MainActivity.this, packageName);
//            }
//        }
//    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        unregisterReceiver(mReceiver);
    }
}
