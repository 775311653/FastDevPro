package com.mohe.fastdevpro.bean;

public class TransactionQueryStoreBean {
    private String ac_dt;
    private String corg_no;
    private String log_no;
    private String paytype;
    private String star_currency;
    private String stoe_nm;
    private String tot_amt_day;
    private String tot_fee_day;
    private String trm_no;
    private String txn_amt;
    private String txn_cd;
    private String txn_fee_amt;
    private String txn_tm;
    private String txn_typ="";

    public String getMerc_id() {
        return merc_id;
    }

    public void setMerc_id(String merc_id) {
        this.merc_id = merc_id;
    }

    private String merc_id;

    public String getPaytype() {
        return this.paytype == null ? "" : this.paytype;
    }

    public void setPaytype(String str) {
        this.paytype = str;
    }

    public String getStoe_nm() {
        return this.stoe_nm == null ? "" : this.stoe_nm;
    }

    public void setStoe_nm(String str) {
        this.stoe_nm = str;
    }

    public String getTxn_cd() {
        return this.txn_cd == null ? "" : this.txn_cd;
    }

    public void setTxn_cd(String str) {
        this.txn_cd = str;
    }

    public String getTxn_tm() {
        return this.txn_tm == null ? "" : this.txn_tm;
    }

    public void setTxn_tm(String str) {
        this.txn_tm = str;
    }

    public String getTrm_no() {
        return this.trm_no == null ? "" : this.trm_no;
    }

    public void setTrm_no(String str) {
        this.trm_no = str;
    }

    public String getCorg_no() {
        return this.corg_no == null ? "" : this.corg_no;
    }

    public void setCorg_no(String str) {
        this.corg_no = str;
    }

    public String getTxn_amt() {
        return this.txn_amt == null ? "" : this.txn_amt;
    }

    public void setTxn_amt(String str) {
        this.txn_amt = str;
    }

    public String getTxn_fee_amt() {
        return this.txn_fee_amt == null ? "" : this.txn_fee_amt;
    }

    public void setTxn_fee_amt(String str) {
        this.txn_fee_amt = str;
    }

    public String getAc_dt() {
        return this.ac_dt == null ? "" : this.ac_dt;
    }

    public void setAc_dt(String str) {
        this.ac_dt = str;
    }

    public String getTot_amt_day() {
        return this.tot_amt_day == null ? "" : this.tot_amt_day;
    }

    public void setTot_amt_day(String str) {
        this.tot_amt_day = str;
    }

    public String getTot_fee_day() {
        return this.tot_fee_day == null ? "" : this.tot_fee_day;
    }

    public void setTot_fee_day(String str) {
        this.tot_fee_day = str;
    }

    public String getLog_no() {
        return this.log_no == null ? "" : this.log_no;
    }

    public void setLog_no(String str) {
        this.log_no = str;
    }

    public String getTxn_typ() {
        return this.txn_typ == null ? "" : this.txn_typ;
    }

    public void setTxn_typ(String str) {
        this.txn_typ = str;
    }

    public String getStar_currency() {
        return this.star_currency == null ? "" : this.star_currency;
    }

    public void setStar_currency(String str) {
        this.star_currency = str;
    }
}
