package com.example.mysqlfood;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.example.mysqlfood.RemoteService.base_url;

public class MainActivity extends AppCompatActivity {
    List<FoodVO> arraylist = new ArrayList<>();
    Retrofit retrofit;
    RemoteService remoteService;
    Myadapter ad = new Myadapter();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().setTitle("맛집관리");
        ListView list = findViewById(R.id.foodlist);
        list.setAdapter(ad);

        retrofit = new Retrofit.Builder().baseUrl(base_url).addConverterFactory(GsonConverterFactory.create()).build();
        remoteService = retrofit.create(RemoteService.class);
        calldata();

        Button current = findViewById(R.id.btncurrent);
        current.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, CurrentActivity.class);
                intent.putExtra("latitude", 36.7989522);
                intent.putExtra("longitude", 127.072742);

                startActivityForResult(intent, 1);
            }
        });
    }

    public void calldata() {
        Call<List<FoodVO>> call = remoteService.listFood();
        call.enqueue(new Callback<List<FoodVO>>() {
            @Override
            public void onResponse(Call<List<FoodVO>> call, Response<List<FoodVO>> response) {
                arraylist = response.body();
                System.out.println("갯수" + arraylist.size());
                ad.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<List<FoodVO>> call, Throwable t) {
                System.out.println("오류" + t);
            }
        });
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
            view = getLayoutInflater().inflate(R.layout.item_food, viewGroup, false);
            TextView txtname = view.findViewById(R.id.txtname);
            TextView txttel = view.findViewById(R.id.txttel);
            TextView txtaddress = view.findViewById(R.id.txtaddress);
            final FoodVO vo = arraylist.get(i);
            txtname.setText(vo.getName());
            txttel.setText(vo.getTel());
            txtaddress.setText(vo.getAddress());

            ImageView img = view.findViewById(R.id.btnlocation);
            img.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(MainActivity.this, MapsActivity.class);
                    intent.putExtra("latitude", vo.getLatitude());
                    intent.putExtra("longitude", vo.getLongitude());
                    intent.putExtra("name", vo.getName());
                    intent.putExtra("tel", vo.getTel());
                    startActivity(intent);
                }
            });

            return view;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            calldata();
        }
    }


}
