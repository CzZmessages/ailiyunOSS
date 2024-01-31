package com.example.ossservicedemo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.app.Application;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.blankj.utilcode.util.GsonUtils;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.ThreadUtils;
import com.example.commonlibrary.Qulick.QuickClickListener;
import com.example.ossservicedemo.Bean.ApkBean;
import com.example.ossservicedemo.Bean.DataBean;
import com.example.ossservicedemo.Bean.OSSBean;
import com.example.ossservicedemo.OSS.OSSCallback;
import com.example.ossservicedemo.OSS.OSSService;
import com.example.ossservicedemo.ViewModel.DownLoadViewModel;

import utils.RegexUtils;

public class MainActivity extends AppCompatActivity {
    private ProgressBar progress_test;
    private TextView progress_size_text;
    private int mProgress;
    private Button btn_down, btn_oss, btn_apk;
    private DownLoadViewModel downLoadViewModel;
    private OSSService ossService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        downLoadViewModel = new ViewModelProvider.AndroidViewModelFactory(MyApplication.getInstance()).create(DownLoadViewModel.class);
        init();
    }

    private void init() {
        progress_test = findViewById(R.id.progress_test);
        progress_size_text = findViewById(R.id.progress_size_text);
        btn_down = findViewById(R.id.btn_down);
        btn_apk = findViewById(R.id.btn_apk);
        btn_apk.setOnClickListener(quickClickListener);
        btn_oss = findViewById(R.id.btn_oss);
        ossService = new OSSService(this);
        btn_oss.setOnClickListener(quickClickListener);
        btn_down.setOnClickListener(quickClickListener);
    }

    QuickClickListener quickClickListener = new QuickClickListener() {
        @Override
        protected void onNoDoubleClick(View v) {
            switch (v.getId()) {
                case R.id.btn_down:
                    LogUtils.e("开始下载");
                    OSSDownLoad();
                    break;
                case R.id.btn_oss:
                    requestGetAutoUrl();
                    requestOss();
                    break;
                case R.id.btn_apk:
                    getApk();
                    break;
                default:
                    break;
            }
        }
    };

    private void requestOss() {
        downLoadViewModel.mapDown().observeForever(result -> {
            if (RegexUtils.isJsonString(result)) {
                OSSBean ossBean = GsonUtils.fromJson(result, OSSBean.class);
                if (result != null) {
                    OSSBean data = ossBean.getData();
                    if (data == null) {
                        LogUtils.e("无OSS数据");
                    } else {
                        SPUtils.getInstance().put("id", data.getAccessKeyId());
                        SPUtils.getInstance().put("Scret", data.getAccessKeySecret());
                        SPUtils.getInstance().put("Token", data.getSecurityToken());
                        LogUtils.e("请求成功！");
                        LogUtils.e("ID：" + data.getAccessKeyId() + "-----Scr:" + data.getAccessKeySecret() + "---Token:" + data.getSecurityToken());
                    }
                }
            }
        });
    }

    private void requestGetAutoUrl() {
        downLoadViewModel.getDownLoadPath().observeForever(result -> {
            if (RegexUtils.isJsonString(result)) {
                DataBean dataBean = GsonUtils.fromJson(result, DataBean.class);
                if (result != null) {
                    DataBean data = dataBean.getData();
                    if (data == null) {
                        LogUtils.e("无数据返回");
                        return;
                    } else {
                        SPUtils.getInstance().put("path", data.getVersionPath());
                        LogUtils.e("下载路径:" + data.getVersionPath());
                    }
                }
            }
        });
    }

    private void getApk() {
        downLoadViewModel.getApk().observeForever(result->{
            if (RegexUtils.isJsonString(result)) {
                ApkBean apkBean=GsonUtils.fromJson(result,ApkBean.class);
                if(result!=null){
                    ApkBean data=apkBean.getData();
                    if(data==null){
                        LogUtils.e("无数据返回");
                        return;
                    }else{
                        SPUtils.getInstance().put("path",data.getFilePath());
                    }
                }
            }
        });
    }

    private void OSSDownLoad() {
        LogUtils.e("OSS开始下载");
        String id = SPUtils.getInstance().getString("id");
        String Scret = SPUtils.getInstance().getString("Scret");
        String Token = SPUtils.getInstance().getString("Token");
//        String apkPath = SPUtils.getInstance().getString("path");
        String apkPath = "output/apkWarehouse/1.1.2.8-320.apk";
//        LogUtils.e("取出数据id："+id+"/----Scr:"+Scret+"/----Token"+Token);
        ossService.prepareOSS(id, Scret, Token, new OSSCallback() {
            @Override
            public void getDownloadProgress(long _progress, long readSize) {
                ThreadUtils.runOnUiThread(() -> progress_test.setProgress((int) _progress));
                ThreadUtils.runOnUiThread(() -> progress_size_text.setText((int) _progress + "%"));
            }

            @Override
            public void ossStart() {

            }

            @Override
            public void ossnError(Throwable throwable) {

            }

            @Override
            public void ossnComplete() {
                LogUtils.e("下载完毕！");
            }
        }, "");
        ossService.download(apkPath, Constant.apkPath);
    }
}