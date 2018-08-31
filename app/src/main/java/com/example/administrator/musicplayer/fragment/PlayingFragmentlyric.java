package com.example.administrator.musicplayer.fragment;

import android.content.Context;
import android.media.AudioManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;

import com.example.administrator.musicplayer.R;

/**
 * Created by Administrator on 2017/5/24.
 */

public class PlayingFragmentlyric extends Fragment {
    View view;
    private SeekBar sb;
    private AudioManager mAudioManager;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        view = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_playing_lyric, null);
        initUI();
        initData();
        initListener();
    }

    private void initData() {
        mAudioManager = (AudioManager) getActivity().getSystemService(Context.AUDIO_SERVICE);
        sb.setMax(mAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC));
        sb.setProgress(mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC));
    }

    private void initListener() {
        sb.setOnSeekBarChangeListener(new MyOnSeekBarChangeListener());
    }

    private void initUI() {
        sb = (SeekBar) view.findViewById(R.id.sb);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return view;
    }

    class MyOnSeekBarChangeListener implements SeekBar.OnSeekBarChangeListener {
        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC,progress,AudioManager.FLAG_REMOVE_SOUND_AND_VIBRATE);
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {

        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {

        }
    }
}
