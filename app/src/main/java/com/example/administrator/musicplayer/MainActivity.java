package com.example.administrator.musicplayer;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.services.weather.LocalWeatherForecastResult;
import com.amap.api.services.weather.LocalWeatherLive;
import com.amap.api.services.weather.LocalWeatherLiveResult;
import com.amap.api.services.weather.WeatherSearch;
import com.amap.api.services.weather.WeatherSearchQuery;
import com.example.administrator.musicplayer.adapter.MyFragmentAdapter;
import com.example.administrator.musicplayer.application.AppCache;
import com.example.administrator.musicplayer.fragment.LocalMusicFragment;
import com.example.administrator.musicplayer.fragment.OnlineMusicFragment;
import com.example.administrator.musicplayer.fragment.PlayMusicFrament;

import java.util.ArrayList;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity {
    private ArrayList<Fragment> fragments = new ArrayList<>();
    private NavigationView nav;
    private DrawerLayout drawerLayout;
    private ViewPager vp;
    private TextView tv_localMusic;
    private TextView tv_onlineMusic;
    private final int LOCAL_MUSIC = 0;
    private final int ONLINE_MUSIC = 1;
    private TextView tv_songName;
    private TextView tv_singer;
    private boolean isFragmentPlaying = false;
    private PlayMusicFrament playMusicFrament;
    private LocalMusicFragment localMusicFragment;
    private OnlineMusicFragment onlineMusicFragment;
    private View headerView;
    private ImageView iv_weather;
    private TextView tv_temperture;
    private TextView tv_locate;
    private TextView tv_wind;
    private WeatherSearchQuery weatherSearchQuery;
    private WeatherSearch weatherSearch;
    private TextView tv_city;
    private String city = "";
    private AMapLocationClient aMapLocationClient;
    private AMapLocationListener aMapLocationListener;
    private AMapLocationClientOption mLocationOption;
    private AppCache appCache;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initUI();
        initData();
        initConfig();
        initListener();
        checkLocationPermission(android.Manifest.permission.ACCESS_COARSE_LOCATION);
    }

    /**
     * 检查定位权限
     */
    private void checkLocationPermission(String reqPermission) {
        if (ContextCompat.checkSelfPermission(MainActivity.this, reqPermission) != PackageManager.PERMISSION_GRANTED) {//如果没有定位权限
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{reqPermission,Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
        }else {
            aMapLocationClient.startLocation();
            localMusicFragment.scanMusic(this);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 1:
                if (grantResults.length>0&&grantResults[0] == PackageManager.PERMISSION_GRANTED) {//获取成功
                    //开始定位
                    aMapLocationClient.startLocation();
                    localMusicFragment.scanMusic(this);
                } else {//获取失败
                    Toast.makeText(MainActivity.this, "用户已拒绝该给予该应用权限", Toast.LENGTH_SHORT).show();
                }
                break;
        }


    }

    private void initListener() {
        //viewPager设置滚动监听
        vp.addOnPageChangeListener(new MyOnScollListener());
        //设置navigation 的item 点击事件
        nav.setNavigationItemSelectedListener(new MyNavigationItemSelectionListener());
    }

    private void initConfig() {
        //viewPager设置适配器
        vp.setAdapter(new MyFragmentAdapter(getSupportFragmentManager(), fragments));
        //设置定位模式为高精度
        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
        //设置一次定位
        mLocationOption.setOnceLocation(true);
        //设置缓存机制
        mLocationOption.setLocationCacheEnable(true);
        //初始化定位
        aMapLocationClient.setLocationListener(aMapLocationListener);
    }

    private void initData() {
        appCache = AppCache.getInstence();
        appCache.setMainActivity(this);
        appCache.setAppContext(getApplicationContext());
        //声明AMapLocationClientOption对象
        mLocationOption = new AMapLocationClientOption();
        //声明AMapLocationClient类对象
        aMapLocationClient = new AMapLocationClient(MainActivity.this);
        //声明定位回调监听器
        aMapLocationListener = new AMapLocationListener() {
            @Override
            public void onLocationChanged(AMapLocation aMapLocation) {
                if (aMapLocation != null) {
                    if (aMapLocation.getErrorCode() == 0) {
                        city = aMapLocation.getCity();
                        initWeather();
                        //停止定位
                        aMapLocationClient.stopLocation();
                    } else {
                        //定位失败时，可通过ErrCode（错误码）信息来确定失败的原因，errInfo是错误信息，详见错误码表。
                        Log.e("AmapError", "location Error, ErrCode:"
                                + aMapLocation.getErrorCode() + ", errInfo:"
                                + aMapLocation.getErrorInfo());
                    }
                }
            }
        };

        localMusicFragment = new LocalMusicFragment();
        onlineMusicFragment = new OnlineMusicFragment();
        appCache.setLocalMusicFragment(localMusicFragment);
        appCache.setOnlineMusicFragment(onlineMusicFragment);
        fragments.add(localMusicFragment);
        fragments.add(onlineMusicFragment);
    }

    private void initUI() {
        //侧滑栏头部布局
        headerView = View.inflate(MainActivity.this, R.layout.nav_head, null);
        nav = (NavigationView) findViewById(R.id.nav_view);
        nav.addHeaderView(headerView);
        iv_weather = (ImageView) headerView.findViewById(R.id.iv_weather);
        tv_temperture = (TextView) headerView.findViewById(R.id.tv_temperture);
        tv_city = (TextView) headerView.findViewById(R.id.tv_city);
        tv_wind = (TextView) headerView.findViewById(R.id.tv_wind);
        drawerLayout = (DrawerLayout) findViewById(R.id.dl);
        vp = (ViewPager) findViewById(R.id.vp);
        tv_localMusic = (TextView) findViewById(R.id.tv_localMusic);
        tv_onlineMusic = (TextView) findViewById(R.id.tv_onlineMusic);
        tv_songName = (TextView) findViewById(R.id.tv_songName);
        tv_singer = (TextView) findViewById(R.id.tv_singer);
    }

    /**
     * 点击事件绑定的方法
     *
     * @param v
     */
    public void click(View v) {
        switch (v.getId()) {
            case R.id.iv_menu://点击menu图片
                drawerLayout.openDrawer(GravityCompat.START);
                break;
            case R.id.iv_search://点击搜索按钮
                startActivity(new Intent(MainActivity.this, SearchActivity.class));
                break;
            case R.id.tv_localMusic://点击我的音乐
                //改变颜色
                colorChange(LOCAL_MUSIC);
                //viewPager翻页
                vp.setCurrentItem(LOCAL_MUSIC);
                break;
            case R.id.tv_onlineMusic://点击在线音乐
                //改变颜色
                colorChange(ONLINE_MUSIC);
                //viewPager翻页
                vp.setCurrentItem(ONLINE_MUSIC);
                break;
            case R.id.lv_bottom://点击bottomBar
                showPlayMusicFragment();
                break;
        }
    }

    /**
     * 显示播放界面
     */
    private void showPlayMusicFragment() {
        if (isFragmentPlaying) {
            return;
        }
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        //加载显示动画
        fragmentTransaction.setCustomAnimations(R.anim.playmusic_show, 0);
        if (playMusicFrament == null) {
            playMusicFrament = new PlayMusicFrament();
            appCache.setPlayMusicFrament(playMusicFrament);
            fragmentTransaction.replace(R.id.fl_main, playMusicFrament);
        } else {
            //刷新封面
            playMusicFrament.reflashFace();
            //判断是否正在播放
            if (appCache.isPlaying()) {
                playMusicFrament.setPauseImage();
            } else {
                playMusicFrament.setPlayImage();
            }
            fragmentTransaction.show(playMusicFrament);
        }
        fragmentTransaction.commitAllowingStateLoss();
        isFragmentPlaying = true;
    }

    /**
     * 隐藏播放界面
     */
    private void hidePlayMusicFragment() {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.setCustomAnimations(0, R.anim.playmusic_hide);
        fragmentTransaction.hide(playMusicFrament);
        fragmentTransaction.commitAllowingStateLoss();
        isFragmentPlaying = false;
    }

    /**
     * 改变tab颜色
     *
     * @param position
     */
    private void colorChange(int position) {
        if (position == 0) {
            tv_localMusic.setTextColor(getResources().getColor(R.color.white));
            tv_onlineMusic.setTextColor(getResources().getColor(R.color.translucent_white));
        } else if (position == 1) {
            tv_onlineMusic.setTextColor(getResources().getColor(R.color.white));
            tv_localMusic.setTextColor(getResources().getColor(R.color.translucent_white));
        }
    }


    @Override
    public void onBackPressed() {//更改返回键的功能
        if (nav.isShown()) {//如果nav展开,则关闭
            drawerLayout.closeDrawers();
        } else if (isFragmentPlaying) {
            hidePlayMusicFragment();
        } else {
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.addCategory(Intent.CATEGORY_HOME);
            startActivity(intent);
        }
    }

    /**
     * 监听viewPager滚动的类
     */
    class MyOnScollListener implements ViewPager.OnPageChangeListener {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @RequiresApi(api = Build.VERSION_CODES.M)
        @Override
        public void onPageSelected(int position) {
            colorChange(position);
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    }

    class MyWeatherListener implements WeatherSearch.OnWeatherSearchListener {
        @Override
        public void onWeatherLiveSearched(LocalWeatherLiveResult localWeatherLiveResult, int rCode) {
            if (rCode == 1000) {
                LocalWeatherLive liveResult = localWeatherLiveResult.getLiveResult();
                if (localWeatherLiveResult != null && liveResult != null) {
                    //设置图片
                    iv_weather.setImageResource(getWeatherIcon(liveResult.getWeather()));
                    //刷新温度
                    tv_temperture.setText(liveResult.getTemperature() + "°");
                    //刷新城市
                    tv_city.setText(liveResult.getCity());
                    //刷新风级和相对湿度
                    tv_wind.setText(liveResult.getWindDirection() + "风" + liveResult.getWindPower() + "级   相对湿度" + liveResult.getHumidity() + "%");
                } else {
                    Toast.makeText(MainActivity.this, "获取天气没有结果", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(MainActivity.this, "获取天气发生错误", Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public void onWeatherForecastSearched(LocalWeatherForecastResult localWeatherForecastResult, int i) {

        }
    }

    /**
     * navigation的item点击事件
     */
    class MyNavigationItemSelectionListener implements NavigationView.OnNavigationItemSelectedListener {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.menuItem_timer:    //定时关闭播放
                    Toast.makeText(MainActivity.this, "1", Toast.LENGTH_SHORT).show();
                    break;
                case R.id.menuItem_mode:     //夜间模式、白天模式
                    Toast.makeText(MainActivity.this, "1", Toast.LENGTH_SHORT).show();
                    break;
                case R.id.menuItem_setting:  //功能设置
                    Toast.makeText(MainActivity.this, "1", Toast.LENGTH_SHORT).show();
                    break;
                case R.id.menuItem_about:    //应用关于页面
                    Toast.makeText(MainActivity.this, "1", Toast.LENGTH_SHORT).show();
                    break;
                case R.id.menuItem_exit:     //退出应用
                    Toast.makeText(MainActivity.this, "1", Toast.LENGTH_SHORT).show();
                    break;
            }
            return true;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    /**
     * 加载天气
     */
    private void initWeather() {
        if (weatherSearchQuery == null)
            weatherSearchQuery = new WeatherSearchQuery(city, WeatherSearchQuery.WEATHER_TYPE_LIVE);
        if (weatherSearch == null)
            weatherSearch = new WeatherSearch(MainActivity.this);
        //设置天气监听
        weatherSearch.setOnWeatherSearchListener(new MyWeatherListener());
        weatherSearch.setQuery(weatherSearchQuery);
        weatherSearch.searchWeatherAsyn();
    }

    /**
     * 根据温度和时间获取icon
     *
     * @param weather
     * @return
     */
    private int getWeatherIcon(String weather) {
        if (weather.contains("-")) {
            weather = weather.substring(0, weather.indexOf("-"));
        }
        int hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
        int resId;
        if (weather.contains("晴")) {
            if (hour >= 7 && hour < 19) {
                resId = R.mipmap.ic_weather_sunny;
            } else {
                resId = R.mipmap.ic_weather_sunny_night;
            }
        } else if (weather.contains("多云")) {
            if (hour >= 7 && hour < 19) {
                resId = R.mipmap.ic_weather_cloudy;
            } else {
                resId = R.mipmap.ic_weather_cloudy_night;
            }
        } else if (weather.contains("阴")) {
            resId = R.mipmap.ic_weather_overcast;
        } else if (weather.contains("雷阵雨")) {
            resId = R.mipmap.ic_weather_thunderstorm;
        } else if (weather.contains("雨夹雪")) {
            resId = R.mipmap.ic_weather_sleet;
        } else if (weather.contains("雨")) {
            resId = R.mipmap.ic_weather_rain;
        } else if (weather.contains("雪")) {
            resId = R.mipmap.ic_weather_snow;
        } else if (weather.contains("雾") || weather.contains("霾")) {
            resId = R.mipmap.ic_weather_foggy;
        } else if (weather.contains("风") || weather.contains("飑")) {
            resId = R.mipmap.ic_weather_typhoon;
        } else if (weather.contains("沙") || weather.contains("尘")) {
            resId = R.mipmap.ic_weather_sandstorm;
        } else {
            resId = R.mipmap.ic_weather_cloudy;
        }
        return resId;
    }


}