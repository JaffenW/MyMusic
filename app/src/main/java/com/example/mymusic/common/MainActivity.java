package com.example.mymusic.common;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;
import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.example.mymusic.R;
import com.example.mymusic.localmusic.LocalMusicFragment;
import com.example.mymusic.musiclist.ListActivity;
import com.example.mymusic.onlinemusic.OnlineMusicFragment;
import com.example.mymusic.play.PlayActivity;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private ImageView playlist;
    private ImageView play;
    private TextView local;
    private TextView online;
    private ViewPager viewPager;
    private LocalMusicFragment localfragment;
    private OnlineMusicFragment onlinefragment;
    private List<Fragment> fragments;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setPlay();
        setPlaylist();
        setViewPager();
        setTitleClick();
        checkPermission();
    }

    //播放功能
    public void setPlay(){
        play = findViewById(R.id.play);//左下角的播放界面的一个按键
        //播放界面
        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, PlayActivity.class);
                startActivity(intent);
            }
        });
    }

    //播放列表功能
    public void setPlaylist(){
        playlist = findViewById(R.id.playlist);//右下角的播放列表按键
        //播放列表界面
        playlist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ListActivity.class);
                startActivity(intent);
            }
        });
    }

    //viewpager的初始化
    public void setPagerInit(){
        local = findViewById(R.id.local);//顶部本地音乐标题，用来设置点击事件
        online = findViewById(R.id.online);//顶部在线音乐标题，用来设置点击事件
        //ViewPager和Fragment等的实例
        viewPager = findViewById(R.id.musicviewpager);
        localfragment = new LocalMusicFragment();
        onlinefragment = new OnlineMusicFragment();
        fragments = new ArrayList<Fragment>();
        fragments.add(localfragment);
        fragments.add(onlinefragment);
    }

    //设置viewpager的Adapter和OnpagerchangeListener
    public void setViewPager(){
        setPagerInit();
        viewPager.setAdapter(new MainPagerAdapter(getSupportFragmentManager(),fragments));
        //设置ViewPager的滑动事件
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }
            @Override
            public void onPageSelected(int position) {
                if (position == 0){
                    online.setTextColor(0x76DCD0D0);
                    local.setTextColor(0xffFFFFFF);
                }else{
                    local.setTextColor(0x76DCD0D0);
                    online.setTextColor(0xffFFFFFF);
                }
            }
            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    //设置标题点击切换方法
    public void setTitleClick(){
        //本地音乐的点击事件，控制标题改变颜色
        local.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                online.setTextColor(0x76DCD0D0);
                local.setTextColor(0xffFFFFFF);
                viewPager.setCurrentItem(0,true);
            }
        });
        //在线音乐的点击事件，控制标题改变颜色
        online.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                local.setTextColor(0x76DCD0D0);
                online.setTextColor(0xffFFFFFF);
                viewPager.setCurrentItem(1,true);
            }
        });
    }

    //检查是否授权，没有授权就请求授权
    public void checkPermission(){
        if(ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(MainActivity.this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},1);
        }else if(ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.INTERNET)!= PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(MainActivity.this,new String[]{Manifest.permission.INTERNET},2);
            Toast.makeText(MainActivity.this,"请求访问网络",Toast.LENGTH_SHORT).show();
        }
    }

    //授权回调函数的处理
    public void onRequestPermissionResult(int requestCode,@NonNull String[] permissions, @NonNull int[] grantResults){
        switch (requestCode){
            case 1:
                if(grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    Toast.makeText(MainActivity.this,"访问本地音乐的权限终于授权成功了！",Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(MainActivity.this,"访问本地音乐的权限还是授权失败。。。",Toast.LENGTH_SHORT).show();
                }
            case 2:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    Toast.makeText(MainActivity.this,"访问网络的权限终于授权成功了！",Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(MainActivity.this,"访问网络的权限还是授权失败。。。",Toast.LENGTH_SHORT).show();
                }
        }
    }
}
