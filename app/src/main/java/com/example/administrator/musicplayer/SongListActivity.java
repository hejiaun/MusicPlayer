package com.example.administrator.musicplayer;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.administrator.musicplayer.adapter.MySongListAdapter;
import com.example.administrator.musicplayer.application.AppCache;
import com.example.administrator.musicplayer.bean.Music;
import com.example.administrator.musicplayer.bean.Song;
import com.example.administrator.musicplayer.bean.SongList;
import com.example.administrator.musicplayer.utils.ContentValuseUtils;
import com.example.administrator.musicplayer.utils.HttpUtils;
import com.example.administrator.musicplayer.utils.MusicUtils;
import com.example.administrator.musicplayer.utils.NetUtils;

import java.security.Permission;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2017/6/26.
 */

public class SongListActivity extends AppCompatActivity {
    @BindView(R.id.tb)
    Toolbar tb;
    @BindView(R.id.iv_poster)
    ImageView ivPoster;
    @BindView(R.id.tv_updateTime)
    TextView tvUpdateTime;
    @BindView(R.id.tv_introduce)
    TextView tvIntroduce;
    @BindView(R.id.lv)
    ListView lv;
    private ProgressDialog progressDialog;
    private ActionBar supportActionBar;
    private String list;
    private int type;
    private int mFirstItem = 0;
    private MyListViewOnScrollListener myListViewOnScrollListener;
    private MyListViweOnItemListener myListViweOnItemListener;
    private MySongListAdapter mySongListAdapter = null;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case ContentValuseUtils.GET_SONGLIST:
                    SongList songList = (SongList) msg.obj;
                    if (mySongListAdapter == null) {
                        setListData(songList);
                        mySongListAdapter = new MySongListAdapter(SongListActivity.this, billboard, song_list);
                        lv.setAdapter(mySongListAdapter);
                    } else {
                        mySongListAdapter.addMusic(songList.getSong_list());
                        if (progressDialog.isShowing() && progressDialog != null) {
                            progressDialog.dismiss();
                        }
                    }
                    break;
                case ContentValuseUtils.PLAY_NET_MUSIC:
                    Song netSong = appCache.getNetSong();
                    Music music = musicUtils.Song2Music(netSong);
                    //播放
                    appCache.getLocalMusicFragment().playNetMusic(music);
                    //获取封面
                    httpUtils.getImageByURL(music.getImagePath(), handler);
                    if (progressDialog != null && progressDialog.isShowing())
                        progressDialog.dismiss();
                    break;
            }
        }

    };
    private AppCache appCache;
    private HttpUtils httpUtils;
    private SongList.BillboardBean billboard;
    private List<SongList.SongListBean> song_list;

    private MusicUtils musicUtils;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_song_list);
        ButterKnife.bind(this);
        initIntent();
        ininData();
        initConfig();
        getList();
    }

    private void ininData() {
        appCache = AppCache.getInstence();
        httpUtils = HttpUtils.getInstence();
        //获取权限
        musicUtils = MusicUtils.getInstence();
    }

    private void initIntent() {
        Bundle extras = getIntent().getExtras();
        list = "";
        list = extras.getString("list");
        type = extras.getInt("type");
    }

    private void initConfig() {
        setSupportActionBar(tb);
        supportActionBar = getSupportActionBar();
        supportActionBar.setDisplayHomeAsUpEnabled(true);
        supportActionBar.setTitle(list);
    }

    private void initBillboardInfo(String path, String introduct, String time) {
        Glide.with(SongListActivity.this).load(path).into(ivPoster);
        tvIntroduce.setText(introduct);
        tvUpdateTime.setText("更新时间为:" + time);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home)
            finish();
        return super.onOptionsItemSelected(item);
    }

    public void getList() {
        if (!NetUtils.getInstence().getNetState(SongListActivity.this)) {
            Toast.makeText(SongListActivity.this, "网络开小差了", Toast.LENGTH_SHORT).show();
        }
        if (type > 0) {
            httpUtils.getSongList(type, 10, 0, handler);
        }
    }

    private void setListData(SongList songList) {
        billboard = songList.getBillboard();
        song_list = songList.getSong_list();
        initBillboardInfo(billboard.getPic_s640(), billboard.getComment(), billboard.getUpdate_date());
        if (myListViweOnItemListener == null)
            myListViweOnItemListener = new MyListViweOnItemListener();
        lv.setOnItemClickListener(myListViweOnItemListener);
        if (myListViewOnScrollListener == null)
            myListViewOnScrollListener = new MyListViewOnScrollListener();
        lv.setOnScrollListener(myListViewOnScrollListener);
    }

    private void showPlayMusicDialog() {
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(SongListActivity.this);
            progressDialog.setTitle("加载中....");
        }
        progressDialog.show();
    }


    class MyListViweOnItemListener implements AdapterView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            if (!NetUtils.getInstence().getNetState(SongListActivity.this)) {//如果网络不可以用
                Toast.makeText(SongListActivity.this, "网络开小差啦", Toast.LENGTH_LONG).show();
                return;
            }
            if (song_list != null) {
                String song_id = song_list.get(position).getSong_id();
                httpUtils.getSongByID(song_id, handler);
                showPlayMusicDialog();
            }
        }
    }

    class MyListViewOnScrollListener implements AbsListView.OnScrollListener {
        @Override
        public void onScrollStateChanged(AbsListView view, int scrollState) {
        }

        @Override
        public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
            if (!NetUtils.getInstence().getNetState(SongListActivity.this)) {
                Toast.makeText(SongListActivity.this, "网络开小差了", Toast.LENGTH_SHORT).show();
                return;
            }
            if (firstVisibleItem > mFirstItem) {//拉到最后一条
                mFirstItem = firstVisibleItem;
                if (view.getLastVisiblePosition() == view.getCount() - 1) {
                    //判断滚动到底部
                    showPlayMusicDialog();
                    httpUtils.getSongList(type, 10, mySongListAdapter.getDataSize(), handler);
                }
            }
        }
    }


}
