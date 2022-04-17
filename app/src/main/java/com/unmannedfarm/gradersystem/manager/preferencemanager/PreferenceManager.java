package com.unmannedfarm.gradersystem.manager.preferencemanager;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * 临时存储模块
 * */

public class PreferenceManager {

    /**
     * 保存数据
     * */
    //通过键值形式保存浮点型数据
    public static void saveFloatValue(Context context, float var, String key){
        SharedPreferences sp = context.getSharedPreferences("NavigationSystem", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putFloat(key, var);
        editor.apply();
    }
    //通过键值形式保存整型数据
    public static void saveIntValue(Context context, int var, String key){
        SharedPreferences sp = context.getSharedPreferences("NavigationSystem", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putInt(key, var);
        editor.apply();
    }
    //通过键值形式保存字符串数据
    public static void saveStringValue(Context context, String var, String key){
        SharedPreferences sp = context.getSharedPreferences("NavigationSystem", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(key, var);
        editor.apply();
    }
    //通过键值形式保存布尔数据
    public static void saveBooleanValue(Context context, boolean var, String key){
        SharedPreferences sp = context.getSharedPreferences("NavigationSystem", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putBoolean(key, var);
        editor.apply();
    }
    /**
     * 获取数据
     * */
    //通过对应的键值获取字符串数值，默认值为空字符串
    public static String getStringValue(Context context, String key){
        SharedPreferences sp = context.getSharedPreferences("NavigationSystem", Context.MODE_PRIVATE);
        String str = sp.getString(key, "");
        return str;
    }
    //通过对应的键值获取整型数据，默认值为0
    public static int getIntValue(Context context, String key){
        SharedPreferences sp = context.getSharedPreferences("NavigationSystem", Context.MODE_PRIVATE);
        int value = sp.getInt(key, 0);
        return value;
    }
    //通过对应的键值获取浮点型数据，默认值为0.0
    public static float getFloatValue(Context context, String key){
        SharedPreferences sp = context.getSharedPreferences("NavigationSystem", Context.MODE_PRIVATE);
        float value = sp.getFloat(key, 0f);
        return value;
    }
    //通过对应的键值获取浮点型数据，默认值为0.0
    public static boolean getBooleanValue(Context context, String key){
        SharedPreferences sp = context.getSharedPreferences("NavigationSystem", Context.MODE_PRIVATE);
        boolean value = sp.getBoolean(key, false);
        return value;
    }

}
