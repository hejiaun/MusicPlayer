package com.example.administrator.musicplayer.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.administrator.musicplayer.R;
import com.example.administrator.musicplayer.bean.SongList;
import com.example.administrator.musicplayer.utils.HttpUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2017/6/27.
 */

public class MySongListAdapter extends BaseAdapter {
    List<SongList.SongListBean> song_list = null;
    private Context context;

    public MySongListAdapter(Context context, SongList.BillboardBean billboard, List<SongList.SongListBean> song_list) {
        this.song_list = song_list;
        this.context = context;
    }

    @Override
    public int getCount() {
        return song_list.size();
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
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolderSong viewHolderSong;
        if (convertView == null) {
            convertView = View.inflate(context, R.layout.item_music_list, null);
            viewHolderSong = new ViewHolderSong(convertView);
            convertView.setTag(viewHolderSong);
        } else {
            viewHolderSong = (ViewHolderSong) convertView.getTag();
        }
        final SongList.SongListBean songListBean = song_list.get(position);
        Glide.with(context).load(songListBean.getPic_small()).into(viewHolderSong.ivSong);
        viewHolderSong.tvSinger.setText(songListBean.getAuthor());
        viewHolderSong.tvSongName.setText(songListBean.getTitle());
        viewHolderSong.ivMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(context);
                View view = View.inflate(context, R.layout.dialog_search_item, null);
                builder.setView(view);
                final AlertDialog dialog = builder.create();
                TextView tv_title = (TextView) view.findViewById(R.id.tv_title);
                TextView tv_share = (TextView) view.findViewById(R.id.tv_share);
                TextView tv_download = (TextView) view.findViewById(R.id.tv_download);
                tv_title.setText(songListBean.getTitle());
                View.OnClickListener onClickListener = new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        switch (v.getId()) {
                            case R.id.tv_share:
                                break;
                            case R.id.tv_download:
                                //下载
                                HttpUtils.getInstence().downSong(context, songListBean.getSong_id());
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

        return convertView;
    }

    public void addMusic(List<SongList.SongListBean> song_list) {
        this.song_list.addAll(song_list);
        notifyDataSetChanged();
    }

    public int getDataSize() {
        return song_list.size();
    }

    class ViewHolderSong {
        @BindView(R.id.view_select)
        View viewSelect;
        @BindView(R.id.iv_song)
        ImageView ivSong;
        @BindView(R.id.tv_songName)
        TextView tvSongName;
        @BindView(R.id.tv_singer)
        TextView tvSinger;
        @BindView(R.id.iv_more)
        ImageView ivMore;

        ViewHolderSong(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
