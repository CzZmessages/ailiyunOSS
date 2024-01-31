package com.example.ossservicedemo.Bean;

public class DataBean {
    private DataBean data;
    private int versionId;
    private String versionName;
    private String versionTime;
    private String versionText;
    private String versionAddtime;
    private String fileState;
    private String versionRelease;
    private Boolean latesetVersion;
    private String versionPath;
    private int priority;

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public int getVersionId() {
        return versionId;
    }

    public void setVersionId(int versionId) {
        this.versionId = versionId;
    }

    public String getVersionName() {
        return versionName;
    }

    public void setVersionName(String versionName) {
        this.versionName = versionName;
    }

    public String getVersionTime() {
        return versionTime;
    }

    public void setVersionTime(String versionTime) {
        this.versionTime = versionTime;
    }

    public String getVersionText() {
        return versionText;
    }

    public void setVersionText(String versionText) {
        this.versionText = versionText;
    }

    public String getVersionAddtime() {
        return versionAddtime;
    }

    public void setVersionAddtime(String versionAddtime) {
        this.versionAddtime = versionAddtime;
    }

    public String getFileState() {
        return fileState;
    }

    public void setFileState(String fileState) {
        this.fileState = fileState;
    }

    public String getVersionRelease() {
        return versionRelease;
    }

    public void setVersionRelease(String versionRelease) {
        this.versionRelease = versionRelease;
    }

    public Boolean getLatesetVersion() {
        return latesetVersion;
    }

    public void setLatesetVersion(Boolean latesetVersion) {
        this.latesetVersion = latesetVersion;
    }

    public String getVersionPath() {
        return versionPath;
    }

    public void setVersionPath(String versionPath) {
        this.versionPath = versionPath;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    @Override
    public String toString() {
        return "DataBean{" +
                "data=" + data +
                ", versionId=" + versionId +
                ", versionName='" + versionName + '\'' +
                ", versionTime='" + versionTime + '\'' +
                ", versionText='" + versionText + '\'' +
                ", versionAddtime='" + versionAddtime + '\'' +
                ", fileState='" + fileState + '\'' +
                ", versionRelease='" + versionRelease + '\'' +
                ", latesetVersion=" + latesetVersion +
                ", versionPath='" + versionPath + '\'' +
                ", priority=" + priority +
                '}';
    }
}
