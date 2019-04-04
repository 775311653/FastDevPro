package com.mohe.fastdevpro.pattern.filterPattern;

/**
 * Created by xiePing on 2019/4/4 0004.
 * Description:
 */
public class Person {
    public int age;
    public String sex;//man,woman
    public String status;//single,unSingle

    public Person(int age, String sex, String status) {
        this.age = age;
        this.sex = sex;
        this.status = status;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
