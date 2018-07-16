package com.mohe.fastdevpro.utils;

import java.lang.reflect.ParameterizedType;

/**
 * Created by xiePing on 2018/7/16 0016.
 */
public class CreateUtil {
    static <T> T getT(Object o, int i) {
        try {
            return ((Class<T>) ((ParameterizedType) (o.getClass().getGenericSuperclass())).getActualTypeArguments()[i]).newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
