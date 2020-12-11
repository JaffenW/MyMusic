package com.example.mymusic.common;

import android.graphics.Bitmap;

import java.util.List;

public class Chart {
    private String chart_id;//榜单ID
    private Bitmap picturesrc;//榜单图片
    private String chartname;//榜单名
    private String firstsong;
    private String firstsinger;
    private String secondsong;
    private String secondsinger;
    private String thirdsong;
    private String thirdsinger;
    private String undate_date;//榜单的更新时间
    private String comment;//榜单的简介
    private List<Music> musics;

    public Chart(String chart_id,Bitmap picturesrc, String chartname, String firstsong,
                 String firstsinger, String secondsong, String secondsinger, String thirdsong, String thirdsinger) {
        this.chart_id = chart_id;
        this.picturesrc = picturesrc;
        this.chartname = chartname;
        this.firstsong = firstsong;
        this.firstsinger = firstsinger;
        this.secondsong = secondsong;
        this.secondsinger = secondsinger;
        this.thirdsong = thirdsong;
        this.thirdsinger = thirdsinger;
    }

    public Chart(Bitmap picturesrc, String chartname, String undate_date, String comment, List<Music> musics) {
        this.picturesrc = picturesrc;
        this.chartname = chartname;
        this.undate_date = undate_date;
        this.comment = comment;
        this.musics = musics;
    }

    public Chart() {
    }

    public String getChart_id() {
        return chart_id;
    }

    public void setChart_id(String chart_id) {
        this.chart_id = chart_id;
    }

    public Bitmap getPicturesrc() {
        return picturesrc;
    }

    public void setPicturesrc(Bitmap picturesrc) {
        this.picturesrc = picturesrc;
    }

    public String getChartname() {
        return chartname;
    }

    public void setChartname(String chartname) {
        this.chartname = chartname;
    }

    public String getFirstsong() {
        return firstsong;
    }

    public void setFirstsong(String firstsong) {
        this.firstsong = firstsong;
    }

    public String getFirstsinger() {
        return firstsinger;
    }

    public void setFirstsinger(String firstsinger) {
        this.firstsinger = firstsinger;
    }

    public String getSecondsong() {
        return secondsong;
    }

    public void setSecondsong(String secondsong) {
        this.secondsong = secondsong;
    }

    public String getSecondsinger() {
        return secondsinger;
    }

    public void setSecondsinger(String secondsinger) {
        this.secondsinger = secondsinger;
    }

    public String getThirdsong() {
        return thirdsong;
    }

    public void setThirdsong(String thirdsong) {
        this.thirdsong = thirdsong;
    }

    public String getThirdsinger() {
        return thirdsinger;
    }

    public void setThirdsinger(String thirdsinger) {
        this.thirdsinger = thirdsinger;
    }

    public String getUndate_date() {
        return undate_date;
    }

    public void setUndate_date(String undate_date) {
        this.undate_date = undate_date;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public List<Music> getMusics() {
        return musics;
    }

    public void setMusics(List<Music> musics) {
        this.musics = musics;
    }
}
