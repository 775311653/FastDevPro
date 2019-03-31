package com.mohe.fastdevpro.utils;


import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by xiePing on 2018/10/9 0009.
 * Description:
 */
public class GsonUtilsTest {

    private TestBean bean;
    @Before
    public void setUp() throws Exception {

    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void getGson() {
    }

    @Test
    public void getGson1() {
    }

    @Test
    public void fromJson() {
        bean= GsonUtils.fromJson("{\n" +
                "    \"name\": \"xp\",\n" +
                "    \"age\": 25\n" +
                "}",TestBean.class);

//        bean= GsonUtils.fromJson("{}",TestBean.class);
        assertEquals(25,bean.age);
    }
    private class TestBean{
        private String name="";
        private int age=0;

        public TestBean(String name, int age) {
            this.name = name;
            this.age = age;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getAge() {
            return age;
        }

        public void setAge(int age) {
            this.age = age;
        }
    }
}