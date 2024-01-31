package com.example.ossservicedemo.Bean;

public class OSSBean {
    private String AccessKeyId;
    private String AccessKeySecret;
    private String SecurityToken;
    private OSSBean data;

    public String getAccessKeyId() {
        return AccessKeyId;
    }

    public void setAccessKeyId(String accessKeyId) {
        AccessKeyId = accessKeyId;
    }

    public String getAccessKeySecret() {
        return AccessKeySecret;
    }

    public void setAccessKeySecret(String accessKeySecret) {
        AccessKeySecret = accessKeySecret;
    }

    public String getSecurityToken() {
        return SecurityToken;
    }

    public void setSecurityToken(String securityToken) {
        SecurityToken = securityToken;
    }

    public OSSBean getData() {
        return data;
    }

    public void setData(OSSBean data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "OSSBean{" +
                "AccessKeyId='" + AccessKeyId + '\'' +
                ", AccessKeySecret='" + AccessKeySecret + '\'' +
                ", SecurityToken='" + SecurityToken + '\'' +
                ", data=" + data +
                '}';
    }
}
