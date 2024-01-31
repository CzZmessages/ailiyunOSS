package com.example.ossservicedemo.OSS;

import android.content.Context;

import com.alibaba.sdk.android.oss.ClientConfiguration;
import com.alibaba.sdk.android.oss.ClientException;
import com.alibaba.sdk.android.oss.OSSClient;
import com.alibaba.sdk.android.oss.ServiceException;
import com.alibaba.sdk.android.oss.callback.OSSCompletedCallback;
import com.alibaba.sdk.android.oss.common.OSSLog;
import com.alibaba.sdk.android.oss.common.auth.OSSCredentialProvider;
import com.alibaba.sdk.android.oss.common.auth.OSSStsTokenCredentialProvider;
import com.alibaba.sdk.android.oss.model.GetObjectRequest;
import com.alibaba.sdk.android.oss.model.GetObjectResult;
import com.blankj.utilcode.util.LogUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;

public class OSSService {
    private OSSClient oss_;
    private String bucket_name_;
    ///< 宋涛 2023/9/6 15:08yourEndpoint填写Bucket所在地域对应的Endpoint。以华东1（杭州）为例，Endpoint填写为https://oss-cn-hangzhou.aliyuncs.com。
    private String endpoint_ = "oss-cn-hangzhou.aliyuncs.com";
    private String accessKeyId_;
    ///< 宋涛 2023/9/6 15:08从STS服务获取的临时访问密钥（AccessKey ID和AccessKey Secret）。
    private String accessKeySecret_;
    ///< 宋涛 2023/9/6 15:08从STS服务获取的安全令牌（SecurityToken）。
    private String securityToken_;
    private Context context_;
    private OSSCallback callback_;
    public OSSService(Context _context) {
        context_ = _context;
    }
    public void prepareOSS(String _id, String _secret, String _token,OSSCallback _callback, String _bucket_name) {
        accessKeyId_ = _id;
        accessKeySecret_ = _secret;
        securityToken_ = _token;
        callback_=_callback;
        if (_bucket_name == "")
            bucket_name_ = "iyuepu-app-gy";
        else
            bucket_name_ = _bucket_name;
    }

    public void download(String _oss_obj_url, String _out_path) {
        LogUtils.e("初始化"+_out_path);
        initOSS();
        downloadOSSObjectPrivate(_oss_obj_url, _out_path);
    }

 //c初始化
    private void initOSS() {
        OSSCredentialProvider credentialProvider = new OSSStsTokenCredentialProvider(accessKeyId_,
                accessKeySecret_, securityToken_);
        oss_ = new OSSClient(context_, endpoint_, credentialProvider);
    }

   //下载
    private void downloadOSSObjectPrivate(String _oss_obj_url, String _out_path) {
        GetObjectRequest get = new GetObjectRequest(bucket_name_, _oss_obj_url);

        callback_.ossStart();
        oss_.asyncGetObject(get, new OSSCompletedCallback<GetObjectRequest, GetObjectResult>() {
            @Override
            public void onSuccess(GetObjectRequest request, GetObjectResult result) {
                try{
                    LogUtils.e("请求成功开始下载");
                    File file=new File(_out_path);
                    File parentFile=file.getParentFile();
                    if(!parentFile.exists()){
                        parentFile.mkdirs();
                    }
                    if(!file.exists()){
                        file.createNewFile();
                    }

                    long length = result.getContentLength();
                    long download_progress = 0;

                    InputStream inputStream = result.getObjectContent();
                    OutputStream outputStream = new FileOutputStream(_out_path);

                    byte[] buffer = new byte[1024 * 1024]; // 使用更合理的缓冲区大小
                    int readCount;

                    while ((readCount = inputStream.read(buffer)) != -1) {
                        outputStream.write(buffer, 0, readCount);
                        download_progress += readCount;
                        callback_.getDownloadProgress((int)((download_progress*1.0)/(length*1.0)*100), readCount);
                    }

                    outputStream.close();
                    inputStream.close();

                    if(download_progress == length){
                        callback_.ossnComplete();
                    } else {
                        LogUtils.e("下载失败！");
                    }
                } catch (Exception e) {
                    callback_.ossnError(e);
                    OSSLog.logInfo(e.toString());
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(GetObjectRequest request, ClientException clientException, ServiceException serviceException) {
                LogUtils.e("GetObjectRequest"+request+"/ClientException"+clientException+"ServiceException"+serviceException);
            }

            // ... 其他重写的方法如onFailure等...
        });

    }
}
