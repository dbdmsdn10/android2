package com.example.myproject;


import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;

import java.util.ArrayList;
import java.util.HashMap;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;


class Daumweather extends AsyncTask<String, String, String> {
    ArrayList<HashMap<String, String>> arrayDaum = new ArrayList<>();
    int index = 0;
    ActionBar actionBar;
    String name;
    boolean thread=true;
    public Daumweather(ActionBar actionBar) {
        this.actionBar = actionBar;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }


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
        //System.out.println("갯수"+arrayDaum.size());
        name = actionBar.getTitle().toString();
        BackThread backThread = new BackThread();
        backThread.start();
    }

    class BackThread extends Thread {
        @Override
        public void run() {
            super.run();

            while (thread) {
                handler.sendEmptyMessage(0);
                index++;
                if (index == arrayDaum.size()) {
                    index = 0;
                }
                try {
                    Thread.sleep(3000);
                } catch (Exception e) {

                }
            }
        }
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            try {
                super.handleMessage(msg);
                HashMap<String, String> map = arrayDaum.get(index);
                String part = map.get("part");
                String temper = map.get("temper");

                String ico = map.get("ico");
                actionBar.setTitle(name + "       " + part + "   " + temper + "°C   " + ico);
            } catch (Exception e) {
                thread=false;
                Daumweather daumweather=new Daumweather(actionBar);
                daumweather.execute();
            }
        }
    };
}


