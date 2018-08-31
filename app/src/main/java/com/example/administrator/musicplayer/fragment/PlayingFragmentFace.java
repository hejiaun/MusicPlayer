package com.example.administrator.musicplayer.fragment;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.administrator.musicplayer.R;
import com.example.administrator.musicplayer.application.AppCache;
import com.example.administrator.musicplayer.view.AlbumCoverView;

/**
 * Created by Administrator on 2017/5/24.
 */

public class PlayingFragmentFace extends Fragment {
    View view;
    AlbumCoverView albumCoverView;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        view = View.inflate(getContext(), R.layout.fragment_playing_face, null);
        initUI();
    }

    private void initUI() {
        albumCoverView = (AlbumCoverView) view.findViewById(R.id.albumCoverView);
        if (AppCache.getInstence().isPlaying()) {
            AppCache.getInstence().getPlayMusicFrament().ivPlayClick();
            AppCache.getInstence().setPlaying(false);
            AppCache.getInstence().getPlayMusicFrament().ivPlayClick();
            startRotate();
        }
//        albumCoverView.changeFace();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return view;
    }

    /**
     * 刷新封面
     */
    public void reflashFace() {
        if (albumCoverView != null)
            albumCoverView.changeFace();
    }

    public void startRotate() {
        albumCoverView.roate();
    }

    public void resetRotate() {
        albumCoverView.resetRotate();
    }


}
