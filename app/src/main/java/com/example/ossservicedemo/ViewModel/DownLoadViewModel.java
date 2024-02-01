package com.example.ossservicedemo.ViewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.ossservicedemo.Constant;
import com.example.ossservicedemo.OSS.HttpUtils;
import com.google.gson.JsonObject;

import org.jetbrains.annotations.NotNull;

public class DownLoadViewModel extends BaseViewModel {
    public DownLoadViewModel(@NonNull @NotNull Application application) {
        super(application);
    }
    //请求服务端
    public LiveData<String> getDownLoadPath(){
        final MutableLiveData<String> downloadLiveAble=new MutableLiveData<>();
        JsonObject jsonObject=new JsonObject();
        jsonObject.addProperty("auserType","1");
        add(execute(HttpUtils.doPost(Constant.bas_Url+"tmversion/download",jsonObject.toString()),
        resultBean->downloadLiveAble.setValue(String.valueOf(resultBean)),
                throwable->downloadLiveAble.setValue("")));
        return downloadLiveAble;
    }
    //请求令牌
    public LiveData<String> mapDown(){
        final MutableLiveData<String> ossMessgae=new MutableLiveData<>();
        JsonObject jsonObject=new JsonObject();
        add(execute(HttpUtils.doPost(Constant.bas_Url+"oss/token",jsonObject.toString()),
                resultBean->ossMessgae.setValue(String.valueOf(resultBean)),
                throwable->ossMessgae.setValue("")));
        return ossMessgae;
    }
    //微信接口
    public LiveData<String> getApk(String apkName){
        final MutableLiveData<String> apkData=new MutableLiveData<>();
        JsonObject jsonObject=new JsonObject();
        jsonObject.addProperty("appName",apkName);
        add(execute(HttpUtils.doPost(Constant.bas_Url+"totherapp/app",jsonObject.toString()),
                resultBean->apkData.setValue(String.valueOf(resultBean)),
                throwable->apkData.setValue("")));
        return apkData;
    }
}
