package com.example.administrator.musicplayer.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.administrator.musicplayer.R;
import com.example.administrator.musicplayer.utils.ContentValuseUtils;

/**
 * Created by Administrator on 2017/6/26.
 */

public class BillTitleView extends LinearLayout {

    private TextView tv_title;

    public BillTitleView(Context context) {
        this(context, null, 0);
    }

    public BillTitleView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BillTitleView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        View view = View.inflate(context, R.layout.item_online_title, this);
        if (attrs != null) {
            tv_title = (TextView) view.findViewById(R.id.tv_title);
            //获取属性值
            String title = attrs.getAttributeValue(ContentValuseUtils.SPEACE_NAME, "title");
            tv_title.setText(title);
        }
    }

    public void setTitle(String title) {
        tv_title.setText(title);
    }
}
