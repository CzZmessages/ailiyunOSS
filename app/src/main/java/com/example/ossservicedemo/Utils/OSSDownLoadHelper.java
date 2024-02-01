package com.example.ossservicedemo.Utils;

import android.content.Context;
import android.util.Log;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.SPUtils;
import com.example.ossservicedemo.Constant;
import com.example.ossservicedemo.OSS.OSSCallback;
import com.example.ossservicedemo.OSS.OSSService;

public class OSSDownLoadHelper {
    public void OSSDownLoadHelper(Context context, String apkPath, String appName, String outPath) {
        OSSService oss = new OSSService(context);
        String id = SPUtils.getInstance().getString("id");
        String Scret = SPUtils.getInstance().getString("Scret");
        String Token = SPUtils.getInstance().getString("Token");
        oss.prepareOSS(id, Scret, Token, new OSSCallback() {
            @Override
            public void getDownloadProgress(long _progress, long readSize) {
                Log.e("下载量", "进度" + _progress);
            }

            @Override
            public void ossStart() {

            }

            @Override
            public void ossnError(Throwable throwable) {

            }

            @Override
            public void ossnComplete() {
                LogUtils.e(appName + "下载完成！");
            }
        }, "");
        oss.download(apkPath, outPath);
    }
}
