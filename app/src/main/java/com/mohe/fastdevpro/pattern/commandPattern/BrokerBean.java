package com.mohe.fastdevpro.pattern.commandPattern;

import com.blankj.utilcode.util.LogUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xiePing on 2019/4/5 0005.
 * Description:
 */
public class BrokerBean {
    public List<StockActionBean> stockActionBeans=new ArrayList<>();

    public void addAction(StockActionBean stockActionBean){
        stockActionBeans.add(stockActionBean);
    }

    public void doActions(){
        for (int i=0;i<stockActionBeans.size();i++){
            StockActionBean actionBean=stockActionBeans.get(i);
            switch (actionBean.getType()){
                case StockActionBean.STOCK_ACTION_BUY:
                    StockUtils.buyStock(actionBean.getStockBean());
                    break;
                case StockActionBean.STOCK_ACTION_SELL:
                    StockUtils.sellStock(actionBean.getStockBean());
                    break;
            }
        }
        stockActionBeans.clear();
        LogUtils.i("股票操作已执行完成");
    }
}
