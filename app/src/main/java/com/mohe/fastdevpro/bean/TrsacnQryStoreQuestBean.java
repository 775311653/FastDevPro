package com.mohe.fastdevpro.bean;

import java.io.Serializable;

public class TrsacnQryStoreQuestBean implements Serializable {
    private String beg_dt;
    private String end_dt;
    private String merc_id;
    private String pay_type;
    private String sn_no;
    private String trm_no;
    private String txn_cd;
    private String txn_mode;
    private String txn_typ;
    private String usr_typ;

    public String getSn_no() {
        return this.sn_no == null ? "" : this.sn_no;
    }

    public void setSn_no(String str) {
        this.sn_no = str;
    }

    public String getPay_type() {
        return this.pay_type == null ? "" : this.pay_type;
    }

    public void setPay_type(String str) {
        this.pay_type = str;
    }

    public String getTxn_cd() {
        return this.txn_cd == null ? "" : this.txn_cd;
    }

    public void setTxn_cd(String str) {
        this.txn_cd = str;
    }

    public String getUsr_typ() {
        return this.usr_typ == null ? "" : this.usr_typ;
    }

    public void setUsr_typ(String str) {
        this.usr_typ = str;
    }

    public String getMerc_id() {
        return this.merc_id == null ? "" : this.merc_id;
    }

    public void setMerc_id(String str) {
        this.merc_id = str;
    }

    public String getBeg_dt() {
        return this.beg_dt == null ? "" : this.beg_dt;
    }

    public void setBeg_dt(String str) {
        this.beg_dt = str;
    }

    public String getEnd_dt() {
        return this.end_dt == null ? "" : this.end_dt;
    }

    public void setEnd_dt(String str) {
        this.end_dt = str;
    }

    public String getTxn_typ() {
        return this.txn_typ == null ? "" : this.txn_typ;
    }

    public void setTxn_typ(String str) {
        this.txn_typ = str;
    }

    public String getTxn_mode() {
        return this.txn_mode == null ? "" : this.txn_mode;
    }

    public void setTxn_mode(String str) {
        this.txn_mode = str;
    }

    public String getTrm_no() {
        return this.trm_no == null ? "" : this.trm_no;
    }

    public void setTrm_no(String str) {
        this.trm_no = str;
    }
}
