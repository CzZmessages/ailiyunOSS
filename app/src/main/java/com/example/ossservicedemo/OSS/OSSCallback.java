package com.example.ossservicedemo.OSS;

public interface OSSCallback {
    /**
     * @author 宋涛
     * @date 2023/9/6 19:46
     * @brief getDownloadProgress 获取oss下载进度
     * @param[_progress下载进度]参数说明
     * @return void 返回说明
     * @note 注解
     * @warning 警告
     */
    public void getDownloadProgress(long _progress,long readSize);

    public  void ossStart();
    public  void ossnError(Throwable throwable);
    public  void ossnComplete();
}
