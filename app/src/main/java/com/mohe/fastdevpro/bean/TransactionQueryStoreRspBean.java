package com.mohe.fastdevpro.bean;

import java.util.List;

public class TransactionQueryStoreRspBean extends BaseRspBean {
    private List<TransactionQueryStoreBean> stoeTxnList;
    private String tot_rec_num;

    public String getTot_rec_num() {
        return this.tot_rec_num == null ? "" : this.tot_rec_num;
    }

    public void setTot_rec_num(String str) {
        this.tot_rec_num = str;
    }

    public void setStoeTxnList(List<TransactionQueryStoreBean> list) {
        this.stoeTxnList = list;
    }

    public List<TransactionQueryStoreBean> getStoeTxnList() {
        return this.stoeTxnList;
    }
}
