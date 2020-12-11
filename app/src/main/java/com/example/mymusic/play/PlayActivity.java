package com.example.mymusic.play;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;
import com.example.mymusic.R;
import java.util.ArrayList;

public class PlayActivity extends AppCompatActivity {
    private String songname;
    private String singer;
    private Bitmap album;
    private String duration;
    private String data;
    private SpecialFragment specialFragment;//专辑Fragment
    private LyricsFragment lyricsFragment;//歌词Fragment
    private ViewPager viewPager;//用来存放两个Fragment的ViewPager
    private ArrayList<Fragment> fragmentlist = new ArrayList<Fragment>();
    private MyPageAdapter pagerAdapter;//自定义的FragmentPagerAdapter

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.playmusic);
        init(getIntent());
        setAdapter();
    }

    public void init(Intent intent){
        //获取从本地音乐列表传过来的音乐信息
        songname = intent.getStringExtra("songname");
        singer = intent.getStringExtra("singer");
        album = intent.getParcelableExtra("album");
        duration = intent.getStringExtra("duration");
        data = intent.getStringExtra("data");

        viewPager = findViewById(R.id.viewPager);
        //专辑Fragment，主要的播放功能展示都在此fragment所在的界面
        specialFragment = new SpecialFragment(songname,singer,album,duration,data);
        //歌词Fragment
        lyricsFragment = new LyricsFragment();
    }

    public void setAdapter(){
        fragmentlist.add(specialFragment);
        fragmentlist.add(lyricsFragment);

        //实现滑动Fragment的关键步骤
        viewPager.setAdapter(new MyPageAdapter(getSupportFragmentManager(),fragmentlist));
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }
            @Override
            public void onPageSelected(int position) {
            }
            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

}
