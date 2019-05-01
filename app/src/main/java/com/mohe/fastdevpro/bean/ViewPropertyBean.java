package com.mohe.fastdevpro.bean;

/**
 * Created by xiePing on 2019/5/1 0001.
 * Description:view属性bean类
 */
public class ViewPropertyBean {
    private int width;
    private int height;

    public ViewPropertyBean(int width, int height) {
        this.width = width;
        this.height = height;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }
}
