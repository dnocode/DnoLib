package com.dnocode.lib.business.utils;

import android.content.Context;
import android.content.SharedPreferences;
import com.google.gson.Gson;

/**
 * trick for
 * objects saving
 */
public  abstract  class Savable {

    private  static SharedPreferences sPreferences;

    private  final static Gson sGson = new Gson();

    public abstract  String storeKey();

    private void initPreferences(Context context , int ... mode){

        if(sPreferences==null) {
            if (mode.length > 0) {
                sPreferences= context.getSharedPreferences(context.getPackageName(), mode[0]);
            }
        }
    }

    public void save(Context context , int ... mode) {

        initPreferences(context,mode);
        sPreferences.edit().putString(storeKey(), sGson.toJson(this));
    }

    public void commit(Context context , int ... mode) {

        initPreferences(context,mode);
        sPreferences.edit().commit();
    }

    public   <T> T load( Context context,Class<T> a ,int ... mode) {

        initPreferences(context,mode);
        String gson = sPreferences.getString(a.getName(), null);

        if (gson == null) { return null;}
            try {  return sGson.fromJson(gson, a); }
            catch (Exception e) {
                throw new IllegalArgumentException("Object stored with key "+ a.getName() + " is instance of other class");
            }
    }
}