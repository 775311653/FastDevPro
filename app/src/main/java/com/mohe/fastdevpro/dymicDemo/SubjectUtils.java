package com.mohe.fastdevpro.dymicDemo;

import com.blankj.utilcode.util.LogUtils;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * Created by xiePing on 2019/4/16 0016.
 * Description:
 */
public class SubjectUtils implements InvocationHandler {

    private Object delegate;

    public  Object bindSubject(Object delegate){
        this.delegate=delegate;
        return Proxy.newProxyInstance(delegate.getClass().getClassLoader()
                ,delegate.getClass().getInterfaces()
                ,this);
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        Object result = null;
        if (method.getName().equals("request")){
            LogUtils.i("执行绑定的RealSubject的方法前",method.getName());
            result=method.invoke(delegate,args);
            LogUtils.i("执行绑定的RealSubject的方法后",result!=null?result:"");
        }
        return result;
    }
}
