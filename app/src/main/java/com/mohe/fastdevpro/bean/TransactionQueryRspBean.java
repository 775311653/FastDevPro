package com.mohe.fastdevpro.bean;

import java.util.List;

public class TransactionQueryRspBean extends BaseRspBean {
    private List<TransactionQueryBean> qryList;
    private String sn_no;
    private String tot_amt;
    private String tot_cnt;
    private String tot_day;
    private String tot_fee;
    private String tot_merc;
    private String tot_stoe;

    public String getTot_merc() {
        return this.tot_merc == null ? "" : this.tot_merc;
    }

    public void setTot_merc(String str) {
        this.tot_merc = str;
    }

    public String getSn_no() {
        return this.sn_no == null ? "" : this.sn_no;
    }

    public void setSn_no(String str) {
        this.sn_no = str;
    }

    public String getTot_amt() {
        return this.tot_amt == null ? "" : this.tot_amt;
    }

    public void setTot_amt(String str) {
        this.tot_amt = str;
    }

    public String getTot_fee() {
        return this.tot_fee == null ? "" : this.tot_fee;
    }

    public void setTot_fee(String str) {
        this.tot_fee = str;
    }

    public String getTot_stoe() {
        return this.tot_stoe == null ? "" : this.tot_stoe;
    }

    public void setTot_stoe(String str) {
        this.tot_stoe = str;
    }

    public String getTot_day() {
        return this.tot_day == null ? "" : this.tot_day;
    }

    public void setTot_day(String str) {
        this.tot_day = str;
    }

    public String getTot_cnt() {
        return this.tot_cnt == null ? "" : this.tot_cnt;
    }

    public void setTot_cnt(String str) {
        this.tot_cnt = str;
    }

    public List<TransactionQueryBean> getQryList() {
        return this.qryList;
    }

    public void setQryList(List<TransactionQueryBean> list) {
        this.qryList = list;
    }
}
