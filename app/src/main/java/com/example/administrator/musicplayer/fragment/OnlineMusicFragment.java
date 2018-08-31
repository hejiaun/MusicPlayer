package com.example.administrator.musicplayer.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.administrator.musicplayer.R;
import com.example.administrator.musicplayer.SongListActivity;
import com.example.administrator.musicplayer.application.AppCache;
import com.example.administrator.musicplayer.bean.SongList;
import com.example.administrator.musicplayer.utils.ContentValuseUtils;
import com.example.administrator.musicplayer.utils.HttpUtils;
import com.example.administrator.musicplayer.utils.NetUtils;
import com.example.administrator.musicplayer.view.BillItemView;
import com.example.administrator.musicplayer.view.BillTitleView;

import java.util.List;

import butterknife.BindView;

/**
 * Created by Administrator on 2017/5/20.
 */

public class OnlineMusicFragment extends Fragment {

    @BindView(R.id.btv_main)
    BillTitleView btvMain;
    @BindView(R.id.biv_hotSong)
    BillItemView bivHotSong;
    @BindView(R.id.biv_newSong)
    BillItemView bivNewSong;
    @BindView(R.id.biv_chineseSong)
    BillItemView bivChineseSong;
    @BindView(R.id.biv_EuropeSong)
    BillItemView bivEuropeSong;
    @BindView(R.id.biv_filmSong)
    BillItemView bivFilmSong;
    @BindView(R.id.biv_loveSong)
    BillItemView bivLoveSong;
    @BindView(R.id.biv_netSong)
    BillItemView bivNetSong;
    @BindView(R.id.biv_oldSong)
    BillItemView bivOldSong;
    @BindView(R.id.biv_rockSong)
    BillItemView bivRockSong;
    @BindView(R.id.biv_ktvSong)
    BillItemView bivKtvSong;
    @BindView(R.id.biv_BillboardSong)
    BillItemView bivBillboardSong;
    @BindView(R.id.biv_hitChineseSong)
    BillItemView bivHitChineseSong;
    @BindView(R.id.btv_chichaSong)
    BillItemView btvChichaSong;
    butterknife.Unbinder unbinder;
    private View view;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case ContentValuseUtils.GET_SONGLIST:
                    SongList songList = (SongList) msg.obj;
                    setListItem(songList);
                    break;
            }
        }
    };
    private HttpUtils mHttpUtils;
    private MyOnClickListener mOnClickListener;
    private AppCache appCache;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (view == null) {
            view = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_online, null);
        }
        unbinder = butterknife.ButterKnife.bind(this, view);
        initUI();
        initData();
        initListener();
    }

    private void initListener() {
        bivChineseSong.setOnClickListener(mOnClickListener);
        bivFilmSong.setOnClickListener(mOnClickListener);
        bivOldSong.setOnClickListener(mOnClickListener);
        bivEuropeSong.setOnClickListener(mOnClickListener);
        bivBillboardSong.setOnClickListener(mOnClickListener);
        bivHitChineseSong.setOnClickListener(mOnClickListener);
        bivHotSong.setOnClickListener(mOnClickListener);
        bivKtvSong.setOnClickListener(mOnClickListener);
        bivLoveSong.setOnClickListener(mOnClickListener);
        bivNetSong.setOnClickListener(mOnClickListener);
        bivRockSong.setOnClickListener(mOnClickListener);
        bivNewSong.setOnClickListener(mOnClickListener);
        btvChichaSong.setOnClickListener(mOnClickListener);
    }

    private void initData() {
        appCache = AppCache.getInstence();
        mOnClickListener = new MyOnClickListener();
        mHttpUtils = HttpUtils.getInstence();
        getList();
    }

    private void initUI() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return view;
    }

    public void getList() {
        if (mHttpUtils != null) {
            mHttpUtils.getSongList(1, 3, 0, mHandler);//获取新歌榜
            mHttpUtils.getSongList(2, 3, 0, mHandler);//获取热歌榜
            mHttpUtils.getSongList(6, 3, 0, mHandler);//获取KTV歌曲榜
            mHttpUtils.getSongList(7, 3, 0, mHandler);//获取叱咤歌曲榜
            mHttpUtils.getSongList(8, 3, 0, mHandler);//获取Billboard榜
            mHttpUtils.getSongList(11, 3, 0, mHandler);//获取摇滚歌曲榜
            mHttpUtils.getSongList(18, 3, 0, mHandler);//获取Hit中文榜曲榜
            mHttpUtils.getSongList(20, 3, 0, mHandler);//获取华语金曲榜
            mHttpUtils.getSongList(21, 3, 0, mHandler);//获取欧美金曲榜
            mHttpUtils.getSongList(22, 3, 0, mHandler);//获取经典老歌榜
            mHttpUtils.getSongList(23, 3, 0, mHandler);//获取情歌对唱榜
            mHttpUtils.getSongList(24, 3, 0, mHandler);//获取影视金曲榜
            mHttpUtils.getSongList(25, 3, 0, mHandler);//获取网络歌曲榜
        }
        if (!NetUtils.getInstence().getNetState(getActivity())) {
            Toast.makeText(getActivity(), "请打开网络", Toast.LENGTH_SHORT).show();
            return;
        }
    }

    public void setListItem(SongList songList) {
        List<SongList.SongListBean> song_list = songList.getSong_list();
        int size = song_list.size();
        String titles[] = new String[]{"", "", ""};
        for (int i = 0; i < size; i++) {
            titles[i] = song_list.get(i).getTitle() + "—" +  song_list.get(i).getArtist_name();
        }
        String imagePath = songList.getBillboard().getPic_s260();
        switch (songList.getBillboard().getBillboard_type()) {
            case 1: //新歌榜
                bivNewSong.setContent(titles[0], titles[1], titles[2], imagePath);
                break;
            case 2: //热歌榜
                bivHotSong.setContent(titles[0], titles[1], titles[2], imagePath);
                break;
            case 6://KTV榜
                bivKtvSong.setContent(titles[0], titles[1], titles[2], imagePath);
                break;
            case 7: //叱咤歌曲榜
                btvChichaSong.setContent(titles[0], titles[1], titles[2], imagePath);
                break;
            case 8: //Billboard榜
                bivBillboardSong.setContent(titles[0], titles[1], titles[2], imagePath);
                break;
            case 11: //摇滚歌曲榜
                bivRockSong.setContent(titles[0], titles[1], titles[2], imagePath);
                break;
            case 18: //hit中文歌曲榜
                bivHitChineseSong.setContent(titles[0], titles[1], titles[2], imagePath);
                break;
            case 20: //华语金曲榜
                bivChineseSong.setContent(titles[0], titles[1], titles[2], imagePath);
                break;
            case 21: //欧美金曲榜
                bivEuropeSong.setContent(titles[0], titles[1], titles[2], imagePath);
                break;
            case 22: //经典老歌榜
                bivOldSong.setContent(titles[0], titles[1], titles[2], imagePath);
                break;
            case 23: //情歌对唱榜
                bivLoveSong.setContent(titles[0], titles[1], titles[2], imagePath);
                break;
            case 24: //影视金曲榜
                bivFilmSong.setContent(titles[0], titles[1], titles[2], imagePath);
                break;
            case 25: //网络歌曲榜
                bivNetSong.setContent(titles[0], titles[1], titles[2], imagePath);
                break;
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    class MyOnClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(appCache.getOnlineMusicFragment().getActivity(), SongListActivity.class);
            Bundle bundle = new Bundle();
            switch (v.getId()) {
                case R.id.biv_filmSong:
                    bundle.putInt("type", 24);
                    bundle.putString("list", "影视金曲榜");
                    intent.putExtras(bundle);
                    break;
                case R.id.biv_oldSong:
                    bundle.putInt("type", 22);
                    bundle.putString("list", "经典老歌榜");
                    intent.putExtras(bundle);
                    break;
                case R.id.biv_EuropeSong:
                    bundle.putInt("type", 21);
                    bundle.putString("list", "欧美金曲榜");
                    intent.putExtras(bundle);
                    break;
                case R.id.biv_BillboardSong:
                    bundle.putInt("type", 8);
                    bundle.putString("list", "BillBoard");
                    intent.putExtras(bundle);
                    break;
                case R.id.biv_hotSong:
                    bundle.putInt("type", 2);
                    bundle.putString("list", "百度热歌榜");
                    intent.putExtras(bundle);
                    break;
                case R.id.biv_ktvSong:
                    bundle.putInt("type", 6);
                    bundle.putString("list", "KTV热歌榜");
                    intent.putExtras(bundle);
                    break;
                case R.id.biv_hitChineseSong:
                    bundle.putInt("type", 18);
                    bundle.putString("list", "Hito中文榜");
                    intent.putExtras(bundle);
                    break;
                case R.id.biv_loveSong:
                    bundle.putInt("type", 23);
                    bundle.putString("list", "情感对唱榜");
                    intent.putExtras(bundle);
                    break;
                case R.id.biv_netSong:
                    bundle.putInt("type", 25);
                    bundle.putString("list", "网络歌曲榜");
                    intent.putExtras(bundle);
                    break;
                case R.id.biv_newSong:
                    bundle.putInt("type", 1);
                    bundle.putString("list", "百度新歌榜");
                    intent.putExtras(bundle);
                    break;
                case R.id.biv_rockSong:
                    bundle.putInt("type", 11);
                    bundle.putString("list", "摇滚金曲榜");
                    intent.putExtras(bundle);
                    break;
                case R.id.biv_chineseSong:
                    bundle.putInt("type", 20);
                    bundle.putString("list", "华语金曲榜");
                    intent.putExtras(bundle);
                    break;
                case R.id.btv_chichaSong:
                    bundle.putInt("type", 7);
                    bundle.putString("list", "叱咤金曲曲榜");
                    intent.putExtras(bundle);
                    break;
            }
            startActivity(intent);
        }
    }
}
