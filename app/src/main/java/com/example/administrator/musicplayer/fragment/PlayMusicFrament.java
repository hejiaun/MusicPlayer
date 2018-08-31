package com.example.administrator.musicplayer.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.example.administrator.musicplayer.R;
import com.example.administrator.musicplayer.adapter.MyFragmentAdapter;
import com.example.administrator.musicplayer.application.AppCache;
import com.example.administrator.musicplayer.bean.Music;
import com.example.administrator.musicplayer.utils.ContentValuseUtils;
import com.example.administrator.musicplayer.utils.MusicUtils;
import com.example.administrator.musicplayer.utils.SpUtils;
import com.example.administrator.musicplayer.utils.FormatUtils;


import java.util.ArrayList;


/**
 * Created by Administrator on 2017/5/24.
 */

public class PlayMusicFrament extends Fragment implements View.OnClickListener {
    View view;
    private ViewPager vp_play;
    ArrayList<Fragment> arrayList = new ArrayList<>();
    private ImageView iv_back;
    private PlayingFragmentFace fragmentFace;
    private PlayingFragmentlyric fragmentlyric;
    private ImageView iv_play;
    private ImageView iv_next;
    private ImageView iv_prev;
    private TextView tv_songName;
    private TextView tv_singer;
    private SeekBar sb;
    private TextView tv_progressTime;
    private TextView tv_allTime;
    private int NEXT_KEY = 1;
    private int PRE_KEY = 0;
    private ImageView iv_bg;
    private ImageView iv_mode;
    private int mode;
    private SpUtils spUtils;
    private AppCache appCache;
    private FormatUtils timeFormatUtils;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        view = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_playmusic, null);
        initUI();
        initData();
        initConfig();
        initListener();
    }

    private void initListener() {
        iv_back.setOnClickListener(this);
        iv_play.setOnClickListener(this);
        iv_next.setOnClickListener(this);
        iv_prev.setOnClickListener(this);
        iv_mode.setOnClickListener(this);
        sb.setOnSeekBarChangeListener(new MySeekBarChangeListener());
    }

    private void initConfig() {
        vp_play.setAdapter(new MyFragmentAdapter(getActivity().getSupportFragmentManager(), arrayList));
    }

    private void initData() {
        //获取工具类对象
        spUtils = SpUtils.getInstence();
        timeFormatUtils = FormatUtils.getInstence();
        appCache = AppCache.getInstence();
        //获取播放模式键
        mode = spUtils.getInt(getActivity(), ContentValuseUtils.SP_MUSIC, ContentValuseUtils.IV_MODE);
        if (mode == -1) {
            spUtils.saveInt(getActivity(), ContentValuseUtils.SP_MUSIC, ContentValuseUtils.IV_MODE, 1);
            mode = 1;
        }
        //设置播放模式图片
        iv_mode.setImageResource(appCache.getMode(mode));
        spUtils.saveInt(getActivity(), ContentValuseUtils.SP_MUSIC, ContentValuseUtils.IV_MODE, mode);
        fragmentFace = new PlayingFragmentFace();
        fragmentlyric = new PlayingFragmentlyric();
        appCache.setPlayingFragmentFace(fragmentFace);
        appCache.setPlayingFragmentlyric(fragmentlyric);
        arrayList.add(fragmentFace);
        arrayList.add(fragmentlyric);
        reflashFace();
    }

    private void initUI() {
        tv_progressTime = (TextView) view.findViewById(R.id.tv_progressTime);
        tv_allTime = (TextView) view.findViewById(R.id.tv_allTime);
        iv_play = (ImageView) view.findViewById(R.id.iv_play);
        vp_play = (ViewPager) view.findViewById(R.id.vp_play);
        iv_back = (ImageView) view.findViewById(R.id.iv_back);
        iv_next = (ImageView) view.findViewById(R.id.iv_next);
        iv_prev = (ImageView) view.findViewById(R.id.iv_prev);
        tv_songName = (TextView) view.findViewById(R.id.tv_songName);
        tv_singer = (TextView) view.findViewById(R.id.tv_singer);
        sb = (SeekBar) view.findViewById(R.id.sb);
        iv_bg = (ImageView) view.findViewById(R.id.iv_play_bg);
        iv_mode = (ImageView) view.findViewById(R.id.iv_mode);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_mode:
                modeClick();
                break;
            case R.id.iv_back:
                getActivity().onBackPressed();
                break;
            case R.id.iv_play:
                if (appCache.getCurrentMusic() != null) {
                    Music music = appCache.getCurrentMusic();
                    ivPlayClick();
                    fragmentFace.startRotate();
                    appCache.getLocalMusicFragment().saveSpMusic(music.getPath(), appCache.getCurrentIndex());
                }
                break;
            case R.id.iv_next:
                preAndNextClick(NEXT_KEY);
                break;
            case R.id.iv_prev:
                preAndNextClick(PRE_KEY);
                break;
        }
    }

    /**
     * 点击播放模式图片
     */
    private void modeClick() {
        mode = mode + 1;
        if (mode > 2) {
            mode = 0;
        }
        iv_mode.setImageResource(appCache.getMode(mode));
        spUtils.saveInt(getActivity(), ContentValuseUtils.SP_MUSIC, ContentValuseUtils.IV_MODE, mode);
    }

    /**
     * 点击iv_play的逻辑
     */
    public void ivPlayClick() {
        if (appCache.isPlaying()) {
            iv_play.setImageResource(R.drawable.selector_play);
            appCache.getLocalMusicFragment().clickStatusPlay();
        } else {
            iv_play.setImageResource(R.drawable.selector_pause);
            appCache.getLocalMusicFragment().clickStatusPlay();
        }

    }


    public void preAndNextClick(int key) {
        if (appCache.getCurrentMusic() != null) {
            appCache.setOnNet(false);
            Music music = appCache.getCurrentMusic();
            if (key == PRE_KEY) {
                appCache.getLocalMusicFragment().musicPrePlay();
            } else if (key == NEXT_KEY) {
                appCache.getLocalMusicFragment().musicNextPlay();
            }
            reflashFace();
            iv_play.setImageResource(R.drawable.selector_pause);
            fragmentFace.resetRotate();
            fragmentFace.startRotate();
            appCache.getLocalMusicFragment().saveSpMusic(music.getPath(), appCache.getCurrentIndex());
            //保存音乐的路径
            spUtils.saveString(getActivity(), ContentValuseUtils.SP_MUSIC, ContentValuseUtils.MUSIC_PATH, music.getPath());
            //保存音乐的position
            spUtils.saveInt(getActivity(), ContentValuseUtils.SP_MUSIC, ContentValuseUtils.MUSIC_POSITION, appCache.getCurrentIndex());
        }
    }


    /**
     * 刷新封面
     */
    public void reflashFace() {
        if (fragmentFace != null) {
            fragmentFace.reflashFace();
        }
        Music music = null;
        String time = null;
        if (!appCache.isOnNet()) {
            music = appCache.getCurrentMusic();
            if (music == null) {
                time = "00:00";
                tv_singer.setText("未知歌手");
                tv_songName.setText("未知歌曲");
                tv_allTime.setText(time);
            } else {
                sb.setMax((int) music.getDuration());
                time = FormatUtils.getInstence().getFormatTime((int) music.getDuration());
            }
        } else {
            music = MusicUtils.getInstence().Song2Music(appCache.getNetSong());
            sb.setMax((int) music.getDuration() * 1000);
            time = FormatUtils.getInstence().getFormatTimeBySeconde((int) music.getDuration());
        }
        if (music != null) {
            tv_singer.setText(music.getSinger());
            tv_songName.setText(music.getTitle());
            tv_allTime.setText(time);
        }
    }


    /**
     * 设置播放图片
     */
    public void setPlayImage() {
        iv_play.setImageResource(R.drawable.selector_play);
    }

    /**
     * 设置暂停图片
     */
    public void setPauseImage() {
        iv_play.setImageResource(R.drawable.selector_pause);
        fragmentFace.startRotate();
    }

    public void setProgress(long time) {
        tv_progressTime.setText(timeFormatUtils.getFormatTime(time));
        sb.setProgress((int) time);
    }


    class MySeekBarChangeListener implements SeekBar.OnSeekBarChangeListener {

        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {

        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {
            if (appCache.isPlaying()) {
                appCache.getMyBinder().bPullMusic(seekBar.getProgress());
            } else {
                seekBar.setProgress(0);
            }
        }
    }

}
