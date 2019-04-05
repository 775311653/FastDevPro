package com.mohe.fastdevpro.pattern.commandPattern;

import android.support.annotation.StringDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by xiePing on 2019/4/5 0005.
 * Description:
 */
public class StockActionBean {

    public static final String STOCK_ACTION_BUY="stock_action_buy";
    public static final String STOCK_ACTION_SELL="stock_action_sell";

    @StringDef({STOCK_ACTION_BUY,STOCK_ACTION_SELL})
    @Retention(RetentionPolicy.SOURCE)
    @interface StockActionType{}

    public String type;
    public StockBean stockBean;

    public StockActionBean(@StockActionType String type, StockBean stockBean) {
        this.type = type;
        this.stockBean = stockBean;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public StockBean getStockBean() {
        return stockBean;
    }

    public void setStockBean(StockBean stockBean) {
        this.stockBean = stockBean;
    }
}
