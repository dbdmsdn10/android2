package com.example.mysqlfood;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.example.mysqlfood.RemoteService.base_url;

public class AddActivity extends AppCompatActivity {
    EditText addname, addaddress, addtel, addLatitude, addLongitude;
    Button btnSave, btnCancel;
    Retrofit retrofit;
    RemoteService remoteService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);
        getSupportActionBar().setTitle("맛집등록");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        addname = findViewById(R.id.addname);
        addaddress = findViewById(R.id.addaddress);
        addtel = findViewById(R.id.addtel);
        addLatitude = findViewById(R.id.addLatitude);
        addLongitude = findViewById(R.id.addLongitude);
        Intent intent = getIntent();
        addaddress.setText(intent.getStringExtra("address"));
        addLatitude.setText(intent.getDoubleExtra("latitude", 0) + "");
        addLongitude.setText(intent.getDoubleExtra("longitude", 0) + "");


        btnSave = findViewById(R.id.btnSave);
        btnSave.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder box = new AlertDialog.Builder(AddActivity.this);
                box.setTitle("알림");
                box.setMessage("저장하시겠습니까?");
                box.setPositiveButton("예", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        retrofit = new Retrofit.Builder().baseUrl(base_url).addConverterFactory(GsonConverterFactory.create()).build();
                        remoteService = retrofit.create(RemoteService.class);
                        if (addname.getText().toString().equals("") || addtel.getText().toString().equals("")) {
                            Toast.makeText(AddActivity.this,"이름이나 전화번호가 없습니다",Toast.LENGTH_SHORT).show();
                        } else {
                            Call<Void> call = remoteService.insertFood(addname.getText().toString(), addtel.getText().toString(), addaddress.getText().toString(), addLatitude.getText().toString(), addLongitude.getText().toString());
                            call.enqueue(new Callback<Void>() {
                                @Override
                                public void onResponse(Call<Void> call, Response<Void> response) {
                                    setResult(RESULT_OK);
                                    finish();
                                }

                                @Override
                                public void onFailure(Call<Void> call, Throwable t) {
                                    System.out.println("삽입오류" + t);
                                }
                            });
                        }
                    }
                });
                box.setNegativeButton("아니요", null);
                box.show();


            }
        });
        btnCancel = findViewById(R.id.btnCancel);
        btnCancel.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
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
}
