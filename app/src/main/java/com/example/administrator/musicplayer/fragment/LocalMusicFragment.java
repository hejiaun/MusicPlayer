package com.example.administrator.musicplayer.fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Service;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.administrator.musicplayer.R;
import com.example.administrator.musicplayer.adapter.MyLocalRecyclerAdatper;
import com.example.administrator.musicplayer.application.AppCache;
import com.example.administrator.musicplayer.bean.Music;
import com.example.administrator.musicplayer.service.MusicService;
import com.example.administrator.musicplayer.utils.ContentValuseUtils;
import com.example.administrator.musicplayer.utils.FormatUtils;
import com.example.administrator.musicplayer.utils.MusicUtils;
import com.example.administrator.musicplayer.utils.SpUtils;

import java.io.File;
import java.util.Random;


/**
 * Created by Administrator on 2017/5/20.
 */

public class LocalMusicFragment extends Fragment {
    private View view;
    private RecyclerView rcv;
    private MusicService.MyBinder myBinder;
    private LinearLayoutManager linearLayoutManager;
    private MyLocalRecyclerAdatper myRecyclerAdatper;
    private Intent intentMusicService;
    private ImageView iv_musicStatus;
    private ImageView iv_musicNext;
    private MyCon myCon;
    private TextView tv_songName;
    private TextView tv_singer;
    private ImageView iv_songFace;
    private ProgressBar progressBar;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case ContentValuseUtils.INIT_MUSIC:
                    if (myRecyclerAdatper == null) {
                        myRecyclerAdatper = new MyLocalRecyclerAdatper(getActivity(), linearLayoutManager);
                        myRecyclerAdatper.setOnItemClickListener(new MyRecyclerItemClickListener());
                        myRecyclerAdatper.setOnMoreClickListener(new MyMoreClickListener());
                        //配置适配器
                        rcv.setAdapter(myRecyclerAdatper);
                        //加载bottomBar信息
                        initBottomBar();
                    } else {
                        myRecyclerAdatper.notifyDataSetChanged();
                    }
                    break;
                case ContentValuseUtils.PROGRESS:
                    if (appCache.isPlaying() || appCache.isOnNet()) {
                        int tol = msg.arg1;
                        int progress = msg.arg2;
                        progressBar.setMax(tol);
                        progressBar.setProgress(progress);
                        if (appCache.getPlayMusicFrament() != null) {
                            appCache.getPlayMusicFrament().setProgress(progress);
                        }
                    }
                    break;
                case ContentValuseUtils.MUSIC_NEXT:
                    if (spUtils.getInt(getActivity(), ContentValuseUtils.SP_MUSIC, ContentValuseUtils.IV_MODE) == 0) {//如果是单曲循环
                        if (appCache.isOnNet()) {//如果在播放网络歌曲
                            myBinder.bSetPath("");
                            playNetMusic(MusicUtils.getInstence().Song2Music(appCache.getNetSong()));
                        } else {                 //如果不是播放网络歌曲
                            myBinder.bSetPath("");
                            appCache.setOnNet(false);
                            appCache.getCurrentMusic().setSelect(false);
                            playMusic(appCache.getCurrentMusic(), appCache.getCurrentMusic().getPath());
                        }
                    } else {
                        appCache.setOnNet(false);
                        musicNextPlay();


                        if (appCache.getPlayMusicFrament() != null) {
                            appCache.getPlayMusicFrament().reflashFace();
                        }
                    }
                    break;
            }
        }
    };
    private AppCache appCache;
    private SpUtils spUtils;
    private Music dialogMusic;
    private FormatUtils formatUtils;
    private AlertDialog moreDialog;
    private FragmentActivity fragmentActivity;



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
       fragmentActivity= getActivity();
        return view;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (view == null)
            view = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_local, null);
        initUI();
        initData();
        initConfig();
        initListener();
    }

    private void initListener() {
        //设置item点击事件监听
        //音乐暂停图片按钮监听
        iv_musicStatus.setOnClickListener(new MyOnClickListener());
        //下一首歌图片的按钮监听
        iv_musicNext.setOnClickListener(new MyOnClickListener());

    }

    private void initConfig() {
        rcv.setLayoutManager(linearLayoutManager);

        //开启服务
        getActivity().startService(intentMusicService);
        getActivity().bindService(intentMusicService, myCon, Service.BIND_AUTO_CREATE);
    }


    private void initData() {
        formatUtils = FormatUtils.getInstence();
        spUtils = SpUtils.getInstence();
        appCache = AppCache.getInstence();
        myCon = new MyCon();
        linearLayoutManager = new LinearLayoutManager(getActivity());
        intentMusicService = new Intent(getActivity(), MusicService.class);
    }

    private void initUI() {
        rcv = (RecyclerView) view.findViewById(R.id.rcv_local);
        //获取activity中的imageView
        iv_musicStatus = (ImageView) getActivity().findViewById(R.id.iv_musicStatus);
        iv_musicNext = (ImageView) getActivity().findViewById(R.id.iv_musicNext);
        tv_songName = (TextView) getActivity().findViewById(R.id.tv_songName);
        tv_singer = (TextView) getActivity().findViewById(R.id.tv_singer);
        iv_songFace = (ImageView) getActivity().findViewById(R.id.iv_songFace);
        progressBar = (ProgressBar) getActivity().findViewById(R.id.pb_playing);
    }


    /**
     * 扫描音乐
     */
    public void scanMusic(final Activity activity) {
            //扫描歌曲线程
            Thread scanThread = new Thread() {
                @Override
                public void run() {
                    AppCache.getInstence().setMusics(MusicUtils.getInstence().scanMusic(activity));
                    handler.sendEmptyMessage(ContentValuseUtils.INIT_MUSIC);
                }
            };
            scanThread.start();
//        }
    }

    /**
     * 播放音乐
     *
     * @param music
     */
    public void playMusic(Music music, String path) {
        appCache.setPlaying(true);
        if (!music.isSelect()) {//如果点击的不是同一首歌
            myBinder.bPlayMusic(handler, path);
        }
    }

    public void musicNextPlay() {
        int mode = spUtils.getInt(getActivity(), ContentValuseUtils.SP_MUSIC, ContentValuseUtils.IV_MODE);
        if (mode == 2) {//随机播放
            shufflePlay();
        } else {
            loopPlay();
        }
    }

    public void musicPrePlay() {
        int mode = spUtils.getInt(getActivity(), ContentValuseUtils.SP_MUSIC, ContentValuseUtils.IV_MODE);
        if (mode == 2) {//随机播放
            shufflePlay();
        } else {
            preLoopPlay();
        }
    }


    /**
     * 随机播放
     */
    private void shufflePlay() {
        setSelectMusic(false);
        Random random = new Random();
        int nowPosition = appCache.getCurrentIndex();
        int randomPosition = random.nextInt(appCache.getMusic().size() + 1);
        while (nowPosition == randomPosition) {
            randomPosition = random.nextInt(appCache.getMusic().size() + 1);
        }
        appCache.setCurrentIndex(randomPosition);
        Music music = appCache.getCurrentMusic();
        playMusic(music, music.getPath());
        iv_musicStatus.setImageResource(R.mipmap.ic_play_bar_btn_pause);
        setSelectMusic(true);
        myRecyclerAdatper.setRed(randomPosition);
        saveSpMusic(music.getPath(), appCache.getCurrentIndex());
        setBottomBarMessage(music.getTitle(), music.getSinger(), music.getImagePath());
    }


    public void saveSpMusic(String path, int index) {
        if (spUtils == null)
            spUtils = SpUtils.getInstence();
        //保存音乐的路径
        spUtils.saveString(getActivity(), ContentValuseUtils.SP_MUSIC, ContentValuseUtils.MUSIC_PATH, path);
        //保存音乐的position
        spUtils.saveInt(getActivity(), ContentValuseUtils.SP_MUSIC, ContentValuseUtils.MUSIC_POSITION, index);
    }


    /**
     * 顺序播放
     */
    private void loopPlay() {
        int mPosition = appCache.getCurrentIndex();
        setSelectMusic(false);
        if (mPosition >= 0) {
            if (mPosition == appCache.getMusic().size() - 1) {//如果是最后一首歌曲
                mPosition = -1;
            }
            appCache.setCurrentIndex(mPosition + 1);
            Music music = appCache.getCurrentMusic();
            playMusic(music, appCache.getCurrentMusic().getPath());
            setBottomBarMessage(music.getTitle(), music.getSinger(), music.getImagePath());
            iv_musicStatus.setImageResource(R.mipmap.ic_play_bar_btn_pause);
            setSelectMusic(true);
            //设置当前被选中的item
            myRecyclerAdatper.setRed(mPosition + 1);
            saveSpMusic(music.getPath(), appCache.getCurrentIndex());
        }
    }


    /**
     * 顺序播放上一首歌
     */
    private void preLoopPlay() {
        int mPosition = appCache.getCurrentIndex();
        setSelectMusic(false);
        if (mPosition < appCache.getMusic().size()) {
            if (mPosition == 0) {//如果是第一首歌曲
                mPosition = appCache.getMusic().size() - 1;
            }
            appCache.setCurrentIndex(mPosition - 1);
            Music music = appCache.getCurrentMusic();
            playMusic(music, appCache.getCurrentMusic().getPath());
            setBottomBarMessage(music.getTitle(), music.getSinger(), music.getImagePath());
            iv_musicStatus.setImageResource(R.mipmap.ic_play_bar_btn_pause);
            setSelectMusic(true);
            //设置当前被选中的item
            myRecyclerAdatper.setRed(mPosition - 1);
            saveSpMusic(music.getPath(), appCache.getCurrentIndex());
        }
    }


    /**
     * 设置bottomBar信息
     *
     * @param title
     * @param singer
     * @param imagePath
     */
    private void setBottomBarMessage(String title, String singer, String imagePath) {
        tv_songName.setText(title);
        tv_singer.setText(singer);
        if (imagePath.equals("00") || imagePath.equals("")) {//如果没有封面
            iv_songFace.setImageResource(R.mipmap.default_cover);
        } else {
            Glide.with(getActivity()).load(imagePath).into(iv_songFace);
        }
    }

    /**
     * 加载bottomBar信息
     */
    private void initBottomBar() {
        int anInt = SpUtils.getInstence().getInt(getActivity(), ContentValuseUtils.SP_MUSIC, ContentValuseUtils.MUSIC_POSITION);
        if (anInt < 0) {  //如果小于零，说明还没存储，返回小于0的默认值
            return;
        }
        appCache.setCurrentIndex(anInt);
        Music music = appCache.getMusic().get(anInt);
        setBottomBarMessage(music.getTitle(), music.getSinger(), music.getImagePath());
    }


    private void showMoreDialog(Music music) {
        dialogMusic = music;
        //获取UI
        View view = View.inflate(getActivity(), R.layout.dialog_more, null);
        TextView tv_share = (TextView) view.findViewById(R.id.tv_share);
        TextView tv_ring = (TextView) view.findViewById(R.id.tv_ring);
        TextView tv_songInfo = (TextView) view.findViewById(R.id.tv_songInfo);
        TextView tv_delete = (TextView) view.findViewById(R.id.tv_delete);
        //设置点击事件
        tv_share.setOnClickListener(new DialogItemClickListener());
        tv_ring.setOnClickListener(new DialogItemClickListener());
        tv_songInfo.setOnClickListener(new DialogItemClickListener());
        tv_delete.setOnClickListener(new DialogItemClickListener());
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        //设置歌曲名为标题
        builder.setTitle(music.getTitle() + "");
        //设置布局内容
        builder.setView(view);
        moreDialog = builder.create();
        moreDialog.show();
    }

    private void showMusicInfoDialog() {
        StringBuilder info = new StringBuilder();
        info.append("艺术家：" + dialogMusic.getSinger())
                .append("\n\n")
                .append("专辑：" + dialogMusic.getAlbum())
                .append("\n\n")
                .append("播放时长：" + formatUtils.getFormatTime(dialogMusic.getDuration()))
                .append("\n\n")
                .append("文件名称：" + dialogMusic.getFileName())
                .append("\n\n")
                .append("文件大小：" + formatUtils.getFormatSize(dialogMusic.getSize()) + "MB")
                .append("\n\n")
                .append("文件路径：" + dialogMusic.getPath());


        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(dialogMusic.getTitle());
        builder.setMessage(info);
        builder.show();
    }

//

    @Override
    public void onDestroy() {
        super.onDestroy();
        getActivity().unbindService(myCon);
    }

    //*
    //     * @param isSelect 选中还是取消选中
    //     */
    private void setSelectMusic(boolean isSelect) {
        int mPosition = appCache.getCurrentIndex();
        int firstPosition = linearLayoutManager.findFirstVisibleItemPosition();
        int lastPosition = linearLayoutManager.findLastVisibleItemPosition();
        //当前点的item为选中
        if (mPosition >= firstPosition && mPosition <= lastPosition) {
            if (isSelect)
                linearLayoutManager.findViewByPosition(mPosition).findViewById(R.id.view_select).setVisibility(View.VISIBLE);
            if (!isSelect)
                linearLayoutManager.findViewByPosition(mPosition).findViewById(R.id.view_select).setVisibility(View.INVISIBLE);
        }

        if (mPosition == lastPosition + 1) {
            myRecyclerAdatper.notifyItemChanged(appCache.getCurrentIndex());
        }
        if (isSelect && mPosition >= 0)
            appCache.getMusic().get(mPosition).setSelect(true);
        if (!isSelect && mPosition >= 0)
            appCache.getMusic().get(mPosition).setSelect(false);
    }

    /**
     * item点击配置
     */
    private void musicItemClickConfig(Music mMusic) {
        iv_musicStatus.setImageResource(R.mipmap.ic_play_bar_btn_pause);
        setBottomBarMessage(mMusic.getTitle(), mMusic.getSinger(), mMusic.getImagePath());
        saveSpMusic(mMusic.getPath(), appCache.getCurrentIndex());
    }

    public void clickStatusPlay() {
        if (appCache.getCurrentIndex() == -1) {
            return;
        }
        if (appCache.isPlaying()) {//正在播放
            appCache.setPlaying(false);
            iv_musicStatus.setImageResource(R.mipmap.ic_play_bar_btn_play);
            myBinder.bPauseMusic();
        } else {//没播放
            appCache.setPlaying(true);      //记录正在播放
            iv_musicStatus.setImageResource(R.mipmap.ic_play_bar_btn_pause);    //更换图片
            if (!appCache.isOnNet()) {
                if (appCache.getCurrentIndex() >= 0) {
                    setSelectMusic(true);
                    myBinder.bPlayMusic(handler, appCache.getCurrentMusic().getPath());
                    myRecyclerAdatper.setRed(appCache.getCurrentIndex());
                }
            } else {
                myBinder.bPlayMusic(handler, MusicUtils.getInstence().Song2Music(appCache.getNetSong()).getPath());
            }
        }
    }

    /**
     * 分享音乐
     */
    public void shareMusic() {

    }

    /**
     * 设置铃声
     */
    public void setRing() {

    }


    /**
     * 删除歌曲
     */
    public void deleteMusic() {
        final String path = dialogMusic.getPath();
        final File file = new File(path);
        if (file.exists()) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setTitle("提示!!");
            builder.setMessage("删除后无法找回，是否删除?");
            builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    if (dialogMusic.isSelect()) {//如果要删除的歌曲是被选中的
                        if (!appCache.isPlaying()) {//如果没有播放
                            reSetSp();
                        } else {                  //如果在播放
                            musicNextPlay();
                        }
                    }
                    file.delete();
                    file.deleteOnExit();
                    // 刷新媒体库
                    Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse("file://" + path));
                    getActivity().sendBroadcast(intent);
                    Toast.makeText(getActivity(), "已经删除" + dialogMusic.getTitle(), Toast.LENGTH_SHORT).show();
                    appCache.deleteMusic(dialogMusic.getPath());
                    myRecyclerAdatper.notifyDataSetChanged();
                    moreDialog.dismiss();
                    dialogMusic = null;
                }
            });
            builder.show();
        } else {
            Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse("file://" + path));
            getActivity().sendBroadcast(intent);
            appCache.deleteMusic(dialogMusic.getPath());
            myRecyclerAdatper.notifyDataSetChanged();
            moreDialog.dismiss();
            dialogMusic = null;
            Toast.makeText(getActivity(), "文件不存在", Toast.LENGTH_SHORT).show();
        }

    }


    /**
     * 重置歌曲播放缓存记录
     */
    public void reSetSp() {
        //记录清空
        appCache.setCurrentIndex(-1);
        myRecyclerAdatper.setRed(-100);
        saveSpMusic("", -1);
        myBinder.bRemoveRunnable();   //进度消息撤回
        setBottomBarMessage("", "", "00");
        progressBar.setProgress(0);
        if (appCache.getPlayMusicFrament() != null) {
            appCache.getPlayMusicFrament().setProgress(0);
            appCache.getPlayMusicFrament().reflashFace();
        }
    }

    /**
     * 播放网络音乐
     */
    public void playNetMusic(Music music) {
        setSelectMusic(false);
        appCache.setOnNet(true);
        appCache.setPlaying(true);
        //底部信息加载
        setBottomBarMessage(music.getTitle(), music.getSinger(), music.getImagePath());
        iv_musicStatus.setImageResource(R.mipmap.ic_play_bar_btn_pause);
        //播放音乐
        playMusic(music, music.getPath());
        //
    }

    /**
     * 服务绑定类
     */
    class MyCon implements ServiceConnection {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            myBinder = (MusicService.MyBinder) service;
            appCache.setMyBinder(myBinder);
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    }

    /**
     * recycler条目点击监听类
     */
    class MyRecyclerItemClickListener implements MyLocalRecyclerAdatper.OnItemClickListener {
        @Override
        public void onItemClick(int position) {
            appCache.setOnNet(false);
            Music music = appCache.getMusic().get(position);
            if (music.isSelect()) {
                if (!appCache.isPlaying()) {
                    appCache.setPlaying(true);
                    myBinder.bPlayMusic(handler, appCache.getCurrentMusic().getPath());
                    iv_musicStatus.setImageResource(R.mipmap.ic_play_bar_btn_pause);
                } else {
                    appCache.setPlaying(false);
                    myBinder.bPauseMusic();
                    iv_musicStatus.setImageResource(R.mipmap.ic_play_bar_btn_play);
                }
            } else {
                appCache.setCurrentIndex(position);
                musicItemClickConfig(music);
                playMusic(music, music.getPath());
            }
        }
    }

    /**
     * 音乐暂停图片按钮点击监听类
     */
    class MyOnClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.iv_musicStatus:
                    clickStatusPlay();
                    break;
                case R.id.iv_musicNext:
                    musicNextPlay();
                    break;
            }
        }
    }

    class MyMoreClickListener implements MyLocalRecyclerAdatper.OnMoreClickListener {
        @Override
        public void onClick(Music music) {
            showMoreDialog(music);
        }
    }

    class DialogItemClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.tv_share://分享
                    shareMusic();
                    break;
                case R.id.tv_ring://设置为铃声
                    setRing();
                    break;
                case R.id.tv_songInfo://歌曲信息
                    showMusicInfoDialog();
                    break;
                case R.id.tv_delete://删除歌曲
                    deleteMusic();
                    break;
            }

        }
    }


}
