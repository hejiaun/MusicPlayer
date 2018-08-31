package com.example.administrator.musicplayer;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.musicplayer.adapter.MySearchListViewAdapter;
import com.example.administrator.musicplayer.application.AppCache;
import com.example.administrator.musicplayer.bean.Music;
import com.example.administrator.musicplayer.bean.SearchSongList;
import com.example.administrator.musicplayer.bean.Song;
import com.example.administrator.musicplayer.utils.ContentValuseUtils;
import com.example.administrator.musicplayer.utils.MusicUtils;
import com.example.administrator.musicplayer.utils.NetUtils;
import com.example.administrator.musicplayer.utils.HttpUtils;


/**
 * Created by Administrator on 2017/5/18.
 */

public class SearchActivity extends AppCompatActivity implements SearchView.OnQueryTextListener {
    private Toolbar tb_search;
    private ListView lv_search;
    private ProgressDialog progressDialog;
    private SearchView searchView;
    private TextView tv_fail;
    private AppCache appCache;
    private MusicUtils musicUtils;
    private MySearchListViewAdapter adapter;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case ContentValuseUtils.SEARCH_FINISH:
                    searchFinish();
                    break;
                case ContentValuseUtils.PLAY_NET_MUSIC://搜索完成
                    Song netSong = appCache.getNetSong();
                    Music music = musicUtils.Song2Music(netSong);
                    //播放
                    appCache.getLocalMusicFragment().playNetMusic(music);
                    //获取封面
                    searchHttpUtils.getImageByURL(music.getImagePath(), handler);
                    break;
                case ContentValuseUtils.GET_IMAGE_FINISH://获取封面完成
                    if (appCache.getPlayMusicFrament() != null)
                        appCache.getPlayMusicFrament().reflashFace();
                    progressDialog.dismiss();
                    break;
            }
        }
    };
    private HttpUtils searchHttpUtils;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        initUI();
        initConfig();
        initData();
    }

    private void initConfig() {
        setSupportActionBar(tb_search);
        //设置返回图标
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void initData() {
        searchHttpUtils = HttpUtils.getInstence();
        musicUtils = MusicUtils.getInstence();
        appCache = AppCache.getInstence();
        adapter = new MySearchListViewAdapter(SearchActivity.this);
        lv_search.setAdapter(adapter);
        lv_search.setOnItemClickListener(new MyListViewItemClickListener());
    }

    private void initUI() {
        tb_search = (Toolbar) findViewById(R.id.tb_search);
        lv_search = (ListView) findViewById(R.id.lv_search);
        tv_fail = (TextView) findViewById(R.id.tv_fail);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //加载菜单文件
        getMenuInflater().inflate(R.menu.menu_search, menu);
        //获取到menu文件中的searchView
        searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
        //配置searchView
        //设置searchView的最大宽度
        //searchView.setMaxWidth(Integer.MAX_VALUE);
        //设置searchView当前为展开状态
        searchView.onActionViewExpanded();
        //设置searchView的hint
        searchView.setQueryHint("歌名、歌手");
        //设置searchView的监听
        searchView.setOnQueryTextListener(this);
        //设置searchView确定按钮是否可用
        searchView.setSubmitButtonEnabled(true);
        //设置右边图标
        ImageView searchBtn = (ImageView) searchView.findViewById(android.support.v7.appcompat.R.id.search_go_btn);
        searchBtn.setImageResource(R.mipmap.ic_menu_search);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home)
            finish();
        return super.onOptionsItemSelected(item);
    }

    /**
     * 显示搜索对话框
     */
    private void showSearcingDialog() {
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(SearchActivity.this);
            progressDialog.setTitle("搜索中....");
        }
        progressDialog.show();
    }

    /**
     * 搜索完成
     */
    private void searchFinish() {
        adapter.notifyDataSetChanged();
        progressDialog.dismiss();
        searchView.clearFocus();
        if (appCache.getSearchSongs().size() > 0) {
            lv_search.setVisibility(View.VISIBLE);
            tv_fail.setVisibility(View.INVISIBLE);
        } else {
            lv_search.setVisibility(View.INVISIBLE);
            tv_fail.setText("查询无结果");
            tv_fail.setVisibility(View.VISIBLE);
        }
    }


    @Override
    public boolean onQueryTextSubmit(String query) {
        search(query);
        return false;
    }

    /**
     * 点击查询
     *
     * @param query
     */
    private void search(String query) {
        //判断网络是否可用
        boolean netState = NetUtils.getInstence().getNetState(SearchActivity.this);
        if (netState) {
            showSearcingDialog();
            //搜索在线歌曲
            HttpUtils.getInstence().getSearchSong(handler, adapter, query);
            if (adapter == null) {
                adapter = new MySearchListViewAdapter(SearchActivity.this);
            }
        } else {
            lv_search.setVisibility(View.INVISIBLE);
            tv_fail.setText("加载失败，请检查网络后重试");
            tv_fail.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        return false;
    }


    class MyListViewItemClickListener implements AdapterView.OnItemClickListener {
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            if (!NetUtils.getInstence().getNetState(SearchActivity.this)) {//如果网络不可以用
                Toast.makeText(SearchActivity.this, "网络开小差啦", Toast.LENGTH_LONG).show();
                return;
            }
            SearchSongList.SongBean songBean = appCache.getSearchSongs().get(position);
            //获取歌曲id
            String songid = songBean.getSongid();
            //根据id查询歌曲
            searchHttpUtils.getSongByID(songid, handler);
            showSearcingDialog();
        }
    }
}
