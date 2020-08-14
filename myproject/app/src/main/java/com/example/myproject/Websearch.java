package com.example.myproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class Websearch extends AppCompatActivity {
    String url = "https://dapi.kakao.com/v2/search/web";
    String query = "확인";
    int page = 1;
    ArrayList<HashMap<String, String>> array;
    int what = 0;
    int total = 0;
    boolean is_end = false;
    Daumweather daumweather;
    EditText searchedit;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_websearch);
        Intent intent=getIntent();
        searchedit = findViewById(R.id.searchedit);
        ImageView serchbtn = findViewById(R.id.searchbtn2);
        getSupportActionBar().setTitle("검색창");
        serchbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                page = 1;

                query = searchedit.getText().toString();
                if (query == null || query.equals("")) {
                    AlertDialog.Builder box = new AlertDialog.Builder(Websearch.this);
                    box.setTitle("오류");
                    box.setMessage("검색 내용이 없습니다");
                    box.setPositiveButton("확인",null);
                    box.show();
                } else {
                    new SearchThread().execute();
                }
            }
        });

        searchedit.setOnKeyListener(new View.OnKeyListener()
        {
            public boolean onKey(View v, int keyCode, KeyEvent event)
            {
                if (event.getAction() == KeyEvent.ACTION_DOWN)
                {
                    switch (keyCode)
                    {
                        case KeyEvent.KEYCODE_DPAD_CENTER:
                        case KeyEvent.KEYCODE_ENTER:
                            page = 1;

                            query = searchedit.getText().toString();
                            if (query == null || query.equals("")) {
                                AlertDialog.Builder box = new AlertDialog.Builder(Websearch.this);
                                box.setTitle("오류");
                                box.setMessage("검색 내용이 없습니다");
                                box.setPositiveButton("확인",null);
                                box.show();
                            } else {
                                new SearchThread().execute();
                            }
                            return true;
                        default:
                            break;
                    }
                }
                return false;
            }
        });

        FloatingActionButton btnbefore = findViewById(R.id.btnbefore);
        FloatingActionButton btnnext = findViewById(R.id.btnnext);

        btnbefore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (page == 1) {
                    Toast.makeText(Websearch.this, "첫번째 페이지 입니다", Toast.LENGTH_SHORT).show();
                } else {
                    page--;
                    EditText searchedit = findViewById(R.id.searchedit);
                    query = searchedit.getText().toString();
                    if (query == null || query.equals("")) {
                        AlertDialog.Builder box = new AlertDialog.Builder(Websearch.this);
                        box.setTitle("오류");
                        box.setMessage("검색 내용이 없습니다");
                        box.setPositiveButton("확인",null);
                        box.show();
                    } else {
                        new SearchThread().execute();
                    }
                }
            }
        });

        btnnext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (is_end) {
                    Toast.makeText(Websearch.this, "마지막 페이지 입니다", Toast.LENGTH_SHORT).show();
                } else {
                    page++;
                    EditText searchedit = findViewById(R.id.searchedit);
                    query = searchedit.getText().toString();
                    if (query == null || query.equals("")) {
                        AlertDialog.Builder box = new AlertDialog.Builder(Websearch.this);
                        box.setTitle("오류");
                        box.setMessage("검색 내용이 없습니다");
                        box.setPositiveButton("확인",null);
                        box.show();
                    } else {
                        new SearchThread().execute();
                    }
                }
            }
        });
        daumweather = new Daumweather(getSupportActionBar());
        daumweather.setIndex(intent.getIntExtra("index",0));
        daumweather.execute();
    }

    class SearchThread extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... strings) {
            String s = Daum.connect(url + "?query=" + query + "&page=" + page);

            return s;
        }

        @Override
        protected void onPostExecute(String s) {
            parsing(s);
            System.out.println("데이터 갯수" + array.size());
            Myadapter ad = new Myadapter();
            ad.notifyDataSetChanged();
            RecyclerView list = findViewById(R.id.weblist);
            list.setLayoutManager(new LinearLayoutManager(Websearch.this));
            list.setHasFixedSize(true);
            list.setAdapter(ad);
            super.onPostExecute(s);
        }
    }

    public void parsing(String s) {
        try {
            array = new ArrayList<HashMap<String, String>>();
            JSONObject jarray = new JSONObject(s).getJSONObject("meta");
            is_end = jarray.getBoolean("is_end");
            JSONArray jarray2 = new JSONObject(s).getJSONArray("documents");
            for (int i = 0; i < jarray2.length(); i++) {
                JSONObject obj = jarray2.getJSONObject(i);
                HashMap<String, String> map = new HashMap<>();
                map.put("title", obj.getString("title"));
                map.put("contents", obj.getString("contents"));
                map.put("url", obj.getString("url"));

                array.add(map);
            }

            getSupportActionBar().setTitle("검색창  "+page);
        } catch (Exception e) {
            System.out.println("파싱오류" + e.getMessage());
        }
    }

    class Myadapter extends RecyclerView.Adapter<Myadapter.ViewHolder> {
        @NonNull
        @Override
        public Myadapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = getLayoutInflater().inflate(R.layout.web_item, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull Myadapter.ViewHolder holder, int position) {
            final HashMap<String, String> map = array.get(position);
            holder.title.setText(Html.fromHtml(map.get("title")));
            holder.content.setText(map.get("contents"));
            holder.weblayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(Websearch.this, web.class);
                    intent.putExtra("url", map.get("url"));
                    startActivity(intent);
                }
            });
        }

        @Override
        public int getItemCount() {
            return array.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            TextView title, content;
            LinearLayout weblayout;

            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                title = itemView.findViewById(R.id.webtitle);
                content = itemView.findViewById(R.id.webcontent);
                weblayout = itemView.findViewById(R.id.weblayout);
            }
        }
    }
}
