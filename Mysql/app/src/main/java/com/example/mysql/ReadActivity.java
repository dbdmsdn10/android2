package com.example.mysql;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
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

public class ReadActivity extends AppCompatActivity {
    Retrofit retrofit;
    RemoteService remoteService;
    EditText edtid, edtname, edtpassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insert);
        getSupportActionBar().setTitle("수정");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        final Intent inent = getIntent();
        edtid = findViewById(R.id.edtid);
        edtname = findViewById(R.id.edtname);
        edtpassword = findViewById(R.id.edtpassword);

        retrofit = new Retrofit.Builder().baseUrl(base_url).addConverterFactory(GsonConverterFactory.create()).build();
        remoteService = retrofit.create(RemoteService.class);
        Call<UserVO> call = remoteService.readUser(inent.getStringExtra("id"));
        call.enqueue(new Callback<UserVO>() {
            @Override
            public void onResponse(Call<UserVO> call, Response<UserVO> response) {
                UserVO userVO = response.body();
                edtid.setText(userVO.getId());
                edtid.setEnabled(false);
                edtname.setText(userVO.getName());
                edtpassword.setText(userVO.getPassword());
            }

            @Override
            public void onFailure(Call<UserVO> call, Throwable t) {

            }
        });

        FloatingActionButton btnupdae = findViewById(R.id.btnsave);
        btnupdae.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder box = new AlertDialog.Builder(ReadActivity.this);
                box.setTitle("알림");
                box.setMessage("현 내용으로 수정하시겠습니까?");
                box.setPositiveButton("예", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Call<Void> call = remoteService.updateUser(edtid.getText().toString(), edtname.getText().toString(), edtpassword.getText().toString());
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
                box.setNegativeButton("아니오", null);
                box.show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_read, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
            case R.id.itemdelete:
                AlertDialog.Builder box = new AlertDialog.Builder(ReadActivity.this);
                box.setTitle("알림");
                box.setMessage("현 내용을 삭제하시겠습니까?");
                box.setPositiveButton("예", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Call<Void> call = remoteService.deleteUser(edtid.getText().toString());
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
                box.setNegativeButton("아니오", null);
                box.show();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
