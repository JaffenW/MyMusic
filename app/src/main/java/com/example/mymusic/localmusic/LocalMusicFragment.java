package com.example.mymusic.localmusic;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.mymusic.common.Music;
import com.example.mymusic.R;
import java.util.ArrayList;
import java.util.List;

public class LocalMusicFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.main_fragment_local,container,false);
        setRecyclerView(view,getMyData());
        return view;
    }

    //设置RecyclerView的方法
    public void setRecyclerView(View view,List<Music> mymusic){
        RecyclerView recyclerView = view.findViewById(R.id.localrecyclerview);
        LocalRecyclerAdapter localRecyclerAdapter = new LocalRecyclerAdapter(getContext(),R.layout.recyclerview_local_item,mymusic);//自定义的RecyclerView一个Adapter
        recyclerView.setAdapter(localRecyclerAdapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(linearLayoutManager);
    }

    //获取本地音乐的数据
    public List<Music> getMyData(){
        List<Music> mydata = new ArrayList<>();
        Music music;
        Uri usi = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        String[] pros = new String[]{MediaStore.Audio.Media.TITLE,MediaStore.Audio.Media.ARTIST,MediaStore.Audio.Media.ALBUM_ID,
                MediaStore.Audio.Media.DURATION,MediaStore.Audio.Media.DATA};
        Cursor cursor = getContext().getContentResolver().query(usi,pros,null,null,null);
        cursor.moveToFirst();
        do{
            String songname = cursor.getString(0);//歌名
            String singer = cursor.getString(1);//歌手
            String album_id = cursor.getString(2);//专辑ID
            int duration = cursor.getInt(3);//播放时间
            String data = cursor.getString(4);//播放路径
            String time = (duration/1000/60)+":"+duration/1000%60;

            music = new Music(getAlbunArt(album_id),songname,singer,time,data);
            mydata.add(music);
        }while (cursor.moveToNext());

        cursor.close();
        return mydata;
    }

    //通过传入的专辑ID获取对应的位图文件
    public Bitmap getAlbunArt(String albun_id){
        Cursor cursor = getContext().getContentResolver().query(Uri.parse("content://media/external/audio/albums/"+albun_id),
                null,null,null,null);
        String album_art = null;
        if (cursor.getCount() > 0 && cursor.getColumnCount() > 0) {
            cursor.moveToNext();
            album_art = cursor.getString(cursor.getColumnIndex("album_art"));
        }
        Bitmap picture = BitmapFactory.decodeFile(album_art);
        return picture;
    }
}
