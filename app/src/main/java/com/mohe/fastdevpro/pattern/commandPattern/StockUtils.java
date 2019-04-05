package com.mohe.fastdevpro.pattern.commandPattern;

import com.blankj.utilcode.util.LogUtils;

/**
 * Created by xiePing on 2019/4/5 0005.
 * Description:
 */
public class StockUtils {
    public static void buyStock(StockBean stockBean){
        LogUtils.i("买了股票",stockBean.name,stockBean.num);
    }

    public static void sellStock(StockBean stockBean){
        LogUtils.i("卖了股票",stockBean.name,stockBean.num);
    }
}
