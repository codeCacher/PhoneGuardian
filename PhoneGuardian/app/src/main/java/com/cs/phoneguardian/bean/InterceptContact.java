package com.cs.phoneguardian.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Administrator on 2017/4/16.
 */

public class InterceptContact{
    private int id;
    private String name;
    private String phoneNumber;
    private int interceptType;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public int getInterceptType() {
        return interceptType;
    }

    public void setInterceptType(int interceptType) {
        this.interceptType = interceptType;
    }
}
