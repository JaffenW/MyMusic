package com.example.mymusic.musiclist;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.mymusic.common.Music;
import com.example.mymusic.R;

import java.util.List;

public class ListsAdapter extends ArrayAdapter {
    Context context;
    int resource;
    public ListsAdapter(@NonNull Context context, int resource, @NonNull List objects) {
        super(context, resource, objects);
        this.context = context;
        this.resource = resource;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view;
        ViewHolder viewHolder = new ViewHolder();
        Music music = (Music) getItem(position);

        if(convertView == null){
            view = LayoutInflater.from(context).inflate(resource,parent,false);
            viewHolder.pecture = view.findViewById(R.id.songmrg);//歌曲的图片
            viewHolder.name = view.findViewById(R.id.songname);//歌曲名
            viewHolder.singer = view.findViewById(R.id.artist);//歌手
            viewHolder.more = view.findViewById(R.id.listmore);//详情按键
            view.setTag(viewHolder);
        }else{
            view = convertView;
            viewHolder = (ViewHolder) view.getTag();
        }

        viewHolder.pecture.setImageResource(music.getPicture());
        viewHolder.name.setText(music.getName());
        viewHolder.singer.setText(music.getSinger());
        viewHolder.more.setImageResource(R.mipmap.ic_music_list_icon_more);
        return view;
    }

    class ViewHolder{
        ImageView pecture;
        TextView name;
        TextView singer;
        ImageView more;
    }
}
