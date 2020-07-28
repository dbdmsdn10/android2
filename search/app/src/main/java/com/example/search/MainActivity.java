package com.example.search;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    ArrayList<HashMap<String, String>> arraylist;
    Myadapter ad;
    ListView list;
    ProgressDialog progress;
    int display = 5;
    String query = "줄기세포";
    String url = "https://openapi.naver.com/v1/search/news.json";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        list = findViewById(R.id.list);
        new NaverThread().execute();
        getSupportActionBar().setTitle("뉴스검색");
        try {
            Intent intent2 = getIntent();
            String url2 = intent2.getStringExtra("url");
            if (url != null && !url2.equals("")) {
                url = url2;
            }
        } catch (Exception e) {
        }
        FloatingActionButton more = findViewById(R.id.morebtn);
        more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                display += 5;
                new NaverThread().execute();
            }
        });
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                HashMap<String, String> map = arraylist.get(i);
                String title = map.get("title");
                String link = map.get("link");
                Intent intent = new Intent(MainActivity.this, Detail.class);
                intent.putExtra("title", title);
                intent.putExtra("link", link);

                startActivity(intent);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.mainmenu, menu);
        MenuItem search = menu.findItem(R.id.search);
        SearchView searchView = (SearchView) search.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                query = s;
                new NaverThread().execute();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.news:
                url = "https://openapi.naver.com/v1/search/news.json";
                getSupportActionBar().setTitle("뉴스검색");
                new NaverThread().execute();
                break;
            case R.id.blog:
                url = "https://openapi.naver.com/v1/search/blog.json";
                getSupportActionBar().setTitle("블로그검색");
                new NaverThread().execute();
                break;
            case R.id.cafe:
                url = "https://openapi.naver.com/v1/search/cafearticle.json";
                getSupportActionBar().setTitle("카페검색");
                new NaverThread().execute();
                break;
            case R.id.book:
                Intent intent = new Intent(MainActivity.this, Book.class);
                startActivity(intent);
                new NaverThread().execute();
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    //네이버 접속 쓰레드
    class NaverThread extends AsyncTask<String, String, String> {//네이버에 접속 및 값 가져오기

        @Override
        protected void onPreExecute() {//가장 먼저 시작됨
            progress = new ProgressDialog(MainActivity.this);
            progress.setMessage("검색중입니다");
            progress.show();
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... strings) {
            String result = NAVER.connect(display, query, url);
            return result;
        }

        @Override
        protected void onPostExecute(String s) {//위의 return값을 받는곳
            parserJSON(s);
            // System.out.println("크기는"+arraylist.size());
            ad = new Myadapter();
            list.setAdapter(ad);
            progress.dismiss();//더이상 안보이게
            super.onPostExecute(s);
        }
    }

    //결봐 파싱=정보분석
    public void parserJSON(String result) {
        arraylist = new ArrayList<HashMap<String, String>>();
        try {
            JSONArray jarray = new JSONObject(result).getJSONArray("items");
            for (int i = 0; i < jarray.length(); i++) {
                JSONObject obj = (JSONObject) jarray.get(i);
                String title = obj.getString("title");
                String link = obj.getString("link");
                String description = obj.getString("description");
                HashMap<String, String> map = new HashMap<String, String>();
                map.put("title", title);
                map.put("link", link);
                map.put("description", description);
                arraylist.add(map);
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    class Myadapter extends BaseAdapter {

        @Override
        public int getCount() {
            return arraylist.size();
        }

        @Override
        public Object getItem(int i) {
            return null;
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            view = getLayoutInflater().inflate(R.layout.item, null);
            TextView title = view.findViewById(R.id.title2);
            TextView description = view.findViewById(R.id.description);
            HashMap<String, String> map = arraylist.get(i);
            title.setText(Html.fromHtml(map.get("title")));
            description.setText(map.get("description"));
            return view;
        }

    }
}
