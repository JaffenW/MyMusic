package com.example.mymusic.onlinemusic;

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

public class SpecialRecyclerAdapter extends RecyclerView.Adapter<SpecialRecyclerAdapter.ViewHolder> {
    Context context;
    int resource;
    List<Music> musics;

    public SpecialRecyclerAdapter(Context context, int resource, List<Music> musics) {
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
        holder.songmrg.setImageBitmap(music.getAlbun());
        holder.songmrg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (music.getAlbun() == null)
                    Log.d("helloword","图片内容为空");
                Intent intent = new Intent(context, PlayActivity.class);
                intent.putExtra("songname",music.getName());
                intent.putExtra("singer",music.getSinger());
                intent.putExtra("album",music.getAlbun());
                intent.putExtra("duration",music.getDuration());
                intent.putExtra("data",music.getData());
                context.startActivity(intent);
            }
        });
        holder.songname.setText(music.getName());
        holder.singer.setText(music.getSinger());
    }

    @Override
    public int getItemCount() {
        return musics.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        ImageView songmrg;//音乐专辑图片
        TextView songname;//音乐名
        TextView singer;//歌手
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            songmrg = itemView.findViewById(R.id.songmrg);
            songname = itemView.findViewById(R.id.songname);
            singer = itemView.findViewById(R.id.singer);
        }
    }
}
