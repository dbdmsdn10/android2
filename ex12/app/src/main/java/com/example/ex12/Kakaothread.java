package com.example.ex12;

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

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;

public class Kakaothread extends AsyncTask<String, String, String> {
    String url = "https://dapi.kakao.com/v2/search/blog";
    String query = "게임";
    int size = 10;
    ArrayList<HashMap<String, String>> array = new ArrayList<>();
    int what = 0;
    FragmentActivity activity2;

    public void get(String url, String query, int size, FragmentActivity activity, int what) {
        this.url = url;
        this.query = query;
        this.size = size;
        this.activity2 = activity;
        this.what = what;
    }

    @Override
    protected String doInBackground(String... strings) {
        String s = "";
        switch (what) {
            case 1:
                s = Daum.connect(url + "?query=" + query + "&size=" + size);
                break;
            case 2:
                s = Daum.connect(url + "&query=" + query + "&size=" + size);
                break;
            case 3:
                s = Daum.connect(url + "?query=" + query + "&AddressSize=" + size);
                break;

        }

        //System.out.println(s);

        return s;
    }

    @Override
    protected void onPostExecute(String s) {
        RecyclerView list;
        switch (what) {
            case 1:
                list = activity2.findViewById(R.id.list2);
                break;
            case 2:
                list = activity2.findViewById(R.id.booklist2);

                break;
            case 3:
                list = activity2.findViewById(R.id.locallist2);
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + what);
        }
        list.setLayoutManager(new LinearLayoutManager(activity2));
        list.setHasFixedSize(true);
        switch (what) {
            case 1:
                blogparser(s);
                blogadapter ad = new blogadapter();
                list.setAdapter(ad);
                break;
            case 2:
                bookparser(s);
                Bookadapter ad2 = new Bookadapter();
                list.setAdapter(ad2);

                break;
            case 3:
                Localparser(s);
                System.out.println("갯수=" + array.size());
                Localadapter ad3 = new Localadapter();

                list.setAdapter(ad3);
                break;
        }
        //System.out.println("데이터 갯수"+array.size());


        super.onPostExecute(s);
    }

    //-----------------------------------------------------------------------------------------------------------
    public void blogparser(String s) {
        array = new ArrayList<>();
        try {
            JSONArray jarray = new JSONObject(s).getJSONArray("documents");
            for (int i = 0; i < jarray.length(); i++) {
                JSONObject obj = jarray.getJSONObject(i);
                HashMap<String, String> map = new HashMap<>();
                map.put("title", obj.getString("title"));
                map.put("link", obj.getString("url"));
                array.add(map);
            }
        } catch (Exception e) {
            System.out.println("오류" + e.getMessage());
        }
    }

    class blogadapter extends RecyclerView.Adapter<blogadapter.ViewHolder> {

        @NonNull
        @Override
        public blogadapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_blog, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull blogadapter.ViewHolder holder, int position) {
            // System.out.println("Position="+position);

            final HashMap<String, String> map = array.get(position);

            holder.title.setText(Html.fromHtml(map.get("title")));
            holder.link.setText(map.get("link"));
            holder.bloglayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent=new Intent(activity2, Website.class);
                    intent.putExtra("name",Html.fromHtml(map.get("title")));
                    intent.putExtra("link",map.get("link"));
                    activity2.startActivity(intent);
                }
            });

        }

        @Override
        public int getItemCount() {

            return array.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            TextView title, link;
            LinearLayout bloglayout;

            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                title = itemView.findViewById(R.id.title);
                link = itemView.findViewById(R.id.link);
                bloglayout=itemView.findViewById(R.id.bloglayout);
            }
        }
    }

    //-----------------------------------------------------------------------------------------------------------
    public void bookparser(String s) {
        array = new ArrayList<>();
        try {
            JSONArray jarray = new JSONObject(s).getJSONArray("documents");
            for (int i = 0; i < jarray.length(); i++) {
                JSONObject obj = jarray.getJSONObject(i);
                HashMap<String, String> map = new HashMap<>();
                map.put("title", obj.getString("title"));
                map.put("thumbnail", obj.getString("thumbnail"));
                map.put("publisher", obj.getString("publisher"));
                map.put("price", obj.getString("price"));
                map.put("contents", obj.getString("contents"));
                array.add(map);
            }
        } catch (Exception e) {
            System.out.println("오류" + e.getMessage());
        }
    }

    class Bookadapter extends RecyclerView.Adapter<Bookadapter.ViewHolder> {
        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_book, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            final HashMap<String, String> map = array.get(position);
            holder.title.setText(Html.fromHtml(map.get("title")));
            holder.publisher.setText(Html.fromHtml(map.get("publisher")));
            holder.price.setText(Html.fromHtml(map.get("price")));
            Picasso.with(activity2).load(map.get("thumbnail")).into(holder.img);
            holder.img.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    AlertDialog.Builder box=new AlertDialog.Builder(activity2);
                    box.setTitle(Html.fromHtml(map.get("title")));
                    box.setMessage(map.get("contents"));
                    box.setPositiveButton("확인",null);
                    box.show();
                }
            });

        }
        @Override
        public int getItemCount() {
            return array.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            TextView title, publisher, price;
            ImageView img;

            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                title = itemView.findViewById(R.id.booktitle);
                publisher = itemView.findViewById(R.id.publisher);
                price = itemView.findViewById(R.id.price);
                img = itemView.findViewById(R.id.bookthumbnail);
            }
        }


    }

    //-----------------------------------------------------------------------------------------------------------
    public void Localparser(String s) {
        array = new ArrayList<>();
        try {
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
                    Intent intent=new Intent(activity2,MapsActivity.class);

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
