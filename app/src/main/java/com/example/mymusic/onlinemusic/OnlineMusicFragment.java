package com.example.mymusic.onlinemusic;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.mymusic.common.Chart;
import com.example.mymusic.R;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class OnlineMusicFragment extends Fragment {
    View view;
    public List<Chart> charts = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.main_fragment_online,container,false);
        runMyTask();
        return view;

    }

    //用来获取RecyclerView的输入数据
    public void runMyTask(){
        String[] ss = {"1","2","11","21","22","23","24","25"};//榜单ID
        MyTasks task = new MyTasks();
        task.execute(ss);
    }

    //用来设置RecyclerView的参数
    public void setRecyclerView(){
        RecyclerView recyclerView = view.findViewById(R.id.onlinerecyclerview);
        OnlineRecyclerAdapter onlineRecyclerAdapter = new OnlineRecyclerAdapter(getContext(),R.layout.recyclerview_online_item,charts);//自定义的RecyclerView的Adapter
        recyclerView.setAdapter(onlineRecyclerAdapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(linearLayoutManager);
    }

    //定义的线程内部类
    class MyTasks extends AsyncTask<String,Chart,Void> {
        @Override
        protected Void doInBackground(String... strings) {
            Chart chart = null;//榜单对象，用来存储榜单的信息
            try {
                for (int i=0;i<strings.length;i++){
                    URL url = new URL("http://tingapi.ting.baidu.com/v1/restserver/ting?format=xml&calback=&method=baidu.ting.billboard.billList&type="
                            +strings[i]+"&size=10&offset=0");//设置榜单的URL
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("GET");
                    InputStream inputStream = connection.getInputStream();
                    chart = xmlPullParse(inputStream,strings[i]);//将inputstream的内容解析并返回一个Chart对象
                    publishProgress(chart);
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (XmlPullParserException e) {
                e.printStackTrace();
            }
            return null;
        }
        @Override
        protected void onProgressUpdate(Chart... values) {
            super.onProgressUpdate(values);
            charts.add(values[0]);
            setRecyclerView();//设置RecyclerView
        }
    }

    //解析inputstream并返回一个chart对象
    public Chart xmlPullParse(InputStream inputStream,String chart_id) throws XmlPullParserException, IOException {
        Chart chart;
        String defaultPath = "http://business0.qianqian.com/qianqian/file/5bfe504b1c299_491.png";
        Bitmap picture = getBitmap(defaultPath);//设置一个默认地址
        String chartname = "";
        String[] song = {"","",""};
        String[] singer = {"","",""};

        XmlPullParser parser = null;
        parser = XmlPullParserFactory.newInstance().newPullParser();
        parser.setInput(inputStream,"UTF-8");

        int evenType = parser.getEventType();
        int i = 0;//获取歌曲信息的次数
        while(evenType != parser.END_DOCUMENT && i<3){
            if(evenType == parser.START_TAG && parser.getName().equals("title")) {
                song[i] = parser.nextText();
            }else if (evenType == parser.START_TAG && parser.getName().equals("author")){
                singer[i] = parser.nextText();
                i++;
            }
            evenType = parser.next();
        }
        while(evenType != parser.END_DOCUMENT){
            if(evenType == parser.START_TAG && parser.getName().equals("pic_s192")) {//获取榜单图片
                picture = getBitmap(parser.nextText());
            }else if(evenType == parser.START_TAG && parser.getName().equals("name")){//获取榜单名
                chartname = parser.nextText();
            }
            evenType = parser.next();
        }
        chart = new Chart(chart_id,picture,chartname,song[0],singer[0],song[1],singer[1],song[2],singer[2]);
        return chart;
    }

    //将网络图片url转换成Bitmap格式
    public Bitmap getBitmap(String url){
        Bitmap bmap = null;
        try {
            URL myurl = new URL(url);
            HttpURLConnection connection = (HttpURLConnection) myurl.openConnection();
            connection.setConnectTimeout(6000);
            connection.setDoInput(true);
            connection.setUseCaches(false);
            connection.connect();
            InputStream in = connection.getInputStream();
            bmap = BitmapFactory.decodeStream(in);
            in.close();
        }catch (Exception e){
            e.printStackTrace();
        }
        return bmap;
    }
}
