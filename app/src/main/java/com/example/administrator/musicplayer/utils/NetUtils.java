package com.example.administrator.musicplayer.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkInfo;
import android.util.Log;

/**
 * Created by Administrator on 2017/6/21.
 */

public class NetUtils {
    private static NetUtils netUtils;

    private NetUtils() {

    }

    public static NetUtils getInstence() {
        if (netUtils == null)
            netUtils = new NetUtils();
        return netUtils;
    }

    /**
     * 判断是否网络是否可用
     *
     * @param context
     * @return
     */
    public boolean getNetState(Context context) {
        ConnectivityManager systemService = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo allNetworks = systemService.getActiveNetworkInfo();
        if (allNetworks != null) {
            return true;
        }
        return false;
    }

    /**
     * 获取移动数据是否开启
     *
     * @param context
     * @return
     */
    public boolean getMobileDataState(Context context) {
        ConnectivityManager systemService = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo wifi = systemService.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        if (wifi != null && wifi.getState() == NetworkInfo.State.CONNECTED) {//用wifi数据
            return false;
        }
        return true;//用移动数据
    }



}
