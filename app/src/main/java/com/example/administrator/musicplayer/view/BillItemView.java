package com.example.administrator.musicplayer.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.administrator.musicplayer.R;
import com.example.administrator.musicplayer.utils.ContentValuseUtils;

/**
 * Created by Administrator on 2017/6/26.
 */

public class BillItemView extends LinearLayout {

    private TextView tv_first;
    private TextView tv_second;
    private TextView tv_third;
    private ImageView iv_poster;

    public BillItemView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        View view = View.inflate(context, R.layout.item_online, this);
        tv_first = (TextView) view.findViewById(R.id.tv_first);
        tv_second = (TextView) view.findViewById(R.id.tv_second);
        tv_third = (TextView) view.findViewById(R.id.tv_third);
        iv_poster = (ImageView) view.findViewById(R.id.iv_poster);
    }

    public BillItemView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BillItemView(Context context) {
        this(context, null, 0);
    }

    public void setContent(String first, String second, String third, String poster) {
        if(first.equals("")){
            tv_first.setVisibility(View.GONE);
        }else{
            tv_first.setText(first);
        }
        if(second.equals("")){
            tv_second.setVisibility(View.GONE);
        }else{
            tv_second.setText(second);
        }
        if(third.equals("")){
            tv_third.setVisibility(View.GONE);
        }else{
            tv_third.setText(third);
        }

        Glide.with(getContext()).load(poster).into(iv_poster);
    }
}
