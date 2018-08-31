package com.example.administrator.musicplayer.utils;


import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Administrator on 2017/5/22.
 */

public class SpUtils {
    private static SpUtils spUtils = null;

    private SpUtils() {
    }

    public static SpUtils getInstence() {
        if (spUtils == null) {
            spUtils = new SpUtils();
        }
        return spUtils;
    }

    /**
     * 保存字符串
     *
     * @param context 上下文
     * @param sp_name sp名称
     * @param key     键
     * @param value   值
     */
    public void saveString(Context context, String sp_name, String key, String value) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(sp_name, Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = sharedPreferences.edit();
        edit.putString(key, value);
        edit.commit();
    }

    public void saveInt(Context context, String sp_name, String key, int value) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(sp_name, Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = sharedPreferences.edit();
        edit.putInt(key, value);
        edit.commit();
    }

    /**
     * 获取字符串
     *
     * @param context 上下文
     * @param sp_name sp名称
     * @param key     键
     * @return
     */
    public String getString(Context context, String sp_name, String key) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(sp_name, Context.MODE_PRIVATE);
        String result = sharedPreferences.getString(key, "null");
        return result;
    }


    public int getInt(Context context, String sp_name, String key) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(sp_name, Context.MODE_PRIVATE);
        int result = sharedPreferences.getInt(key,-1);
        return result;
    }

}
