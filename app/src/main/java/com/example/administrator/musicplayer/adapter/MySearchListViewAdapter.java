package com.example.administrator.musicplayer.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.administrator.musicplayer.R;
import com.example.administrator.musicplayer.bean.SearchSongList;
import com.example.administrator.musicplayer.utils.HttpUtils;

import java.util.ArrayList;


/**
 * Created by Administrator on 2017/6/21.
 */

public class MySearchListViewAdapter extends BaseAdapter {
    ArrayList<SearchSongList.SongBean> songs = new ArrayList<>();
    Context context = null;

    public MySearchListViewAdapter(Context context) {
        this.context = context;
    }

    public void setSongs(ArrayList<SearchSongList.SongBean> songs) {
        this.songs.clear();
        this.songs.addAll(songs);
    }

    @Override
    public int getCount() {
        return songs.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (songs == null) {
            return convertView;
        }
        final SearchSongList.SongBean songBean = songs.get(position);
        MyViewHolder viewHolder = null;
        if (convertView == null) {
            convertView = View.inflate(context, R.layout.item_search, null);
            viewHolder = new MyViewHolder();
            viewHolder.iv_more = (ImageView) convertView.findViewById(R.id.iv_more);
            viewHolder.tv_singer = (TextView) convertView.findViewById(R.id.tv_singer);
            viewHolder.tv_songName = (TextView) convertView.findViewById(R.id.tv_songName);
            convertView.setTag(viewHolder);
        }
        viewHolder = (MyViewHolder) convertView.getTag();
        viewHolder.iv_more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(context);
                View view = View.inflate(context, R.layout.dialog_search_item, null);
                builder.setView(view);
                final AlertDialog dialog = builder.create();
                TextView tv_title = (TextView) view.findViewById(R.id.tv_title);
                tv_title.setText(songBean.getSongname());
                TextView tv_share = (TextView) view.findViewById(R.id.tv_share);
                TextView tv_download = (TextView) view.findViewById(R.id.tv_download);
                View.OnClickListener onClickListener = new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        switch (v.getId()) {
                            case R.id.tv_share:
                                break;
                            case R.id.tv_download:
                                //下载
                                //开启服务
                                HttpUtils.getInstence().downSong(context, songBean.getSongid());
                                dialog.dismiss();
                                break;
                        }
                    }
                };
                tv_download.setOnClickListener(onClickListener);
                tv_share.setOnClickListener(onClickListener);
                dialog.show();
            }
        });
        viewHolder.tv_singer.setText(songBean.getArtistname());
        viewHolder.tv_songName.setText(songBean.getSongname());
        return convertView;
    }


    class MyViewHolder {
        TextView tv_songName;
        TextView tv_singer;
        ImageView iv_more;
    }

}
