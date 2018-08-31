package com.example.administrator.musicplayer.utils;

import android.os.Message;

/**
 * Created by Administrator on 2017/5/21.
 */

public class ContentValuseUtils {
    /**
     * 扫描音乐
     */
    public final static int INIT_MUSIC = 999;
    /**
     * sharepreference music 值
     */
    public static final String SP_MUSIC = "spMusic";
    public static final String MUSIC_POSITION = "musicPosition";
    public static final String MUSIC_PATH = "musicPath";
    public static final int PROGRESS = 998;
    public static final int MUSIC_NEXT = 997;
    public static final int SEARCH_FINISH = 996;
    public static final int PLAY_NET_MUSIC = 995;
    public static final int GET_IMAGE_FINISH = 994;
    public static final int GET_SONGLIST = 993;

    public static final String SPEACE_NAME = "http://schemas.android.com/apk/res/com.example.administrator.musicplayer";
    public static final String IV_MODE = "iv_mode";
    public static final String BASE_URL = "http://tingapi.ting.baidu.com/v1/restserver/";
    public static final String SEARCH_SONG_BY_ID = "http://music.baidu.com/data/music/links?songIds=";

    public static final String METHOD_SEARCH_SONG_LIST = "baidu.ting.search.catalogSug";

    public static final String METHOD_GET_LIST = "baidu.ting.billboard.billList";



}
