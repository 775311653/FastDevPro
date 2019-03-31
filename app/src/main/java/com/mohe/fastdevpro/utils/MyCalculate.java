package com.mohe.fastdevpro.utils;

/**
 * Created by xiePing on 2018/10/10 0010.
 * Description:
 */
public class MyCalculate {
    public int add(int a,int b){
        return a+b;
    }

    public double divide(int a,int b){
        if (b==0) throw new IllegalArgumentException("除数不能为0");
        return a/b;
    }
}
