package com.example.mymusic.common;

import android.graphics.Bitmap;

public class Music {
    private int picture;//int格式的专辑图片
    private Bitmap albun;//Bitmap格式的专辑图片
    private String songId;//歌曲Id
    private String name;//歌名
    private String singer;//歌手
    private String duration;//播放时间
    private String link;//播放路径

    public Music(Bitmap albun, String name, String singer) {
        this.albun = albun;
        this.name = name;
        this.singer = singer;
    }

    public Music(int picture, String name, String singer) {
        this.picture = picture;
        this.name = name;
        this.singer = singer;
    }

    public Music(Bitmap albun, String name, String singer, String time, String data) {
        this.albun = albun;
        this.name = name;
        this.singer = singer;
        this.duration = time;
        this.link = data;
    }

    public Music(Bitmap albun, String songId, String name, String singer) {
        this.albun = albun;
        this.songId = songId;
        this.name = name;
        this.singer = singer;
    }

    public int getPicture() {
        return picture;
    }

    public void setPicture(int picture) {
        this.picture = picture;
    }

    public Bitmap getAlbun() {
        return albun;
    }

    public void setAlbun(Bitmap albun) {
        this.albun = albun;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSinger() {
        return singer;
    }

    public void setSinger(String singer) {
        this.singer = singer;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getData() {
        return link;
    }

    public void setData(String data) {
        this.link = data;
    }

    public String getSongId() {
        return songId;
    }

    public void setSongId(String songId) {
        this.songId = songId;
    }
}
