package com.mohe.fastdevpro.pattern.builderPattern;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xiePing on 2019/3/31 0031.
 * Description:
 */
public class OrderBuilder {

    private OrderBuilder orderBuilder;
    private List<FoodItem> foodItems=new ArrayList<>();
    private float fTotlePrice;

    public OrderBuilder() {

    }

    public OrderBuilder addItem(FoodItem foodItem){
        foodItems.add(foodItem);
        fTotlePrice+=foodItem.getPrice();
        return this;
    }

    public Order build(){
        Order order=new Order();
        order.getItem().addAll(foodItems);
        order.setPrice(fTotlePrice);
        return order;
    }
}
