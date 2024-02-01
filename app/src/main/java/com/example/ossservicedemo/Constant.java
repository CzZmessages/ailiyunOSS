package com.example.ossservicedemo;

import android.os.Environment;

public class Constant {
    public static final String bas_Url="http://8.140.62.62:8001/";
    public static final String apkPath= Environment.getExternalStorageDirectory().getAbsolutePath()+"/Demo.apk";
    public static final String apkOutPath_weixin=Environment.getExternalStorageDirectory().getAbsolutePath()+"/weixin.apk";
    public static final String apkOutPath_lebo=Environment.getExternalStorageDirectory().getAbsolutePath()+"/lebo.apk";
    public static final String apkOutPath_gaode=Environment.getExternalStorageDirectory().getAbsolutePath()+"/gaode.apk";
    public static final String outPath=Environment.getExternalStorageDirectory().getAbsolutePath();
}
