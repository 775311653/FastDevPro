package com.mohe.fastdevpro.pattern.builderPattern;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.blankj.utilcode.util.LogUtils;
import com.mohe.fastdevpro.R;

public class BuilderDemoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_builder_demo);
        Order order= new OrderBuilder().addItem(new FoodItem("汉堡", 10.5f))
                .addItem(new FoodItem("水果",5.2f))
                .build();
        LogUtils.i("食物是"+order.getItem().get(0).getName()+","+order.getItem().get(1).getName()
                ,"价格是"+order.getPrice());
    }
}
