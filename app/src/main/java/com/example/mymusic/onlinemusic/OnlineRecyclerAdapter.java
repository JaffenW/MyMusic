package com.example.mymusic.onlinemusic;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mymusic.common.Chart;
import com.example.mymusic.common.Music;
import com.example.mymusic.R;

import java.util.List;

public class OnlineRecyclerAdapter extends RecyclerView.Adapter<OnlineRecyclerAdapter.ViewHolder> {
    Context context;
    int resource;
    List<Chart> charts;

    public OnlineRecyclerAdapter(Context context, int resource, List<Chart> charts) {
        this.context = context;
        this.resource = resource;
        this.charts = charts;
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
        final Chart chart = charts.get(position);
        holder.special.setImageBitmap(chart.getPicturesrc());//设置榜单专辑图片
        holder.songname1.setText(chart.getFirstsong());//设置列表上的第一首歌名
        holder.songname2.setText(chart.getSecondsong());//设置列表上的第二首歌名
        holder.songname3.setText(chart.getThirdsong());//设置列表上的第三首歌名
        holder.singer1.setText(chart.getFirstsinger());//设置列表上的第一个歌手名
        holder.singer2.setText(chart.getSecondsinger());//设置列表上的第二个歌手名
        holder.singer3.setText(chart.getThirdsinger());//设置列表上的第三个歌手名

        //在此为专辑图片设置了一个点击事件，通过点击专辑图片可以进入专辑歌曲列表界面
        holder.special.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context,SpecialActivity.class);
                intent.putExtra("chart_id",chart.getChart_id());//设置了榜单ID，用于SpecialActivity界面进行查询榜单信息
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return charts.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        ImageView special;
        TextView songname1;
        TextView songname2;
        TextView songname3;
        TextView singer1;
        TextView singer2;
        TextView singer3;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            special = itemView.findViewById(R.id.special);
            songname1 = itemView.findViewById(R.id.songname1);
            songname2 = itemView.findViewById(R.id.songname2);
            songname3 = itemView.findViewById(R.id.songname3);
            singer1 = itemView.findViewById(R.id.singer1);
            singer2 = itemView.findViewById(R.id.singer2);
            singer3 = itemView.findViewById(R.id.singer3);
        }
    }
}
