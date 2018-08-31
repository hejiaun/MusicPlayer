package com.example.administrator.musicplayer.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.example.administrator.musicplayer.application.AppCache;

/**
 * Created by Administrator on 2017/6/24.
 */

public class DownloadReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        AppCache.getInstence().getLocalMusicFragment().scanMusic(AppCache.getInstence().getMainActivity());
        Toast.makeText(context, "歌曲下载完成", Toast.LENGTH_SHORT).show();
        context.unregisterReceiver(this);
    }
}
