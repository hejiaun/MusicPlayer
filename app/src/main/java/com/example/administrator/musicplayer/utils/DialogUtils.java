package com.example.administrator.musicplayer.utils;

import android.app.AlertDialog;
import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.example.administrator.musicplayer.R;
import com.example.administrator.musicplayer.bean.SongList;

/**
 * Created by Administrator on 2017/6/27.
 */

public class DialogUtils {
    private static DialogUtils dialogUtils;
    private DialogUtils(){

    }
    public static DialogUtils getInstence(){
        if (dialogUtils == null) {
            dialogUtils = new DialogUtils();
        }
        return dialogUtils;
    }

    public void getNetDialog(String title, Context context,View.OnClickListener onClickListener){
        final AlertDialog.Builder builder = new AlertDialog.Builder(context);
        View view = View.inflate(context, R.layout.dialog_search_item, null);
        builder.setView(view);
        final AlertDialog dialog = builder.create();
        TextView tv_title = (TextView) view.findViewById(R.id.tv_title);
        TextView tv_share = (TextView) view.findViewById(R.id.tv_share);
        TextView tv_download = (TextView) view.findViewById(R.id.tv_download);
        tv_title.setText(title);
        tv_download.setOnClickListener(onClickListener);
        tv_share.setOnClickListener(onClickListener);
        dialog.show();
    }

}
