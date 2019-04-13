package com.mohe.fastdevpro.pattern.intercepterPattern;

/**
 * Created by xiePing on 2019/4/13 0013.
 * Description:
 */
public class Student {
    public String name;
    public String id;

    public Student(String name, String id) {
        this.name = name;
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
