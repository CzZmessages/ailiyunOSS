package com.example.ossservicedemo.OSS;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.ThreadUtils;
import com.google.gson.JsonSyntaxException;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.StringWriter;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.net.UnknownHostException;
import java.nio.charset.StandardCharsets;

import io.reactivex.rxjava3.core.Observable;

public class HttpUtils {

    public static Observable<String> doGet(String apiUrl) {
        return Observable.create(emitter -> {
            HttpURLConnection connection = null;
            InputStream in = null;
            BufferedReader reader = null;
            String result = "";
            try {
                URL url = new URL(apiUrl);
                LogUtils.i("---------------url---------------" + apiUrl);

                connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                connection.setConnectTimeout(15000);
                connection.setReadTimeout(15000);
                connection.connect();

                int responseCode = connection.getResponseCode();
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    in = connection.getInputStream();
                    reader = new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8));
                    StringBuilder sb = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        sb.append(line);
                    }
                    result = sb.toString();

                    LogUtils.i(result);
                    if (!emitter.isDisposed()) {
                        emitter.onNext(result);
                    }
                } else {
                    LogUtils.i("---------------onFail---------------" + "responseCode=" + responseCode + "---" + "responseMessage---" + connection.getResponseMessage());
                    if (!emitter.isDisposed()) {
                        emitter.onNext("");
                    }
                }
            } catch (Exception e) {
                if (!emitter.isDisposed()) {
                    disposeServerException(e);
                    LogUtils.e("异常接口：" + apiUrl);
                    emitter.onError(e);
                }
            } finally {
                try {
                    if (connection != null) {
                        connection.disconnect();
                    }
                    if (in != null) {
                        in.close();
                    }
                    if (reader != null) {
                        reader.close();
                    }
                } catch (Exception e) {
                    LogUtils.e(e);
                }
            }
        });
    }

    public static Observable<String> doPost(String apiUrl, String paramsJsonString) {
        return Observable.create(emitter -> {
            HttpURLConnection connection = null;
            OutputStream out = null;
            InputStream in = null;
            String result = "";
            try {
                URL url = new URL(apiUrl);
                LogUtils.i("---------------url---------------" + apiUrl + "---------------params---------------" + paramsJsonString);
                connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("POST");
                connection.setConnectTimeout(15000);
                connection.setReadTimeout(15000);
                connection.setRequestProperty("Content-Type", "application/json;charset=utf-8");
                connection.setRequestProperty("Accept", "application/json;charset=utf-8");
                connection.setDoInput(true);
                connection.setDoOutput(true);
                connection.setUseCaches(false);
                out = connection.getOutputStream();
                out.write(paramsJsonString.getBytes(StandardCharsets.UTF_8));
                out.flush();
                connection.connect();
                int responseCode = connection.getResponseCode();
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    in = connection.getInputStream();
                    StringWriter sw = new StringWriter();
                    InputStreamReader reader = new InputStreamReader(in, StandardCharsets.UTF_8);
                    char[] buffer = new char[4096];
                    for (int i; (i = reader.read(buffer)) != -1; ) {
                        sw.write(buffer, 0, i);
                    }
                    result = sw.toString();

                    LogUtils.i(result);
                    if (!emitter.isDisposed()) {
                        emitter.onNext(result);
                    }
                } else {
                    LogUtils.i("---------------onFail---------------" + "responseCode=" + responseCode + "---" + "responseMessage---" + connection.getResponseMessage());
                    if (!emitter.isDisposed()) {
                        emitter.onNext("");
                    }
                }
            } catch (Exception e) {
                if (!emitter.isDisposed()) {
                    LogUtils.e(String.format("下载异常 %s", e.getMessage()));
                    disposeServerException(e);
                    LogUtils.e("异常接口：" + apiUrl);
                    emitter.onError(e);
                }
            } finally {
                try {
                    if (connection != null) {
                        connection.disconnect();
                    }
                    if (out != null) {
                        out.close();
                    }
                    if (in != null) {
                        in.close();
                    }
                } catch (Exception e) {
                    LogUtils.e(e);
                }
            }
        });
    }
    protected static void disposeServerException(Exception e) {
        LogUtils.e(e);
        ThreadUtils.runOnUiThread(() -> {
            if (e instanceof UnknownHostException) {
                LogUtils.e("请确认您的手机网络是否可用");
            } else if (e instanceof ConnectException) {
//                CommonUtils.showLong("网络连接失败，请检查您的网络设置");
            } else if (e instanceof SocketTimeoutException) {
                LogUtils.e("加载超时，请重试");
            } else if (e instanceof JsonSyntaxException) {
                LogUtils.e("json数据格式异常");
            } else {
                LogUtils.e("服务器异常");
            }
        });
    }


}
