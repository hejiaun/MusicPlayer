package com.example.administrator.musicplayer.service;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.Log;

import com.example.administrator.musicplayer.application.AppCache;
import com.example.administrator.musicplayer.bean.Music;
import com.example.administrator.musicplayer.utils.ContentValuseUtils;
import com.example.administrator.musicplayer.utils.SpUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Administrator on 2017/5/21.
 */

public class MusicService extends Service {
    private MediaPlayer mediaPlayer;
    private Handler mHandler;
    private String mPath;
    private Timer timer;
    private TimerTask timerTask;
    private Runnable runnableProgress = new Runnable() {
        @Override
        public void run() {
            //总时长
            int tol = mediaPlayer.getDuration();
            //获取当前进程
            int currentProgress = mediaPlayer.getCurrentPosition();
            Message msg = Message.obtain();
            msg.what = ContentValuseUtils.PROGRESS;
            msg.arg2 = currentProgress;
            msg.arg1 = tol;
            mHandler.sendMessage(msg);
        }
    };
    private MyPreparedListener myPreparedListener;
    private MyMusicPlayCompletion myMusicPlayCompletion;

    @Override
    public void onCreate() {
        super.onCreate();
        myPreparedListener = new MyPreparedListener();
        myMusicPlayCompletion = new MyMusicPlayCompletion();

    }

    /**
     * 播放音乐
     */
    private void playMusic(Handler handler, String path) {
        mHandler = handler;
        try {
            if (path == mPath) {//如果点击的为同一首歌，直接继续播放
                mediaPlayer.start();
            } else {
                if (mediaPlayer != null)
                    mediaPlayer.release();
                //保存当前播放音乐的path
                mediaPlayer = new MediaPlayer();
                mediaPlayer.setOnPreparedListener(myPreparedListener);
                mPath = path;
                mediaPlayer.setDataSource(path);
                mediaPlayer.prepareAsync();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void removeRunnable() {
        if (mHandler != null) {
            mHandler.removeCallbacks(runnableProgress);
        }
    }

    private void setmPath(String path) {
        mPath = path;
    }

    /**
     * 显示播放进度
     */
    private void showProgress(final Handler handler) {
        //开启定时器，每秒钟获取一次进度
        timer = new Timer();
        timerTask = new TimerTask() {
            @Override
            public void run() {
                mHandler.post(runnableProgress);
            }
        };
        //开启定时器
        timer.schedule(timerTask, 500, 500);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return new MyBinder();
    }

    public class MyBinder extends Binder {
        /**
         * 播放音乐
         */
        public void bPlayMusic(Handler handler, String path) {
            playMusic(handler, path);
        }

        /**
         * 不再发进度消息
         */
        public void bRemoveRunnable() {
            removeRunnable();
        }

        /**
         * 暂停音乐播放
         */
        public void bPauseMusic() {
            mediaPlayer.pause();
        }

        public void bPullMusic(int progress) {
            mediaPlayer.seekTo(progress);
        }

        public void bSetPath(String path) {
            setmPath(path);
        }
    }


    /**
     * 监听资源perpare完成的类
     */
    class MyPreparedListener implements MediaPlayer.OnPreparedListener {
        @Override
        public void onPrepared(MediaPlayer mp) {
            if (mediaPlayer != null) {
                mediaPlayer.setOnCompletionListener(myMusicPlayCompletion);
                mediaPlayer.start();
            }
            showProgress(mHandler);
        }
    }

    class MyMusicPlayCompletion implements MediaPlayer.OnCompletionListener {
        @Override
        public void onCompletion(MediaPlayer mp) {
            timerTask.cancel();
            timer.cancel();
            mHandler.sendEmptyMessage(ContentValuseUtils.MUSIC_NEXT);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mediaPlayer != null) {
            mediaPlayer.release();
        }
    }
}
