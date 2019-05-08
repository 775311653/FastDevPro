package com.mohe.fastdevpro.bean;

import java.io.Serializable;

public class BaseRspBean implements Serializable {
    private String code;
    private String message;
    private String repCode;
    private String repMsg;
    private String serviceTime;
    private String token_id;

    public String getRepCode() {
        return this.repCode;
    }

    public void setRepCode(String str) {
        this.repCode = str;
    }

    public String getServiceTime() {
        return this.serviceTime;
    }

    public void setServiceTime(String str) {
        this.serviceTime = str;
    }

    public String getToken_id() {
        return this.token_id;
    }

    public void setToken_id(String str) {
        this.token_id = str;
    }

    public String getRepMsg() {
        return this.repMsg;
    }

    public void setRepMsg(String str) {
        this.repMsg = str;
    }

    public String getCode() {
        return this.code;
    }

    public void setCode(String str) {
        this.code = str;
    }

    public String getMessage() {
        return this.message;
    }

    public void setMessage(String str) {
        this.message = str;
    }
}
