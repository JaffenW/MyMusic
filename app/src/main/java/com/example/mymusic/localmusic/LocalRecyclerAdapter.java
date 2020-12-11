package com.example.mymusic.localmusic;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mymusic.common.Music;
import com.example.mymusic.R;
import com.example.mymusic.play.PlayActivity;

import java.util.List;

public class LocalRecyclerAdapter extends RecyclerView.Adapter<LocalRecyclerAdapter.ViewHolder> {
    private Context context;
    private int resource;
    private List<Music> musics;

    public LocalRecyclerAdapter(Context context, int resource, List<Music> musics) {
        this.context = context;
        this.resource = resource;
        this.musics = musics;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ViewHolder viewHolder;
        View view;
        view = LayoutInflater.from(context).inflate(resource,parent,false);
        viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final Music music = musics.get(position);

        holder.musicmrg.setImageBitmap(music.getAlbun());

        //设置本地音乐列表的点击事件，通过点击专辑图片跳转到播放界面
        holder.musicmrg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (music.getAlbun() == null)
                    Log.d("helloword","图片内容为空");
                Intent intent = new Intent(context, PlayActivity.class);
                intent.putExtra("songname",music.getName());//歌名
                intent.putExtra("singer",music.getSinger());//歌手
                intent.putExtra("album",music.getAlbun());//专辑图片
                intent.putExtra("duration",music.getDuration());//歌曲时长
                intent.putExtra("data",music.getData());//播放路径
                context.startActivity(intent);
            }
        });

        holder.songname.setText(music.getName());
        holder.artist.setText(music.getSinger());
    }

    @Override
    public int getItemCount() {
        return musics.size();
    }

    //继承ViewHolder类
    class ViewHolder extends RecyclerView.ViewHolder{
        ImageView musicmrg;
        TextView songname;
        TextView artist;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            musicmrg = itemView.findViewById(R.id.musicmrg);
            songname = itemView.findViewById(R.id.songname);
            artist = itemView.findViewById(R.id.artist);
        }
    }
}
