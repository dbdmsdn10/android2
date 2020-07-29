package com.example.search;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class Book extends AppCompatActivity {
    ArrayList<HashMap<String, String>> arrayList;
    int display = 5;
    String query = "안드로이드";
    String url = "https://openapi.naver.com/v1/search/book.json";
    MyAdapter ad;
    ListView list;
    ProgressDialog progress;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book);
        getSupportActionBar().setTitle("도서검색");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        new NaverThred().execute();
        list=findViewById(R.id.list);
        FloatingActionButton more=findViewById(R.id.more2);
        more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                display+=5;
                new NaverThred().execute();
            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        Intent intent=new Intent(Book.this,MainActivity.class);
        switch (item.getItemId())
        {
            case android.R.id.home:
                finish();
                break;
            case R.id.blog:
                intent.putExtra("url","https://openapi.naver.com/v1/search/blog.json");
                intent.putExtra("title","블로그검색");
                startActivity(intent);
                finish();
                break;
            case R.id.news:
                intent.putExtra("url","https://openapi.naver.com/v1/search/news.json");
                intent.putExtra("title","뉴스검색");
                startActivity(intent);
                finish();
                break;
            case R.id.cafe:
                intent.putExtra("url","https://openapi.naver.com/v1/search/cafearticle.json");
                intent.putExtra("title","카페검색");
                startActivity(intent);
                finish();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    //네이버 접속쓰레드
    class NaverThred extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            progress=new ProgressDialog(Book.this);
            progress.setMessage("검색중");
            progress.show();
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... strings) {
            String result = NAVER.connect(display, query, url);
           // System.out.println(result);
            return result;
        }

        @Override
        protected void onPostExecute(String s) {
            parsing(s);
           // System.out.println("갯수확인"+arrayList.size());
            ad=new MyAdapter();
            list.setAdapter(ad);
            progress.dismiss();
            super.onPostExecute(s);
        }
    }

    //파싱
    public void parsing(String result) {
        arrayList = new ArrayList<HashMap<String, String>>();
        try {
            JSONArray jarray=new JSONObject(result).getJSONArray("items");
            for(int i=0;i<jarray.length();i++){
                HashMap<String,String> map=new HashMap<>();
                JSONObject obj=jarray.getJSONObject(i);
                map.put("title",obj.getString("title"));
                map.put("author",obj.getString("author"));
                map.put("price",obj.getString("price"));
                map.put("image",obj.getString("image"));
                map.put("description",obj.getString("description"));
                map.put("publisher",obj.getString("publisher"));
                map.put("pubdate",obj.getString("pubdate"));
                arrayList.add(map);
            }
        } catch (Exception e) {
            System.out.println("오류"+e.getMessage());
        }
    }

    //어뎁터
    class MyAdapter extends BaseAdapter{
        @Override
        public int getCount() {
            return arrayList.size();
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
            view=getLayoutInflater().inflate(R.layout.item_book,null);
            TextView title=view.findViewById(R.id.title4);
            TextView price=view.findViewById(R.id.price);
            TextView author=view.findViewById(R.id.author);
            final HashMap<String,String> map=arrayList.get(i);

            ImageView image=view.findViewById(R.id.image2);
            title.setText(map.get("title"));
            price.setText(map.get("price")+"원");
            author.setText(map.get("author"));

            Picasso.with(Book.this).load(map.get("image")).into(image);

            view.findViewById(R.id.showmore).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent=new Intent(Book.this,Book_detail.class);
                    intent.putExtra("title",map.get("title"));
                    intent.putExtra("price",map.get("price"));
                    intent.putExtra("author",map.get("author"));
                    intent.putExtra("description",map.get("description"));
                    intent.putExtra("image",map.get("image"));
                    intent.putExtra("publisher",map.get("publisher"));
                    intent.putExtra("pubdate",map.get("pubdate"));


                    startActivity(intent);
                }
            });
            return view;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.mainmenu,menu);
        MenuItem search=menu.findItem(R.id.search);
        SearchView searchView= (SearchView) search.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                query=s;
                new NaverThred().execute();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }
}
