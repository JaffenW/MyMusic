package com.example.mymusic.play;

import android.app.Service;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.example.mymusic.R;
import com.example.mymusic.common.PlayService;
import java.io.IOException;

public class SpecialFragment extends Fragment {
    View view;
    Intent intent;
    PlayService.MyBinder binder;
    boolean isPlay = true;
    boolean isFirst = true;
    private String songname;
    private String singer;
     private Bitmap album;
    private String duration;
    private String data;
    private TextView songNamel;
    private TextView singerl;
    private ImageView albuml;
    private TextView durationl;
    private ImageView btn_play;
    private SeekBar seekBar;
    private TextView current;

    //ServiceConnection的一个匿名内部类，用以获取service中的binder对象
    private ServiceConnection connection = new ServiceConnection(){
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            binder = (PlayService.MyBinder) service;
        }
        @Override
        public void onServiceDisconnected(ComponentName name) {
        }
    };

    //一个Handler匿名内部类，用来处理多线程获取的实时音乐播放时间，用以更新播放界面进度条前面的的时间
    Handler handler = new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 1:current.setText((String) msg.obj);
            }
        }
    };

    //SpecialFragment的一个构造函数
    public SpecialFragment(String songname, String singer, Bitmap album, String duration, String data) {
        this.songname = songname;
        this.singer = singer;
        this.album = album;
        this.duration = duration;
        this.data = data;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.playmusic_special,container,false);
        init(view);
        try {
            setService();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
        return view;
    }

    //这是对界面中的变量进行初始化的方法
    private void init(View view){
        songNamel = view.findViewById(R.id.songname);
        singerl = view.findViewById(R.id.singer);
        albuml = view.findViewById(R.id.albun);
        durationl = view.findViewById(R.id.duration);
        btn_play = view.findViewById(R.id.btn_play);
        seekBar = view.findViewById(R.id.progressBar);
        current = view.findViewById(R.id.textView3);

        songNamel.setText(songname);
        singerl.setText(singer);
        albuml.setImageBitmap(album);
        durationl.setText(duration);
        //设置进度条的监听器
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                long position = seekBar.getProgress()*binder.getDuration()/seekBar.getMax();
                binder.setCurrentPosition(position);//service中自定义的设置音乐当前播放进度的一个方法
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                long position = seekBar.getProgress()*binder.getDuration()/seekBar.getMax();
                binder.setCurrentPosition(position);
            }
        });
    }

    //这是设置服务的方法
    private void setService() throws IOException, InterruptedException {
        intent = new Intent(getContext(),PlayService.class);//连接服务的一个Intent
        getContext().startService(intent);
        getContext().bindService(intent,connection, Service.BIND_AUTO_CREATE);//使用bindService方式启动

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    //这里进行一个一分钟的延迟，因为如果bindService执行之后绑定还需要一段时间，如果马上调用binder会null
                    Thread.sleep(1000);
                    //当进入播放界面时默认开始播放
                    binder.setDataSource(data);//设置音乐的播放路径
                    binder.play();//点击歌曲进入播放界面后默认开始播放

                    String currentime;
                    while (true){
                        //设置音乐与进度条前面的时间同步
                        if (binder.getCurrentPosition()/1000>60){
                            currentime = binder.getCurrentPosition()/1000/60+":"+binder.getCurrentPosition()/1000%60;
                        }else   //如果没大于一分钟就直接将前面固定为00
                            currentime = "00:"+binder.getCurrentPosition()/1000%60;
                        Message message = new Message();
                        message.obj = currentime;
                        message.what = 1;
                        handler.sendMessage(message);

                        //设置音乐与进度条同步
                        if (binder.getDuration()>0){
                            long i = seekBar.getMax()*binder.getCurrentPosition()/binder.getDuration();
                            seekBar.setProgress((int)i);//这里虽然也是设置ui控件，但是程序能正常运行且不报错，所以就不转到handler处理了
                        }
                        Thread.sleep(1000);//约一秒同步一次
                    }
                } catch (InterruptedException | IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
        btn_play.setImageResource(R.mipmap.ic_play_btn_pause);//设置按钮图片为播放图片

        //设置播放按钮的点击事件
        btn_play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isPlay){//如果没有播放，就进行播放且将按钮图案改为播放图片
                    binder.play();
                    btn_play.setImageResource(R.mipmap.ic_play_btn_pause);
                    isPlay = true;
                }else{//如果正在播放，就暂停且将按钮图案改为暂停图片
                    binder.pause();
                    btn_play.setImageResource(R.mipmap.ic_play_btn_play);
                    isPlay = false;
                }
            }
        });
    }
}
