package com.example.administrator.musicplayer.utils;


import android.app.AlertDialog;
import android.app.DownloadManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.webkit.MimeTypeMap;
import android.widget.Toast;

import com.example.administrator.musicplayer.adapter.MySearchListViewAdapter;
import com.example.administrator.musicplayer.application.AppCache;
import com.example.administrator.musicplayer.bean.SearchSongList;
import com.example.administrator.musicplayer.bean.Song;
import com.example.administrator.musicplayer.bean.SongList;
import com.example.administrator.musicplayer.receiver.DownloadReceiver;
import com.google.gson.Gson;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by Administrator on 2017/6/21.
 */

public class HttpUtils {
    private static HttpUtils searchHttpUtils;

    private HttpUtils() {

    }

    public static HttpUtils getInstence() {
        if (searchHttpUtils == null) {
            searchHttpUtils = new HttpUtils();
        }
        return searchHttpUtils;
    }


    public void getSearchSong(final android.os.Handler handler, final MySearchListViewAdapter adapter, final String query) {
        new Thread() {
            private InputStream is;
            private HttpURLConnection urlConnection;

            @Override
            public void run() {
                try {
                    urlConnection = getConnection(ContentValuseUtils.BASE_URL + "ting" + "?method=" + ContentValuseUtils.METHOD_SEARCH_SONG_LIST + "&query=" + query);
                    if (urlConnection.getResponseCode() == 200) {
                        is = urlConnection.getInputStream();
                        String result = FormatUtils.getInstence().inputStream2String(is);
                        Gson gson = new Gson();
                        SearchSongList searchSongList = gson.fromJson(result, SearchSongList.class);
                        if (searchSongList.getError_code() == 22000) {
                            AppCache.getInstence().setSearchSongs((ArrayList<SearchSongList.SongBean>) searchSongList.getSong());
                            adapter.setSongs((ArrayList<SearchSongList.SongBean>) searchSongList.getSong());
                        } else {
                            AppCache.getInstence().getSearchSongs().clear();
                        }
                        handler.sendEmptyMessage(ContentValuseUtils.SEARCH_FINISH);
                    } else {
                        Log.d("getSearchSong", "请求失败");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    try {
                        is.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }.start();

    }


    /**
     * 根据SearchSong的id 获取song对象
     */
    public void getSongByID(final String id, final android.os.Handler handler) {
        new Thread() {
            private InputStream is;

            @Override
            public void run() {
                try {
                    HttpURLConnection con = getConnection(ContentValuseUtils.SEARCH_SONG_BY_ID + id);
                    if (con.getResponseCode() == 200) {//请求成功
                        is = con.getInputStream();
                        String result = FormatUtils.getInstence().inputStream2String(is);
                        Gson gson = new Gson();
                        Song song = gson.fromJson(result, Song.class);
                        if (song.getErrorCode() == 22000) {
                            AppCache.getInstence().setNetSong(song);
                            handler.sendEmptyMessage(ContentValuseUtils.PLAY_NET_MUSIC);
                        } else {
                            Log.d("getSongById", "非22000错误");
                        }
                    } else {
                        Log.d("getSongByID", "请求失败");
                    }
                } catch (Exception e) {

                } finally {
                    try {
                        if (is != null)
                            is.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }.start();
    }

    /**
     * 获取网络图片
     */
    public void getImageByURL(final String path, final android.os.Handler handler) {
        new Thread() {
            private Bitmap bitmap;

            @Override
            public void run() {
                try {
                    HttpURLConnection con = getConnection(path);
                    if (con.getResponseCode() == 200) {
                        InputStream inputStream = con.getInputStream();
                        bitmap = BitmapFactory.decodeStream(inputStream);
                        AppCache.getInstence().setNetFace(bitmap);
                    } else {
                        Log.d("getImageByUrl", "responCode" + con.getResponseCode() + "");
                    }
                    handler.sendEmptyMessage(ContentValuseUtils.GET_IMAGE_FINISH);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }


    /**
     * 下载歌曲
     *
     * @param context
     */
    public void downSong(final Context context, final String id) {
        NetUtils utils = NetUtils.getInstence();
        if (!utils.getNetState(context)) {
            Toast.makeText(context, "网络开小差了", Toast.LENGTH_SHORT).show();
            return;
        }
        final Thread thread = new Thread() {
            @Override
            public void run() {
                try {
                    //1.获取该歌曲下载链接（发送第一次请求）
                    HttpURLConnection con_1 = getConnection(ContentValuseUtils.SEARCH_SONG_BY_ID + id);
                    if (con_1.getResponseCode() == 200) {//请求成功
                        String result = FormatUtils.getInstence().inputStream2String(con_1.getInputStream());
                        Gson gson = new Gson();
                        Song song = gson.fromJson(result, Song.class);
                        if (song.getErrorCode() == 22000) {//根据链接获取下载歌曲
                            String link = song.getData().getSongList().get(0).getSongLink();
                            String format = song.getData().getSongList().get(0).getFormat();
                            String songName = song.getData().getSongList().get(0).getSongName();
                            String size = song.getData().getSongList().size() + "";
                            Uri url = Uri.parse(link);
                            DownloadManager.Request request = new DownloadManager.Request(url);
                            request.setTitle(songName);
                            request.setDescription("正在下载…");
                            request.setDestinationInExternalPublicDir(Environment.getDownloadCacheDirectory().getAbsolutePath(), songName + size + "." + format);
                            request.setMimeType(MimeTypeMap.getFileExtensionFromUrl(link));
                            request.allowScanningByMediaScanner();
                            request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_MOBILE | DownloadManager.Request.NETWORK_WIFI);
                            request.setAllowedOverRoaming(false);// 不允许漫游
                            DownloadManager downloadManager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
                            downloadManager.enqueue(request);
                            //监听下载完成
                            AppCache.getInstence().getAppContext().registerReceiver(new DownloadReceiver(), new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));
                        } else {
                            Log.d("download", "responCode" + con_1.getResponseCode() + "");
                        }
                    } else {
                        Log.d("getDownLink", "responCode" + con_1.getResponseCode() + "");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };

        if (utils.getMobileDataState(context)) {//如果使用移动数据
            final AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle("温馨提示");
            builder.setMessage("当前处于移动网络，下载将消耗多流量，是否继续下载");
            builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            builder.setPositiveButton("下载", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    thread.start();
                }
            });
            builder.show();
        } else {
            thread.start();
        }


    }

    public HttpURLConnection getConnection(String urlString) throws Exception {
        URL u1 = new URL(urlString);
        HttpURLConnection c1 = (HttpURLConnection) u1.openConnection();
        c1.setReadTimeout(8000);
        c1.setRequestMethod("GET");
        c1.setConnectTimeout(3000);
        c1.connect();
        return c1;
    }

    /**
     * 获取歌曲列表
     */
    public void getSongList(final int type, final int size, final int offset, final Handler handler) {
        new Thread() {
            @Override
            public void run() {
                try {
                    String path = ContentValuseUtils.BASE_URL + "ting?method=" + ContentValuseUtils.METHOD_GET_LIST + "&type=" + type + "&size=" + size + "&offset=" + offset;
                    HttpURLConnection connection = getConnection(path);
                    if (connection.getResponseCode() == 200) {//如果请求成功
                        String result = FormatUtils.getInstence().inputStream2String(connection.getInputStream());
                        Gson gson = new Gson();
                        SongList songList = gson.fromJson(result, SongList.class);
                        if (songList.getError_code() == 22000) {
                            Message message = Message.obtain();
                            message.obj = songList;
                            message.what = ContentValuseUtils.GET_SONGLIST;
                            handler.sendMessage(message);
                        }
                    } else {
                        Log.d("getSongList", " " + connection.getResponseCode());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }.start();
    }


}
