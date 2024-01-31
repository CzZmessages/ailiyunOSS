package com.example.ossservicedemo.Bean;

public class ApkBean {
    private Integer id;
    private String version;
    private String appName;
    private String isgrounding;
    private String user;
    private String isgroundingTime;
    private String fileState;
    private String filePath;
   private ApkBean data;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getIsgrounding() {
        return isgrounding;
    }

    public void setIsgrounding(String isgrounding) {
        this.isgrounding = isgrounding;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getIsgroundingTime() {
        return isgroundingTime;
    }

    public void setIsgroundingTime(String isgroundingTime) {
        this.isgroundingTime = isgroundingTime;
    }

    public String getFileState() {
        return fileState;
    }

    public void setFileState(String fileState) {
        this.fileState = fileState;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public ApkBean getData() {
        return data;
    }

    public void setData(ApkBean data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "ApkBean{" +
                "id=" + id +
                ", version='" + version + '\'' +
                ", appName='" + appName + '\'' +
                ", isgrounding='" + isgrounding + '\'' +
                ", user='" + user + '\'' +
                ", isgroundingTime='" + isgroundingTime + '\'' +
                ", fileState='" + fileState + '\'' +
                ", filePath='" + filePath + '\'' +
                ", data=" + data +
                '}';
    }
}
