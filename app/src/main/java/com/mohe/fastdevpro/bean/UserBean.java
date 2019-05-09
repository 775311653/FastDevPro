package com.mohe.fastdevpro.bean;

import java.io.Serializable;

public class UserBean implements Serializable {
    private static final long serialVersionUID = 1;
    private String ac_mt;
    private String clubfunc;
    private String image;
    private boolean isBoundMerc;
    private String log_no;
    private String mbl_no;
    private String name;
    private String number;
    private String opprfuncs;
    private String phone;
    private String token_id;
    private String type;

    public String getLog_no() {
        return this.log_no;
    }

    public void setLog_no(String str) {
        this.log_no = str;
    }

    public String getImage() {
        return this.image == null ? "" : this.image;
    }

    public void setImage(String str) {
        this.image = str;
    }

    public String getClubfunc() {
        return this.clubfunc == null ? "" : this.clubfunc;
    }

    public void setClubfunc(String str) {
        this.clubfunc = str;
    }

    public boolean isBoundMerc() {
        return this.isBoundMerc;
    }

    public void setBoundMerc(boolean z) {
        this.isBoundMerc = z;
    }

    public String getAc_mt() {
        return this.ac_mt == null ? "" : this.ac_mt;
    }

    public void setAc_mt(String str) {
        this.ac_mt = str;
    }

    public String getPhone() {
        return this.phone == null ? "" : this.phone;
    }

    public void setPhone(String str) {
        this.phone = str;
    }

    public String getType() {
        return this.type == null ? "" : this.type;
    }

    public void setType(String str) {
        this.type = str;
    }

    public String getNumber() {
        return this.number == null ? "" : this.number;
    }

    public void setNumber(String str) {
        this.number = str;
    }

    public String getName() {
        return this.name == null ? "" : this.name;
    }

    public void setName(String str) {
        this.name = str;
    }

    public String getToken_id() {
        return this.token_id == null ? "" : this.token_id;
    }

    public void setToken_id(String str) {
        this.token_id = str;
    }

    public String getOpprfuncs() {
        return this.opprfuncs == null ? "" : this.opprfuncs;
    }

    public void setOpprfuncs(String str) {
        this.opprfuncs = str;
    }

    public String getMbl_no() {
        return this.mbl_no;
    }

    public void setMbl_no(String str) {
        this.mbl_no = str;
    }
}
