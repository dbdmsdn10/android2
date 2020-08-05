package com.example.mysql;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.example.mysql.RemoteService.base_url;

public class InsertActivity extends AppCompatActivity {
    Retrofit retrofit;
    RemoteService remoteService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insert);
        getSupportActionBar().setTitle("사용자 등록");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        FloatingActionButton btnsave=findViewById(R.id.btnsave);
        btnsave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                retrofit = new Retrofit.Builder().baseUrl(base_url).addConverterFactory(GsonConverterFactory.create()).build();
                remoteService = retrofit.create(RemoteService.class);
                AlertDialog.Builder box=new AlertDialog.Builder(InsertActivity.this);
                box.setTitle("알림");
                box.setMessage("저장하시겠습니까");
                box.setPositiveButton("예", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        UserVO uservo=new UserVO();
                        EditText txtid=findViewById(R.id.edtid);
                        uservo.setId(txtid.getText().toString());
                        EditText txtpassword=findViewById(R.id.edtpassword);
                        uservo.setPassword(txtpassword.getText().toString());
                        EditText txtname=findViewById(R.id.edtname);
                        uservo.setName(txtname.getText().toString());

                        Call<Void> call=remoteService.insertUser(uservo.getId(),uservo.getName(),uservo.getPassword());
                        call.enqueue(new Callback<Void>() {
                            @Override
                            public void onResponse(Call<Void> call, Response<Void> response) {
                                setResult(RESULT_OK);
                                finish();
                            }

                            @Override
                            public void onFailure(Call<Void> call, Throwable t) {

                            }
                        });
                    }
                });
                box.setNegativeButton("아니요",null);
                box.show();
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId())
        {
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
