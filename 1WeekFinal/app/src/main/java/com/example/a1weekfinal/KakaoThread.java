package com.example.a1weekfinal;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;

public class KakaoThread extends AsyncTask<String, String, String> {
    String url = "https://dapi.kakao.com/v2/search/blog";
    String query = "게임";
    int page = 1;
    ArrayList<HashMap<String, String>> array = new ArrayList<>();
    int what = 0;
    FragmentActivity activity2;
    int total = 0;
    boolean is_end = false;

    public void set(String url, String query, int page, FragmentActivity activity, int what) {
        this.url = url;
        this.query = query;
        this.page = page;
        this.activity2 = activity;
        this.what = what;
    }

    @Override
    protected String doInBackground(String... strings) {
        String s = "";
        switch (what) {
            case 1:
                s = Daum.connect(url + "?query=" + query + "&page=" + page);

                break;
            case 2:
                s = Daum.connect(url + "&query=" + query + "&page=" + page);
                break;
            case 3:
                s = Daum.connect(url + "?query=" + query + "&page=" + page);
                break;
        }
        //System.out.println(s);

        return s;
    }

    @Override
    protected void onPostExecute(String s) {
        RecyclerView list = null;
        switch (what) {
            case 1:
                list = activity2.findViewById(R.id.listBlog);
                break;
            case 2:
                list = activity2.findViewById(R.id.listBook);
                break;
            case 3:
                list = activity2.findViewById(R.id.listLocal);
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + what);
        }
        list.setLayoutManager(new LinearLayoutManager(activity2));
        list.setHasFixedSize(true);
        switch (what) {
            case 1:
                blogparsing(s);
                Blogadapter ad=new Blogadapter();
                ad.notifyDataSetChanged();
                list.setAdapter(ad);
                break;
            case 2:
                Bookparsing(s);
                Bookadapter ad2=new Bookadapter();
                list.setAdapter(ad2);
                break;
            case 3:
                Localparser(s);
                Localadapter ad3=new Localadapter();
                list.setAdapter(ad3);
                break;
        }
       // System.out.println("데이터 갯수"+array.size());
        super.onPostExecute(s);
    }

    //------------------------------------------------------------------------------------------
    public void blogparsing(String s) {
        array = new ArrayList<>();
        try {
            JSONObject jarray = new JSONObject(s).getJSONObject("meta");
            total = jarray.getInt("pageable_count");
            is_end = jarray.getBoolean("is_end");


            JSONArray jarray2 = new JSONObject(s).getJSONArray("documents");
            for (int i = 0; i < jarray2.length(); i++) {
                JSONObject obj = jarray2.getJSONObject(i);
                HashMap<String, String> map = new HashMap<>();
                map.put("title", obj.getString("title"));
                map.put("url", obj.getString("url"));
                array.add(map);
            }
            TextView to=activity2.findViewById(R.id.txtTotal);
            to.setText("검색수="+total+"   마지막인지"+is_end);
            FloatingActionButton flotin=activity2.findViewById(R.id.btnMore);
            flotin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(!is_end){
                        page+=1;
                        KakaoThread kaka=new KakaoThread();
                        url="https://dapi.kakao.com/v2/search/blog";
                        kaka.set(url,  query,  page, activity2,  1);
                        kaka.execute();
                    }
                }
            });

        } catch (Exception e) {
            System.out.println("블로그 오류발생"+e.getMessage());
        }
    }

    class Blogadapter extends RecyclerView.Adapter<Blogadapter.ViewHolder>{
        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view=activity2.getLayoutInflater().inflate(R.layout.item_blog,parent,false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            final HashMap<String,String> map=array.get(position);
            holder.title.setText(Html.fromHtml(map.get("title")));
            holder.link.setText(map.get("url"));
            holder.bloglayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent=new Intent(activity2,Web.class);
                    intent.putExtra("url",map.get("url"));
                    intent.putExtra("title",map.get("title"));
                    activity2.startActivity(intent);
                }
            });
        }

        @Override
        public int getItemCount() {
            return array.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            TextView title,link;
            LinearLayout bloglayout;
            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                title=itemView.findViewById(R.id.title);
                link=itemView.findViewById(R.id.link);
                bloglayout=itemView.findViewById(R.id.bloglayout);
            }
        }
    }

    //-------------------------------------------------------------------------------------------
    public void Bookparsing(String s) {
        array = new ArrayList<>();
        try {
            JSONObject jarray = new JSONObject(s).getJSONObject("meta");
            total = jarray.getInt("pageable_count");
            is_end = jarray.getBoolean("is_end");


            JSONArray jarray2 = new JSONObject(s).getJSONArray("documents");
            for (int i = 0; i < jarray2.length(); i++) {
                JSONObject obj = jarray2.getJSONObject(i);
                HashMap<String, String> map = new HashMap<>();
                map.put("title", obj.getString("title"));
                map.put("price", obj.getString("price"));
                map.put("thumbnail", obj.getString("thumbnail"));
                map.put("contents", obj.getString("contents"));
                map.put("publisher", obj.getString("publisher"));

                array.add(map);
            }
            TextView to=activity2.findViewById(R.id.txtTotal2);
            to.setText("검색수="+total+"   마지막인지"+is_end);
            FloatingActionButton flotin=activity2.findViewById(R.id.btnMore2);
            flotin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(!is_end){
                        page+=1;
                        KakaoThread kaka=new KakaoThread();
                        kaka.set(url,  query,  page, activity2,  2);
                        kaka.execute();
                    }
                }
            });

        } catch (Exception e) {
            System.out.println("책 오류발생"+e.getMessage());
        }
    }

    class Bookadapter extends RecyclerView.Adapter<Bookadapter.ViewHolder>{
        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view=activity2.getLayoutInflater().inflate(R.layout.item_book,parent,false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
            final HashMap<String,String> map=array.get(position);
            holder.title.setText(map.get("title"));
            holder.publisher.setText(map.get("publisher"));
            holder.price.setText(map.get("price"));
            final String thumbnail=map.get("thumbnail");
            if(!thumbnail.equals("")){
                Picasso.with(activity2).load(thumbnail).into(holder.thumbnail);
            }
            holder.thumbnail.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    AlertDialog.Builder box= new AlertDialog.Builder(activity2);
                    box.setTitle("책내용");
                    LinearLayout view2= (LinearLayout) activity2.getLayoutInflater().inflate(R.layout.bookdetail,null);
                    ImageView thumbnail2=view2.findViewById(R.id.thumbnail);
                    TextView title2=view2.findViewById(R.id.title);
                    TextView content2=view2.findViewById(R.id.contents);
                    if(!thumbnail2.equals("")){
                        Picasso.with(activity2).load(thumbnail).into(thumbnail2);
                    }
                    title2.setText(map.get("title"));
                    content2.setText(map.get("contents"));
                    box.setView(view2);
                    box.show();
                }
            });

        }

        @Override
        public int getItemCount() {
            return array.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            TextView title,publisher,price;
            ImageView thumbnail;
            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                title=itemView.findViewById(R.id.title);
                publisher=itemView.findViewById(R.id.publisher);
                price=itemView.findViewById(R.id.price);
                thumbnail=itemView.findViewById(R.id.thumbnail);
            }
        }
    }

    //----------------------------------------------------------------------------------------------
    public void Localparser(String s) {
        array = new ArrayList<>();
        try {
            JSONObject jarray2 = new JSONObject(s).getJSONObject("meta");
            total = jarray2.getInt("pageable_count");
            is_end = jarray2.getBoolean("is_end");
            JSONArray jarray = new JSONObject(s).getJSONArray("documents");
            for (int i = 0; i < jarray.length(); i++) {
                JSONObject obj = jarray.getJSONObject(i);
                HashMap<String, String> map = new HashMap<>();
                map.put("place_name", obj.getString("place_name"));
                map.put("phone", obj.getString("phone"));
                map.put("address_name", obj.getString("address_name"));
                map.put("x", obj.getString("x"));
                map.put("y", obj.getString("y"));
                array.add(map);
            }
            TextView to=activity2.findViewById(R.id.txtTotal);
            to.setText("검색수="+total+"   마지막인지"+is_end);
            FloatingActionButton flotin=activity2.findViewById(R.id.btnMore);
            flotin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(!is_end){
                        page+=1;
                        KakaoThread kaka=new KakaoThread();
                        kaka.set(url,  query,  page, activity2,  3);
                        kaka.execute();
                    }
                }
            });
        } catch (Exception e) {
            System.out.println("오류" + e.getMessage());
        }
    }

    class Localadapter extends RecyclerView.Adapter<Localadapter.ViewHolder> {
        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_local, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            final HashMap<String,String> map=array.get(position);
            holder.name.setText(map.get("place_name"));
            holder.phone.setText(map.get("phone"));
            holder.address_name.setText(map.get("address_name"));
            holder.map.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent=new Intent(activity2,map.class);

                    intent.putExtra("name",map.get("place_name"));
                    intent.putExtra("x",map.get("x"));
                    intent.putExtra("y",map.get("y"));
                    activity2.startActivity(intent);
                }
            });
        }

        @Override
        public int getItemCount() {
            return array.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            TextView name, phone,address_name;
            ImageView map;
            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                name = itemView.findViewById(R.id.localname);
                phone = itemView.findViewById(R.id.localphone);
                address_name = itemView.findViewById(R.id.localaddress);
                map=itemView.findViewById(R.id.map);
            }
        }
    }
}


