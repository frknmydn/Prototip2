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
    public void setSharedPreferenceForLong(Context context,String key, long value){
        SharedPreferences sharedPreferences = context.getSharedPreferences(context.getPackageName(),Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putLong(key,value);
        editor.commit();

    }

    public void setSharedPreferenceForInt(Context context,String key, int value){
        SharedPreferences sharedPreferences = context.getSharedPreferences(context.getPackageName(),Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(key,value);
        editor.commit();

    }

    public int getSharedPreferenceForInt(Context context, String key, int defaultValue){
        return context.getSharedPreferences(context.getPackageName(),Context.MODE_PRIVATE).getInt(key,defaultValue);
    }

    public long getSharedPreferenceForLong(Context context, String key, long defaultValue){
        return context.getSharedPreferences(context.getPackageName(),Context.MODE_PRIVATE).getLong(key,defaultValue);
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
