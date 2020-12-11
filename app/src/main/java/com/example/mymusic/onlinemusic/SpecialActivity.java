package com.example.mymusic.onlinemusic;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.mymusic.common.Chart;
import com.example.mymusic.common.MainActivity;
import com.example.mymusic.common.Music;
import com.example.mymusic.R;
import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class SpecialActivity extends AppCompatActivity {
    private ImageView goback;
    private ImageView chartmrg;
    private TextView charttitle;
    private TextView chartname;
    private TextView update_date;
    private TextView comment;
    private RecyclerView recyclerView;
    private SpecialRecyclerAdapter specialAdaper;
    private List<Music> musics;
    private String chart_id;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.online_special);
        Intent intent = getIntent();
        chart_id = intent.getStringExtra("chart_id");//获取榜单ID
        init();
    }

    public void init(){//对控件进行初始化，调用AsyncTask对象
        goback = findViewById(R.id.goback);
        chartmrg = findViewById(R.id.chartmrg);
        charttitle = findViewById(R.id.charttitle);
        chartname = findViewById(R.id.chartname);
        update_date = findViewById(R.id.update_date);
        comment = findViewById(R.id.chartcomment);
        musics = new ArrayList<Music>();

        new MyTasks().execute(chart_id);


        //在此为左上角的返回图标设置了一个点击事件，点击后返回上一个界面
        goback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SpecialActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }

    class MyTasks extends AsyncTask<String,Chart,Void>{

        @Override
        protected Void doInBackground(String... strings) {
            Chart chart = null;//榜单对象，用来储存榜单的信息
            try {
                URL url = new URL("http://tingapi.ting.baidu.com/v1/restserver/ting?format=xml&calback=&method=baidu.ting.billboard.billList&type="
                        +strings[0]+"&size=10&offset=0");//设置榜单的URL
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();//获取连接
                connection.setRequestMethod("GET");
                InputStream inputStream = connection.getInputStream();
                chart = xmlPullParse(inputStream);//将inputstream的内容进行xml解析并返回一个Chart对象
                publishProgress(chart);
                inputStream.close();

            }catch (Exception e){
                e.printStackTrace();
            }
            return null;
        }

        @Override
        //设置榜单的简介信息
        protected void onProgressUpdate(Chart... values) {
            super.onProgressUpdate(values);
            musics = values[0].getMusics();//用于设置列表的音乐数据
            charttitle.setText("百度"+values[0].getChartname());
            chartmrg.setImageBitmap(values[0].getPicturesrc());
            chartname.setText(values[0].getChartname());
            update_date.setText(values[0].getUndate_date());
            comment.setText(values[0].getComment());
            setRecyclerView(musics);
        }

        //进行xml解析的方法
        public Chart xmlPullParse(InputStream inputStream) throws XmlPullParserException, IOException {
            Chart chart;
            List<Music> musics = new ArrayList<>();
            Music music;
            String defaultPath = "http://business0.qianqian.com/qianqian/file/5bfe504b1c299_491.png";
            Bitmap picture = getBitmap(defaultPath);//设置图片的默认地址
            String songId = null;
            String songname = null;
            String singer = null;
            String chartname = null;
            String update_date =null;
            String comment = null;

            //设置Pull解析对象，并实例化
            XmlPullParser parser = null;
            parser = XmlPullParserFactory.newInstance().newPullParser();
            parser.setInput(inputStream,"UTF-8");

            int evenType = parser.getEventType();
            while(evenType != parser.END_DOCUMENT){
                if(evenType == parser.START_TAG && parser.getName().equals("pic_small")) {//获取歌曲专辑图片
                    picture = getBitmap(parser.nextText());
                }else if (evenType == parser.START_TAG && parser.getName().equals("song_id")){//获取歌曲id
                    songId = parser.nextText();
                }else if (evenType == parser.START_TAG && parser.getName().equals("title")){//获取歌名
                    songname = parser.nextText();
                }else if(evenType == parser.START_TAG && parser.getName().equals("author")){//获取歌手
                    singer = parser.nextText();
                    String[] st = getDurandLink(songId);    //通过自定义的getDuraandLink（）方法返回歌曲id对应的播放时间和播放地址
                    music = new Music(picture,songname,singer,st[0],st[1]);//实例一个音乐对象，并添加到音乐数组里
                    musics.add(music);
                }else if(evenType == parser.START_TAG && parser.getName().equals("update_date")) {//获取榜单更新时间
                    update_date = parser.nextText();
                }else if(evenType == parser.START_TAG && parser.getName().equals("name")) {//获取榜单名
                    chartname = parser.nextText();
                }else if(evenType == parser.START_TAG && parser.getName().equals("comment")){//获取榜单简介
                    comment = parser.nextText();
                }else if(evenType == parser.START_TAG && parser.getName().equals("pic_s192")){//获取榜单图片
                    picture = getBitmap(parser.nextText());
                }
                evenType = parser.next();
            }
            chart = new Chart(picture,chartname,update_date,comment,musics);//实例一个榜单对象
            return chart;
        }

        //将网络图片url转换成Bitmap格式
        public Bitmap getBitmap(String url){
            Bitmap bmap = null;
            try {
                URL myurl = new URL(url);
                HttpURLConnection connection = (HttpURLConnection) myurl.openConnection();
                connection.setConnectTimeout(60);
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

        //设置列表的方法
        private void setRecyclerView(List<Music> musics){
            recyclerView = findViewById(R.id.speciallist);
            specialAdaper = new SpecialRecyclerAdapter(SpecialActivity.this,R.layout.online_special_item,musics);
            recyclerView.setAdapter(specialAdaper);
            LinearLayoutManager layoutManager = new LinearLayoutManager(SpecialActivity.this);
            recyclerView.setLayoutManager(layoutManager);
        }

        //根据传入的歌曲id获取播放时长和播放地址
        private String[] getDurandLink(String songId) throws IOException {
            String[] st = new String[2];//存放歌曲时长和播放地址
            InputStream inputStream = null;
            BufferedReader reader = null;
            try {
                URL url = new URL("http://tingapi.ting.baidu.com/v1/restserver/ting?format=xml&calback=&method=baidu.ting.song.play&songid="+songId);//设置榜单的URL
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();//获取连接
                connection.setRequestMethod("GET");
                inputStream = connection.getInputStream();

                reader = new BufferedReader(new InputStreamReader(inputStream));

                String result = "";
                StringBuilder builder = new StringBuilder();
                while((result = reader.readLine())!= null){
                    builder.append(result);
                }

                //进行JSON解析
                String text = builder.toString();
                JSONObject jsonObject = new JSONObject(text);
                JSONObject jsonObject1 = (JSONObject) jsonObject.get("bitrate");
                for (int i = 0; i< jsonObject1.length(); i++) {
                    // 用getXXX方法取出对应键值
                    int a = Integer.parseInt(jsonObject1.getString("file_duration"));
                    st[0] = (a/60)+":"+a%60;//将歌曲时长转化为00：00格式并存入数组中
                    st[1] = jsonObject1.getString("file_link");
                }
            }catch (MalformedURLException e){
                e.printStackTrace();
            }catch (ProtocolException e){
                e.printStackTrace();
            }catch (Exception e){
                e.printStackTrace();
            }finally {
                //释放资源
                reader.close();
                inputStream.close();
            }

            return st;
        }

    }
}
