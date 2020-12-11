package com.example.mymusic.common;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.IBinder;
import androidx.annotation.Nullable;
import java.io.IOException;

public class PlayService extends Service {
    private MediaPlayer player;
    private boolean isFirstPlay = true;//一个标志，是否时第一次播放
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        if(!isFirstPlay){//不是第一次播放则是要切歌，切歌的话首先要将前一次的player资源释放
            player.release();
        }
        new Thread(){
            @Override
            public void run(){
                super.run();
                player = new MediaPlayer();
                isFirstPlay = false;
            }
        }.start();
        return new MyBinder();
    }

    @Override
    public void onDestroy() {
        player.release();
        super.onDestroy();
    }

    @Override
    public boolean onUnbind(Intent intent) {
        return super.onUnbind(intent);
    }

    public class MyBinder extends Binder {
        //设置音乐路径
        public void setDataSource(String dataSource) throws IOException {
            player.reset();//切歌后需要重置Idle状态才能设置播放路径
            player.setDataSource(dataSource);
            player.setLooping(true);//设置循环模式，在此即是进行单曲循环
            player.prepare();
        }
        //播放音乐
        public void play(){
            player.start();
        }
        //暂停
        public void pause(){
            player.pause();
        }
        //获取当前播放进度
        public int getCurrentPosition(){
            return player.getCurrentPosition();
        }
        //获取歌曲时长
        public int getDuration(){
            return player.getDuration();
        }
        //用于通过滑动或点击进度条来控制歌曲播放的进度
        public void setCurrentPosition(long position){
            player.seekTo((int) position);
        }
    }
}
