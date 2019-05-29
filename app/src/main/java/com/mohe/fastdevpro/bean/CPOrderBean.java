package com.mohe.fastdevpro.bean;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by xiePing on 2019/5/25 0025.
 * Description:
 */
@Entity
public class CPOrderBean {
    @Id private long id;
    private String amount;
    private String currentUnit;
    private String orderId;
    private String orderStatus;
    private String orderTime;
    private String orderType;
    private String title;
    private String tn;

    public CPOrderBean(String amount, String currentUnit, String orderId, String orderStatus, String orderTime, String orderType, String title, String tn) {
        this.amount = amount;
        this.currentUnit = currentUnit;
        this.orderId = orderId;
        this.orderStatus = orderStatus;
        this.orderTime = orderTime;
        this.orderType = orderType;
        this.title = title;
        this.tn = tn;
    }

    @Generated(hash = 1642927139)
    public CPOrderBean(long id, String amount, String currentUnit, String orderId, String orderStatus, String orderTime, String orderType, String title,
            String tn) {
        this.id = id;
        this.amount = amount;
        this.currentUnit = currentUnit;
        this.orderId = orderId;
        this.orderStatus = orderStatus;
        this.orderTime = orderTime;
        this.orderType = orderType;
        this.title = title;
        this.tn = tn;
    }

    @Generated(hash = 918894614)
    public CPOrderBean() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getCurrentUnit() {
        return currentUnit;
    }

    public void setCurrentUnit(String currentUnit) {
        this.currentUnit = currentUnit;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public String getOrderTime() {
        return orderTime;
    }

    public void setOrderTime(String orderTime) {
        this.orderTime = orderTime;
    }

    public String getOrderType() {
        return orderType;
    }

    public void setOrderType(String orderType) {
        this.orderType = orderType;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTn() {
        return tn;
    }

    public void setTn(String tn) {
        this.tn = tn;
    }
}
