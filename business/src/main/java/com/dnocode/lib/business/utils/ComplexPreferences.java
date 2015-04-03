package com.dnocode.lib.business.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;

public class ComplexPreferences {

    private static ComplexPreferences       complexPreferences;
    private final Context context;
    private final SharedPreferences preferences;
    private final SharedPreferences.Editor  editor;
    private  Gson mGson = new Gson();

    private ComplexPreferences(Context context, String namePreferences, int mode) {

       this.context = context;
       if (namePreferences == null || namePreferences.equals("")) { namePreferences = "abhan";}
        preferences = context.getSharedPreferences(namePreferences, mode);
        editor = preferences.edit();
    }

    public static ComplexPreferences getComplexPreferences(Context context,String namePreferences, int mode) {

        return complexPreferences==null?new ComplexPreferences(context,namePreferences, mode):complexPreferences;
    }

    public void putObject(String key, Object object) {

        if (object == null|| key.equals("") || key == null) {  throw new IllegalArgumentException("Object is null"); }
        editor.putString(key, mGson.toJson(object));
    }

    public void commit() {
        editor.commit();
    }

    public <T> T getObject(String key, Class<T> a) {

        String gson = preferences.getString(key, null);

        if (gson == null) { return null;}

            try {
                    return mGson.fromJson(gson, a);

            }
            catch (Exception e) {
                throw new IllegalArgumentException("Object stored with key "+ key + " is instance of other class");
            }
    }
}