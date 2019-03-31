package com.mohe.fastdevpro.pattern.builderPattern;

/**
 * Created by xiePing on 2019/3/31 0031.
 * Description:
 */
public class FoodItem {
    private String name;
    private float price;

    public FoodItem(String name, float price) {
        this.name = name;
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }
}
