package com.mohe.fastdevpro.utils;

import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import java.text.ParseException;
import java.util.Date;

import static org.junit.Assert.*;

/**
 * Created by xiePing on 2018/10/12 0012.
 * Description:
 */
public class DateUtilTest {

    private String time = "2017-10-15 16:00:02";

    private long timeStamp = 1508054402000L;

    private Date mDate;

    @Before
    public void setUp() throws Exception {
        System.out.print("测试开始");
        mDate=new Date(timeStamp);
    }

    @After
    public void tearDown() throws Exception {
        System.out.print("测试结束");
    }

    @Test
    public void dateToStamp() throws ParseException {
        assertEquals(timeStamp,DateUtil.dateToStamp(time));
    }

    @Test
    public void stampToDate() {
        assertEquals(time,DateUtil.stampToDate(timeStamp));
    }

    @Test(expected = ParseException.class)
    public void dateToStamp1() throws Exception{
        DateUtil.dateToStamp("2017-10-15");
    }

    @Test
    @Ignore("test方法不执行\n")
    public void test(){
        System.out.print("----");
    }
}