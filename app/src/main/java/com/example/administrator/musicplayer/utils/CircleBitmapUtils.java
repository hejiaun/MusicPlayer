package com.example.administrator.musicplayer.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.example.administrator.musicplayer.R;

/**
 * Created by Administrator on 2017/6/8.
 */

public class CircleBitmapUtils {
    private static CircleBitmapUtils circleBitmap;
    Bitmap bitmapTemplate;
    int templateWidth;
    int templateHeight;
    int mRadius;

    public CircleBitmapUtils(Context context) {
        bitmapTemplate = BitmapFactory.decodeResource(context.getResources(), R.mipmap.play_page_default_cover);
        templateWidth=bitmapTemplate.getWidth();
        templateHeight = bitmapTemplate.getHeight();
        mRadius = Math.min(templateHeight, templateWidth);
    }

    public static CircleBitmapUtils getInstence(Context context){
        if (circleBitmap == null) {
            circleBitmap = new CircleBitmapUtils(context);
        }
        return circleBitmap;
    }

    /**
     * @param toTransform 要进行缩放的Bitmap
     * @return  缩放比例值
     */
    public float getScale( Bitmap toTransform) {
        int width = toTransform.getWidth();
        int height = toTransform.getHeight();
        int radius = Math.min(width, height);
        float scale=1.0f;
        if(radius>=mRadius){
            scale = radius* 1.0f / mRadius ;
        }else{
            scale = mRadius * 1.0f/ radius ;
        }
        return scale;
    }
}
