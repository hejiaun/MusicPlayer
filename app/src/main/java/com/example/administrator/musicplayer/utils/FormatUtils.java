package com.example.administrator.musicplayer.utils;

import android.graphics.Bitmap;

import com.example.administrator.musicplayer.bean.Music;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.Locale;

/**
 * Created by Administrator on 2017/6/20.
 */

public class FormatUtils {
    private static FormatUtils timeFormatUtils;
    private OutputStream os;

    private FormatUtils() {

    }

    public static FormatUtils getInstence() {
        if (timeFormatUtils == null) {
            timeFormatUtils = new FormatUtils();
        }
        return timeFormatUtils;
    }

    public String getFormatTime(long time) {
        String result = "";
        int minute = (int) time / 60 / 1000;
        int second = (int) ((time - minute * 1000 * 60) / 1000);
        if (minute < 10) {
            result = "0" + minute;
        } else {
            result = minute + "";
        }
        if (second < 10) {
            result = result + ":" + "0" + second;
        } else {
            result = result + ":" + second + "";
        }
        return result;
    }

    public String getFormatTimeBySeconde(long i) {
        String result;
        long minute = i / 60;
        long second = i - minute * 60;
        if (minute < 10) {
            result = "0" + minute;
        } else {
            result = minute + "";
        }
        if (second < 10) {
            result = result + ":0" + second;
        } else {
            result = result + ":" + second;
        }
        return result;
    }

    public float getFormatSize(long longSize) {
        String result = String.format(Locale.getDefault(), "%.2f", (float) longSize / 1024 / 1024);
        return Float.valueOf(result);
    }

    public String inputStream2String(InputStream is) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();
        String line = null;
        try {
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return sb.toString();
    }

//    public void inputStream2File(InputStream is, File file) {
//        try {
//            os = new FileOutputStream(file);
//            int bytesRead = 0;
//            byte[] buffer = new byte[1024];
//            while ((bytesRead = is.read(buffer, 0, 1024)) != -1) {
//                os.write(buffer, 0, bytesRead);
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        } finally {
//            try {
//                is.close();
//                os.close();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
//    }


}
