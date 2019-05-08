package com.mohe.fastdevpro.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by xieping on 2019/5/8.
 * 包含查询交易的presenter的对象实例的bean类
 */

public class QueryTransPresenterBean implements Parcelable{

    //查询交易的presenter的对象实例
    private Object queryTransPreInstance;

    public QueryTransPresenterBean(Object queryTransPreInstance) {
        this.queryTransPreInstance = queryTransPreInstance;
    }

    public Object getQueryTransPreInstance() {

        return queryTransPreInstance;
    }

    public void setQueryTransPreInstance(Object queryTransPreInstance) {
        this.queryTransPreInstance = queryTransPreInstance;
    }

    protected QueryTransPresenterBean(Parcel in) {
    }

    public static final Creator<QueryTransPresenterBean> CREATOR = new Creator<QueryTransPresenterBean>() {
        @Override
        public QueryTransPresenterBean createFromParcel(Parcel in) {
            return new QueryTransPresenterBean(in);
        }

        @Override
        public QueryTransPresenterBean[] newArray(int size) {
            return new QueryTransPresenterBean[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
    }
}
