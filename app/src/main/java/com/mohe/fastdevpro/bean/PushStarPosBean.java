package com.mohe.fastdevpro.bean;

/**
 * Created by xiePing on 2019/5/9 0009.
 * Description:
 */
public class PushStarPosBean {

    /**
     * provider : 2
     * pay_time : 20190507010101
     * mch : 宿迁安托商务信息资讯有限公司
     * mch_id : yyyyy
     * pay_money : 10.01
     * bill_no : zzzzzzzzzz
     */

    private String provider="";
    private String pay_time="";
    private String mch="";
    private String mch_id="";
    private String pay_money="";
    private String bill_no="";

    public String getProvider() {
        return provider;
    }

    public void setProvider(String provider) {
        this.provider = provider;
    }

    public String getPay_time() {
        return pay_time;
    }

    public void setPay_time(String pay_time) {
        this.pay_time = pay_time;
    }

    public String getMch() {
        return mch;
    }

    public void setMch(String mch) {
        this.mch = mch;
    }

    public String getMch_id() {
        return mch_id;
    }

    public void setMch_id(String mch_id) {
        this.mch_id = mch_id;
    }

    public String getPay_money() {
        return pay_money;
    }

    public void setPay_money(String pay_money) {
        this.pay_money = pay_money;
    }

    public String getBill_no() {
        return bill_no;
    }

    public void setBill_no(String bill_no) {
        this.bill_no = bill_no;
    }
}
