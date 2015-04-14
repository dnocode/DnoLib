package com.dnocode.lib.business.utils;

import android.content.Context;
import android.content.SharedPreferences;
import com.google.gson.Gson;

public  abstract  class Savable {

    private  static SharedPreferences sPreferences;
    private  final static Gson sGson = new Gson();
    public abstract  String storeKey();

    public void save(Context context , int  mode) {

       ObjectSerializer.getInstance(context,mode).save(storeKey(),this);
    }

    public void commit(Context context , int  mode) {

       ObjectSerializer.getInstance(context, mode).commit();
    }
    public   <T> T load( Context context,Class<T> a ,int  mode) {

        return ObjectSerializer.getInstance(context, mode).load(storeKey(),a);
    }
}