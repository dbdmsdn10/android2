package com.example.crawling;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;


import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {
    ArrayList<HashMap<String, String>> arrayCGV = new ArrayList<>();
    ArrayList<HashMap<String, String>> arrayDaum = new ArrayList<>();
    int index;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        new cgv().execute();
        new DatumWeahter().execute();
    }

    class cgv extends AsyncTask<String, String, String> {//실제 네트워크 접속

        @Override
        protected String doInBackground(String... strings) {
            try {
                Document doc = (Document) Jsoup.connect("http://www.cgv.co.kr/movies").get();
                Elements elements = doc.select(".sect-movie-chart ol");
                for (Element e : elements.select("li")) {
                    HashMap<String, String> map = new HashMap<String, String>();

                    map.put("rank", e.select(".rank").text());
                    map.put("title", e.select(".title").text());
                    map.put("image", e.select("img").attr("src"));

                    if (!e.select(".rank").text().equals("")) {
                        arrayCGV.add(map);
                    }
                }
            } catch (Exception e) {
                System.out.println(e.toString());
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            //System.out.println("데이터 갯수"+arrayCGV.size());
            CgvAdapter cgvAdapter;
            RecyclerView listCGV = findViewById(R.id.listcgv);
            listCGV.setLayoutManager(new LinearLayoutManager(MainActivity.this));
            cgvAdapter = new CgvAdapter(MainActivity.this, arrayCGV);
            listCGV.setAdapter(cgvAdapter);
        }
    }

    class DatumWeahter extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... strings) {
            try {
                HashMap<String, Object> object = new HashMap<String, Object>();
                Document doc = Jsoup.connect("http://www.daum.net").get();
                Elements elements = doc.select(".list_weather");
                for (Element e : elements.select("li")) {
                    HashMap<String, String> map = new HashMap<String, String>();
                    map.put("part", e.select(".txt_part").text());
                    map.put("temper", e.select(".txt_temper").text());
                    map.put("ico", e.select(".ico_ws").text());
                    arrayDaum.add(map);
                }
            } catch (Exception e) {
                System.out.println(e.toString());
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            System.out.println("갯수"+arrayDaum.size());
            getSupportActionBar().setTitle("다음 날씨");
            BackThread backThread=new BackThread();
            backThread.setDaemon(true);//위의 쓰레드가 죽으면 이거도 죽으라는 의미
            backThread.start();
        }
    }

    class BackThread extends Thread{
        @Override
        public void run() {
            super.run();
            index=0;
            while(true){
                handler.sendEmptyMessage(0);
                index++;
                if(index==arrayDaum.size()){
                    index=0;
                }
                try{Thread.sleep(1000);}catch(Exception e){}
            }
        }
    }

    Handler handler=new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            HashMap<String,String> map=arrayDaum.get(index);
            String part=map.get("part");
            String temper=map.get("temper");

            String ico=map.get("ico");
            getSupportActionBar().setTitle(part +"   "+ temper +"°C   "+ ico);
        }
    };
}
