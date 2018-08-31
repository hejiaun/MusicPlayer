package com.example.administrator.musicplayer.utils;

import java.util.Date;

/**
 * Created by Administrator on 2017/6/23.
 */

public class DateUtils {
    private static DateUtils dateUtils;

    private DateUtils() {

    }

    public static DateUtils getInstence() {
        if (dateUtils == null)
            dateUtils = new DateUtils();
        return dateUtils;
    }

    /**
     * 获取文件区分时间
     * @return
     */
    private String getFileDate() {
        Date date = new Date();
        date.getTime();
        return date.getTime() + "";
    }

}
