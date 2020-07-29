package com.example.kakaosearch;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.media.Image;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    String query = "토탈워";//검색어
    int size = 10;
    int page = 1;
    String url = "https://dapi.kakao.com/v2/search/image?query=";//이미지 검색

    TextView text;
    ArrayList<HashMap<String, String>> arraylist;
    Myadapter ad;
    ListView list;
    ProgressBar progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        progress=findViewById(R.id.progres);
        text = findViewById(R.id.retext);
        new DaumThread().execute();
        list = findViewById(R.id.list);

        getSupportActionBar().setTitle("이미지검색");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        FloatingActionButton more = findViewById(R.id.flobtn);
        more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (page + 1 <= 50) {
                    page += 1;
                    new DaumThread().execute();
                } else {
                    AlertDialog.Builder box = new AlertDialog.Builder(MainActivity.this);
                    box.setTitle("오류");
                    box.setMessage("마지막 페이지입니다");
                    box.setPositiveButton("확인", null);
                    box.show();
                }
            }

        });
        FloatingActionButton before = findViewById(R.id.flobtn2);
        before.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (page - 1 >= 1) {
                    page -= 1;
                    new DaumThread().execute();
                } else {
                    AlertDialog.Builder box = new AlertDialog.Builder(MainActivity.this);
                    box.setTitle("오류");
                    box.setMessage("처음 페이지 입니다");
                    box.setPositiveButton("확인", null);
                    box.show();
                }
            }

        });
    }

    //daum 쓰레드(메인에서 바로 웹을 연결하면 안됨)
    class DaumThread extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            progress.setVisibility(View.VISIBLE);
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... strings) {
            String result = Daum.connect(url + query + "&size=" + size + "&page=" + page);

            return result;
        }

        @Override
        protected void onPostExecute(String s) {
            parsing(s);
            //System.out.println("갯수"+arraylist.size());
            ad = new Myadapter();
            list.setAdapter(ad);
            progress.setVisibility(View.INVISIBLE);
            super.onPostExecute(s);
        }
    }

    //파싱==데이터 분석
    public void parsing(String result) {
        arraylist = new ArrayList<>();
        try {
            JSONArray jarray = new JSONObject(result).getJSONArray("documents");
            for (int i = 0; i < jarray.length(); i++) {
                JSONObject obj = jarray.getJSONObject(i);
                HashMap<String, String> map = new HashMap<>();
                map.put("name", obj.getString("display_sitename"));
                map.put("thumb", obj.getString("thumbnail_url"));
                map.put("link", obj.getString("doc_url"));
                map.put("img", obj.getString("image_url"));
                arraylist.add(map);
            }
        } catch (Exception e) {
            System.out.println("오류" + e.getMessage());
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
            final HashMap<String, String> map = arraylist.get(i);
            TextView name = view.findViewById(R.id.sitename);
            ImageView img = view.findViewById(R.id.thumb);

            name.setText(map.get("name"));
            Picasso.with(MainActivity.this).load(map.get("thumb")).into(img);

            img.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    AlertDialog.Builder box = new DatePickerDialog.Builder(MainActivity.this);
                    box.setTitle("이미지");
                    box.setPositiveButton("확인", null);
                    LinearLayout layout = (LinearLayout) getLayoutInflater().inflate(R.layout.item_bigimg, null);

                    ImageView img2=layout.findViewById(R.id.img2);
                    Picasso.with(MainActivity.this).load(map.get("img")).into(img2);
                    box.setView(layout);

                    box.show();
                }
            });
            return view;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.mainmenu, menu);
        MenuItem search = menu.findItem(R.id.search2);
        SearchView searchView = (SearchView) search.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                query = s;
                page = 1;
                new DaumThread().execute();
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
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                break;

            case R.id.local:
                Intent intent=new Intent(MainActivity.this,local.class);
                startActivity(intent);
                finish();
                break;

            case R.id.blog:
                finish();
                break;

            case R.id.imgbtn:
                finish();
                break;

        }
        return super.onOptionsItemSelected(item);
    }
}
