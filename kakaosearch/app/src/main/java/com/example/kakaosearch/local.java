package com.example.kakaosearch;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
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

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class local extends AppCompatActivity {
    String url = "https://dapi.kakao.com/v2/local/search/keyword.json";
    String query = "선문";
    int size = 10;
    int page = 1;
    ArrayList<HashMap<String, String>> arraylist;
    Myadapter ad;
    ListView list;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        list=findViewById(R.id.list);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("지역검색");
        new daumthread().execute();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.mainmenu, menu);
        MenuItem search=menu.findItem(R.id.search2);
        SearchView searchView = (SearchView) search.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                query = s;
                page = 1;
                new daumthread().execute();
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
            case android.R.id.home:
                finish();
                break;

        }
        return super.onOptionsItemSelected(item);
    }



    //다음 쓰레드
    class daumthread extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... strings) {
            String result = Daum.connect(url + "?query=" + query + "&size=" + size);
            //System.out.println("출력" + result);
            return result;
        }

        @Override
        protected void onPostExecute(String s) {
            parsing(s);
            System.out.println("갯수"+arraylist.size());
            ad=new Myadapter();
            list.setAdapter(ad);
            super.onPostExecute(s);
        }
    }


    public void parsing(String result) {
        arraylist = new ArrayList<>();
        try {
            JSONArray jarray = new JSONObject(result).getJSONArray("documents");
            for (int i = 0; i < jarray.length(); i++) {
                JSONObject obj = jarray.getJSONObject(i);
                HashMap<String, String> map = new HashMap<>();
                map.put("name", obj.getString("place_name"));
                map.put("address", obj.getString("address_name"));
                map.put("tel", obj.getString("phone"));
                map.put("x", obj.getString("x"));
                map.put("y", obj.getString("y"));
                arraylist.add(map);
            }
        } catch (Exception e) {
            System.out.println("오류" + e.getMessage());
        }
    }

    class Myadapter extends BaseAdapter{

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
            final HashMap<String ,String> map=arraylist.get(i);
            view=getLayoutInflater().inflate(R.layout.localitem,null);
            TextView name=view.findViewById(R.id.name2);
            TextView address=view.findViewById(R.id.address);
            TextView phone=view.findViewById(R.id.phone);

            name.setText(map.get("name"));
            address.setText(map.get("address"));
            phone.setText(map.get("tel"));
            phone.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent call=new Intent(Intent.ACTION_DIAL, Uri.parse("tel:"+map.get("tel")));
                    startActivity(call);
                }
            });

            ImageView img=view.findViewById(R.id.map);
            img.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent=new Intent(local.this,MapsActivity.class);
                    intent.putExtra("x",map.get("x"));
                    intent.putExtra("y",map.get("y"));
                    intent.putExtra("title",map.get("name"));
                    //System.out.println("위치 x="+map.get("x")+", y="+map.get("y"));
                    startActivity(intent);
                }
            });
            return view;
        }
    }
}
