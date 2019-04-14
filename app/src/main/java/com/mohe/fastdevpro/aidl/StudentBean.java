package com.mohe.fastdevpro.aidl;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by xiePing on 2019/4/14 0014.
 * Description:
 */
public class StudentBean implements Parcelable {
    private int age;
    private String name;

    public StudentBean(int age, String name) {
        this.age = age;
        this.name = name;
    }

    protected StudentBean(Parcel in) {
        age = in.readInt();
        name = in.readString();
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(age);
        dest.writeString(name);
    }

    public static final Creator<StudentBean> CREATOR = new Creator<StudentBean>() {
        @Override
        public StudentBean createFromParcel(Parcel in) {
            return new StudentBean(in);
        }

        @Override
        public StudentBean[] newArray(int size) {
            return new StudentBean[size];
        }
    };
}
