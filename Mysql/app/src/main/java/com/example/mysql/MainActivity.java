package com.example.mysql;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.example.mysql.RemoteService.base_url;

public class MainActivity extends AppCompatActivity {
    Retrofit retrofit;
    RemoteService remoteService;
    List<UserVO> arrayUser = new ArrayList<>();
    UserAdapter userAdapter = new UserAdapter();
    ListView listuser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        listuser = findViewById(R.id.listuser);
        listuser.setAdapter(userAdapter);

        retrofit = new Retrofit.Builder().baseUrl(base_url).addConverterFactory(GsonConverterFactory.create()).build();
        remoteService = retrofit.create(RemoteService.class);

        Call<List<UserVO>> call = remoteService.listUser();//뭐실행할지 결정하는곳
        call.enqueue(new Callback<List<UserVO>>() {//실제 실행
            @Override
            public void onResponse(Call<List<UserVO>> call, Response<List<UserVO>> response) {
                arrayUser = response.body();
                userAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<List<UserVO>> call, Throwable t) {

            }
        });

        FloatingActionButton btnadd=findViewById(R.id.btnadd);
        btnadd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(MainActivity.this,InsertActivity.class);
                startActivityForResult(intent,1);
            }
        });
    }

    class UserAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return arrayUser.size();
        }

        @Override
        public Object getItem(int i) {

            return null;
        }

        @Override
        public long getItemId(int i) {
            return arrayUser.size();
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            view = getLayoutInflater().inflate(R.layout.item_user, viewGroup, false);
            final UserVO uservo=arrayUser.get(i);

            TextView txtid=view.findViewById(R.id.txtid);
            txtid.setText(uservo.getId());
            TextView txtname=view.findViewById(R.id.txtname);
            txtname.setText(uservo.getName());
            ImageView btnedit=view.findViewById(R.id.btnread);

            btnedit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent=new Intent(MainActivity.this,ReadActivity.class);
                    intent.putExtra("id",uservo.getId());
                    startActivityForResult(intent,2);
                }
            });
            return view;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(requestCode==1&&resultCode==RESULT_OK){
            Toast.makeText(MainActivity.this,"삽입완료",Toast.LENGTH_SHORT);
        }
        if(requestCode==2&&resultCode==RESULT_OK){
            Toast.makeText(MainActivity.this,"수정완료",Toast.LENGTH_SHORT);
        }


        Call<List<UserVO>> call = remoteService.listUser();//뭐실행할지 결정하는곳
        call.enqueue(new Callback<List<UserVO>>() {//실제 실행
            @Override
            public void onResponse(Call<List<UserVO>> call, Response<List<UserVO>> response) {
                arrayUser = response.body();
                userAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<List<UserVO>> call, Throwable t) {

            }
        });

        super.onActivityResult(requestCode, resultCode, data);
    }
}
