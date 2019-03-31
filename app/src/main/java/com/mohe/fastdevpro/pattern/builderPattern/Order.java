package com.mohe.fastdevpro.pattern.builderPattern;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xiePing on 2019/3/31 0031.
 * Description:
 */
public class Order {

    private List<FoodItem> foodItems=new ArrayList<>();
    private float price=0;

    public void addItem(FoodItem foodItem){
        foodItems.add(foodItem);
    }

    public List<FoodItem> getItem(){
        return foodItems;
    }


    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }
}
