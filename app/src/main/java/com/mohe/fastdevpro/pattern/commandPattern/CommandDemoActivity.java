package com.mohe.fastdevpro.pattern.commandPattern;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.mohe.fastdevpro.R;

import java.util.ArrayList;
import java.util.List;

public class CommandDemoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_command_demo);
        BrokerBean brokerBean=new BrokerBean();
        List<StockActionBean> actionBeans=getStockActionData();
        for (StockActionBean action:actionBeans){
            brokerBean.addAction(action);
        }
        brokerBean.doActions();

    }

    private List<StockActionBean> getStockActionData() {
        List<StockActionBean> actionBeans=new ArrayList<>();
        actionBeans.add(new StockActionBean(
                StockActionBean.STOCK_ACTION_BUY
                ,new StockBean("中兴通讯",200)));
        actionBeans.add(new StockActionBean(
                StockActionBean.STOCK_ACTION_SELL
                ,new StockBean("中兴通讯",100)));
        actionBeans.add(new StockActionBean(
                StockActionBean.STOCK_ACTION_BUY
                ,new StockBean("科蓝软件",500)));
        actionBeans.add(new StockActionBean(
                StockActionBean.STOCK_ACTION_SELL
                ,new StockBean("科蓝软件",200)));
        return actionBeans;
    }
}
