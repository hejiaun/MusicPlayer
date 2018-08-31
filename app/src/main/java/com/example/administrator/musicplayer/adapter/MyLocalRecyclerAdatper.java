package com.example.administrator.musicplayer.adapter;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.administrator.musicplayer.R;
import com.example.administrator.musicplayer.application.AppCache;
import com.example.administrator.musicplayer.bean.Music;

import java.util.ArrayList;


/**
 * Created by Administrator on 2017/5/20.
 */

public class MyLocalRecyclerAdatper extends RecyclerView.Adapter<MyLocalRecyclerAdatper.MyViewHolder> {
    Context context;
    ArrayList<Music> musics;
    OnItemClickListener onItemClickListener;
    OnMoreClickListener moreClickListener;
    LinearLayoutManager linearLayoutManager;
    int redPosition = -100;

    public MyLocalRecyclerAdatper(Context context, LinearLayoutManager linearLayoutManager) {
        this.linearLayoutManager = linearLayoutManager;
        this.musics = AppCache.getInstence().getMusic();
        this.context = context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_music_list, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        final Music music = musics.get(position);
        //设置选择颜色条
        if (music.isSelect()) {
            holder.view_select.setVisibility(View.VISIBLE);
        } else {
            holder.view_select.setVisibility(View.INVISIBLE);
        }
        holder.tv_singer.setText(music.getSinger());
        holder.tv_songName.setText(music.getTitle());
        holder.iv_more.setOnClickListener(new MyMoreOnclickListener(music));
        String imagePath = music.getImagePath();
        //设置音乐封面
        if (imagePath.equals("00")) {
            holder.iv_song.setImageResource(R.mipmap.default_cover);
        } else {
            Glide.with(context).load(imagePath).into(holder.iv_song);
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //回调接口的点击方法
                onItemClickListener.onItemClick(position);
                if (redPosition == -100) {//如果第一次点击
                    //刷新保存的被选中的item的position
                    redPosition = position;
                } else {            //不是第一次点击
                    if (redPosition != position) {//点击不同的item
                        //判断更新item是否在可视范围,是则跟新item，否则不跟新
                        int firstPosition = linearLayoutManager.findFirstVisibleItemPosition();
                        int lastPosition = linearLayoutManager.findLastVisibleItemPosition();
                        //当前点的item为选中
                        holder.view_select.setVisibility(View.VISIBLE);
                        if (redPosition >= firstPosition && redPosition <= lastPosition) {
                            linearLayoutManager.findViewByPosition(redPosition).findViewById(R.id.view_select).setVisibility(View.INVISIBLE);
                        }
                        musics.get(redPosition).setSelect(false);
                        redPosition = position;
                    }
                }
                //选中当前点击item
                holder.view_select.setVisibility(View.VISIBLE);
                music.setSelect(true);
            }
        });

    }

    @Override
    public int getItemCount() {
        return musics.size();
    }

    public void setRed(int red) {
        redPosition = red;
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView iv_song;
        ImageView iv_more;
        TextView tv_singer;
        TextView tv_songName;
        View view_select;

        public MyViewHolder(View itemView) {
            super(itemView);
            iv_more = (ImageView) itemView.findViewById(R.id.iv_more);
            view_select = itemView.findViewById(R.id.view_select);
            iv_song = (ImageView) itemView.findViewById(R.id.iv_song);
            tv_singer = (TextView) itemView.findViewById(R.id.tv_singer);
            tv_songName = (TextView) itemView.findViewById(R.id.tv_songName);
        }
    }

    class MyMoreOnclickListener implements View.OnClickListener {
        Music music;
        public MyMoreOnclickListener(Music music) {
            this.music = music;
        }
        @Override
        public void onClick(View v) {
            //调用回调方法
            moreClickListener.onClick(music);
        }
    }

    /**
     * 点击事件监听接口
     */
    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public interface OnMoreClickListener {
        void onClick(Music music);
    }

    /**
     * 设置item点击事件
     *
     * @param onItemClickListener
     */
    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public void setOnMoreClickListener(OnMoreClickListener moreClickListener) {
        this.moreClickListener = moreClickListener;
    }

}
