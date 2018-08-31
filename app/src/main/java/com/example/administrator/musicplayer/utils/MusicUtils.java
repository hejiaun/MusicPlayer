package com.example.administrator.musicplayer.utils;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;

import com.example.administrator.musicplayer.bean.Music;
import com.example.administrator.musicplayer.bean.Song;
import com.example.administrator.musicplayer.bean.SongList;

import java.util.ArrayList;

/**
 * Created by Administrator on 2017/5/21.
 */

public class MusicUtils {
    static MusicUtils musicUtils = null;

    private MusicUtils() {

    }

    public static MusicUtils getInstence() {
        if (musicUtils == null)
            musicUtils = new MusicUtils();
        return musicUtils;
    }

    /**
     * 扫描所有音乐
     */
    public ArrayList<Music> scanMusic(Context context) {
        ArrayList<Music> musics = new ArrayList<>();
        //查询系统数据库中的音乐
        Cursor cursor = context.getContentResolver().query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, null, null, null, MediaStore.Audio.Media.DEFAULT_SORT_ORDER);
        if (cursor != null) {
            while (cursor.moveToNext()) {
                String title = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.TITLE));
                String singer = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST));
                String album = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM));
                String path = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DATA));
                String fileName = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DISPLAY_NAME));
                long duration = cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.Media.DURATION));
                long size = cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.Media.SIZE));
                long id = cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.Media._ID));
                long albumId = cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM_ID));
                String imagePath = getImagePath(albumId, context);
                if (singer.contains("unknow")) {
                    singer = "未知歌手";
                }
                //封装到实体类
                Music music = new Music();
                music.setAlbum(album);
                music.setDuration(duration);
                music.setFileName(fileName);
                music.setPath(path);
                music.setSinger(singer);
                music.setTitle(title);
                music.setSize(size);
                music.setImagePath(imagePath);
                musics.add(music);
            }
            cursor.close();
        }
        return musics;
    }

    /**
     * 获取音乐封面
     *
     * @param id      音乐id
     * @param context 上下文
     * @return
     */
    private String getImagePath(long id, Context context) {
        String result = null;
        String uri = "content://media/external/audio/albums";
        String[] projection = new String[]{"album_art"};
        Cursor cur = context.getContentResolver().query(Uri.parse(uri + "/" + Long.toString(id)), projection, null, null, null);
        if (cur.getCount() > 0 && cur.getColumnCount() > 0) {
            cur.moveToNext();
            result = cur.getString(0);
        }
        if (result == null) {
            return "00";
        }
        cur.close();
        return result;
    }


    public Music Song2Music(Song song) {
        Music music = new Music();
        Song.DataBean.SongListBean songListBean = song.getData().getSongList().get(0);
        music.setTitle(songListBean.getSongName());
        music.setSinger(songListBean.getArtistName());
        music.setDuration(songListBean.getTime());
        music.setPath(songListBean.getSongLink());
        music.setSize(songListBean.getSize());
        if (!songListBean.getSongPicBig().equals("")) {
            music.setImagePath(songListBean.getSongPicBig());
        } else if (!songListBean.getSongPicSmall().equals("")) {
            music.setImagePath(songListBean.getSongPicSmall());
        } else {
            music.setImagePath(songListBean.getSongPicRadio());
        }
        return music;
    }



}
