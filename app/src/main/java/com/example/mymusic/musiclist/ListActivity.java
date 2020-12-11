package com.example.mymusic.musiclist;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.mymusic.common.MainActivity;
import com.example.mymusic.common.Music;
import com.example.mymusic.R;

import java.util.ArrayList;
import java.util.List;

public class ListActivity extends AppCompatActivity {
    ImageView goback;//用于实现左上角的返回功能
    ListView listView;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.playlist);
        setListView();
        setGoback();
    }

    public List<Music> getMydata(){
        List<Music> mymusic;
        Music music;

        mymusic = new ArrayList<Music>();
        music = new Music(R.mipmap.defaultpicture,"桥边姑娘","舞蹈女神诺涵");
        mymusic.add(music);
        music = new Music(R.mipmap.defaultpicture,"少年","宋小睿");
        mymusic.add(music);
        music = new Music(R.mipmap.defaultpicture,"大鱼","周深");
        mymusic.add(music);

        return mymusic;
    }

    public void setListView(){
        listView = findViewById(R.id.listview);

        ListsAdapter listsAdapter  = new ListsAdapter(ListActivity.this,R.layout.playlist_item,getMydata());
        listView.setAdapter(listsAdapter);
    }

    public void setGoback(){
        goback = findViewById(R.id.goback);
        goback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ListActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }
}
