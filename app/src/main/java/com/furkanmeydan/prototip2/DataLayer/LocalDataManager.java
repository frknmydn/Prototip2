package com.furkanmeydan.prototip2.DataLayer;

import android.content.Context;
import android.content.SharedPreferences;

public class LocalDataManager {

    public void setSharedPreference(Context context,String key, String value){
        SharedPreferences sharedPreferences = context.getSharedPreferences(context.getPackageName(),Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key,value);
        editor.commit();

    }

    public String getSharedPreference(Context context, String key, String defaultValue){
        return context.getSharedPreferences(context.getPackageName(),Context.MODE_PRIVATE).getString(key,defaultValue);
    }

    public void clearSharedPreference(Context context){
        SharedPreferences sharedPref = context.getSharedPreferences(context.getPackageName(), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.clear();
        editor.commit();

    }

    public void removeSharedPreference(Context context, String key){
        SharedPreferences sharedPref = context.getSharedPreferences(context.getPackageName(), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.remove(key);
        editor.commit();

    }

}
