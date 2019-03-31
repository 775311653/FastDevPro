package com.mohe.fastdevpro.utils;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.after;
import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.atMost;
import static org.mockito.Mockito.only;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by xiePing on 2018/10/10 0010.
 * Description:
 */
public class MyCalculateTest {

    private MyCalculate myCalculate;

    @Spy
    IMathUtil mathUtil;

    @Rule
    public MockitoRule mockitoRule= MockitoJUnit.rule();

    @Before
    public void setUp() throws Exception {
        myCalculate=new MyCalculate();
//        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void add() {
        int a=3;
        int b=4;
        assertEquals(7,myCalculate.add(a,b));
//        IMathUtil mathUtil= Mockito.mock(IMathUtil.class);
//        verify(mathUtil,after(1000));
        when(mathUtil.abs(-1)).thenReturn(1); // 当调用abs(-1)时，返回1
        int abs=mathUtil.abs(-1);
        assertEquals(1,abs);
        verify(mathUtil,times(1)).abs(anyInt());
        verify(mathUtil,atMost(2)).abs(-1);
        verify(mathUtil,atLeast(0)).abs(-1);
        verify(mathUtil,only()).abs(-1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void divide(){
        double d=myCalculate.divide(4,0);
    }

    public interface IMathUtil{
        int abs(int num);
    }
}