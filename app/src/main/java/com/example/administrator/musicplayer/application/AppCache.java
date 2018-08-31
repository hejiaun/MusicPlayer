package com.example.administrator.musicplayer.application;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;

import com.example.administrator.musicplayer.R;
import com.example.administrator.musicplayer.bean.Music;
import com.example.administrator.musicplayer.bean.SearchSongList;
import com.example.administrator.musicplayer.bean.Song;
import com.example.administrator.musicplayer.fragment.LocalMusicFragment;
import com.example.administrator.musicplayer.fragment.OnlineMusicFragment;
import com.example.administrator.musicplayer.fragment.PlayMusicFrament;
import com.example.administrator.musicplayer.fragment.PlayingFragmentFace;
import com.example.administrator.musicplayer.fragment.PlayingFragmentlyric;
import com.example.administrator.musicplayer.service.MusicService;

import java.util.ArrayList;

/**
 * Created by Administrator on 2017/5/31.
 */

public class AppCache {

    private Context appContext;

    public Activity getMainActivity() {
        return mainActivity;
    }

    public void setMainActivity(Activity mainActivity) {
        this.mainActivity = mainActivity;
    }

    private Activity mainActivity;
    public Context getAppContext() {
        return appContext;
    }

    public void setAppContext(Context appContext) {
        this.appContext = appContext;
    }

    private Bitmap netFace;
    private int[] playMode = new int[]{R.drawable.selector_play_single, R.drawable.selector_play_loop, R.drawable.selector_play_shuffle};
    private boolean playing = false;
    private ArrayList<Music> musics;
    private ArrayList<SearchSongList.SongBean> searchSongs = new ArrayList<>();
    private int currentIndex = -1;
    private static AppCache appCache = null;
    private LocalMusicFragment localMusicFragment;
    private OnlineMusicFragment onlineMusicFragment;
    private PlayingFragmentFace playingFragmentFace;
    private PlayingFragmentlyric playingFragmentlyric;
    private PlayMusicFrament playMusicFrament;
    private MusicService.MyBinder myBinder;
    private boolean onNet = false;

    public boolean isOnNet() {
        return onNet;
    }

    public void setOnNet(boolean onNet) {
        this.onNet = onNet;
    }

    public Song getNetSong() {
        return netSong;
    }

    public void setNetSong(Song netSong) {
        this.netSong = netSong;
    }

    private Song netSong;

    public Bitmap getNetFace() {
        return netFace;
    }

    public void setNetFace(Bitmap netFace) {
        this.netFace = netFace;
    }

    public int getMode(int i) {
        if (i < 3 && i >= 0) {
            return playMode[i];
        }
        return 1;
    }

    public ArrayList<SearchSongList.SongBean> getSearchSongs() {
        return searchSongs;
    }

    public void setSearchSongs(ArrayList<SearchSongList.SongBean> searchSongs) {
        this.searchSongs = searchSongs;
    }

    public MusicService.MyBinder getMyBinder() {
        return myBinder;
    }

    public void setMyBinder(MusicService.MyBinder myBinder) {
        this.myBinder = myBinder;
    }

    public LocalMusicFragment getLocalMusicFragment() {
        return localMusicFragment;
    }

    public void setLocalMusicFragment(LocalMusicFragment localMusicFragment) {
        this.localMusicFragment = localMusicFragment;
    }

    public OnlineMusicFragment getOnlineMusicFragment() {
        return onlineMusicFragment;
    }

    public void setOnlineMusicFragment(OnlineMusicFragment onlineMusicFragment) {
        this.onlineMusicFragment = onlineMusicFragment;
    }

    public PlayingFragmentFace getPlayingFragmentFace() {
        return playingFragmentFace;
    }

    public void setPlayingFragmentFace(PlayingFragmentFace playingFragmentFace) {
        this.playingFragmentFace = playingFragmentFace;
    }

    public PlayingFragmentlyric getPlayingFragmentlyric() {
        return playingFragmentlyric;
    }

    public void setPlayingFragmentlyric(PlayingFragmentlyric playingFragmentlyric) {
        this.playingFragmentlyric = playingFragmentlyric;
    }

    public PlayMusicFrament getPlayMusicFrament() {
        return playMusicFrament;
    }

    public void setPlayMusicFrament(PlayMusicFrament playMusicFrament) {
        this.playMusicFrament = playMusicFrament;
    }

    public void deleteMusic(String path) {
        for (Music music : musics) {
            if (path.equals(music.getPath())) {
                musics.remove(music);
                return;
            }
        }

    }

    private AppCache() {

    }

    public static AppCache getInstence() {
        if (appCache == null) {
            appCache = new AppCache();
        }
        return appCache;
    }

    public ArrayList<Music> getMusic() {
        return musics;
    }

    public void setMusics(ArrayList<Music> musics) {
        if(this.musics==null){
            this.musics = musics;
        }else {
            this.musics.clear();
            this.musics.addAll(musics);
        }
    }

    public int getCurrentIndex() {
        return currentIndex;
    }

    public void setCurrentIndex(int indext) {
        currentIndex = indext;
    }

    public Music getCurrentMusic() {
        if (currentIndex == -1) {
            return null;
        }
        return getMusic().get(getCurrentIndex());
    }

    public boolean isPlaying() {
        return playing;
    }

    public void setPlaying(boolean isPlaying) {
        this.playing = isPlaying;
    }
}
