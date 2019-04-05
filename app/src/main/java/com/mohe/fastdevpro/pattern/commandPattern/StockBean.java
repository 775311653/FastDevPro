package com.mohe.fastdevpro.pattern.commandPattern;

/**
 * Created by xiePing on 2019/4/5 0005.
 * Description:
 */
public class StockBean {
    public String name;
    public long num;

    public StockBean(String name, long num) {
        this.name = name;
        this.num = num;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getNum() {
        return num;
    }

    public void setNum(long num) {
        this.num = num;
    }
}
