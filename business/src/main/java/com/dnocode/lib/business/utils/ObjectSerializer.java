package com.dnocode.lib.business.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;


public  class ObjectSerializer {

    private static ObjectSerializer INSTANCE;
    private   SharedPreferences mPreferences;
    private SharedPreferences.Editor mEditor;
    private  final  Gson mGson = new Gson();
    private int mMode;
    private  Context mCtx;

    private ObjectSerializer(Context context,int mode){
        this.mCtx=context;
        this.mMode=mode;
        initPreferences();
    }

    public static ObjectSerializer getInstance(Context context,int mode){
        if(INSTANCE==null){  INSTANCE=new ObjectSerializer(context,mode); }

        return INSTANCE;
    }


    private void initPreferences(){

    mPreferences = mPreferences ==null?
            mCtx.getSharedPreferences(mCtx.getPackageName(),mMode)
            :mPreferences;
        mEditor=mPreferences.edit();

    }

    public void save(String key,Object object) {


       mEditor.putString(key, mGson.toJson(object));
    }

    public void commit(boolean ... async) {

        if (async.length>0&&async[0]){
            mEditor.apply();
            return;
        }
        mEditor.commit();
    }

    public   <T> T load( String key,Class<T> a ) {


        String gson = mPreferences.getString(key, null);

        if (gson == null) { return null;}
            try {  return mGson.fromJson(gson, a); }

            catch (Exception e) {

                throw new IllegalArgumentException("Object stored with key "+ a.getName() + " is instance of other class");
            }
    }
}