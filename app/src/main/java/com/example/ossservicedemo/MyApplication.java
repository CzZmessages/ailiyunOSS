package com.example.ossservicedemo;

import android.app.Application;
import android.content.Context;

public class MyApplication extends Application {
    private static Context mContext;
    private static MyApplication myApplication;
    public static MyApplication getInstance(){
        return myApplication;
    }
    @Override
    public void onCreate() {
        super.onCreate();
        mContext=this;
        myApplication=this;
    }
    public static  Context getmContext(){
        return mContext;
    }
}
