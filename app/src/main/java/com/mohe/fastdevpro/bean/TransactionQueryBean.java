package com.mohe.fastdevpro.bean;

import java.io.Serializable;

public class TransactionQueryBean implements Serializable {
    private String merc_id;
    private String stoe_id;
    private String stoe_nm;
    private String tot_txn_amt;
    private String tot_txn_cnt;
    private String tot_txn_fee;

    public String getMerc_id() {
        return this.merc_id == null ? "" : this.merc_id;
    }

    public void setMerc_id(String str) {
        this.merc_id = str;
    }

    public String getStoe_nm() {
        return this.stoe_nm == null ? "" : this.stoe_nm;
    }

    public void setStoe_nm(String str) {
        this.stoe_nm = str;
    }

    public String getStoe_id() {
        return this.stoe_id == null ? "" : this.stoe_id;
    }

    public void setStoe_id(String str) {
        this.stoe_id = str;
    }

    public String getTot_txn_fee() {
        return this.tot_txn_fee == null ? "" : this.tot_txn_fee;
    }

    public void setTot_txn_fee(String str) {
        this.tot_txn_fee = str;
    }

    public String getTot_txn_amt() {
        return this.tot_txn_amt == null ? "" : this.tot_txn_amt;
    }

    public void setTot_txn_amt(String str) {
        this.tot_txn_amt = str;
    }

    public String getTot_txn_cnt() {
        return this.tot_txn_cnt == null ? "" : this.tot_txn_cnt;
    }

    public void setTot_txn_cnt(String str) {
        this.tot_txn_cnt = str;
    }
}
