package com.example.myproject;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.example.myproject.RemoteService.base_url;

public class Userinfo2 extends AppCompatActivity {
    EditText edtweight, edtheight, edtage;
    RadioButton radioman, radiogirl, radionope, radionomal, radiosomtimes, radioevery;
    Retrofit retrofit;
    RemoteService remoteService;
    String stremail;
    double Weight, Height, Age, Gender, How;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_userinfo);
        Intent intent = getIntent();
        stremail = intent.getStringExtra("stremail");
        Weight = intent.getDoubleExtra("Weight", -1);
        Height = intent.getDoubleExtra("Height", -1);
        Age = intent.getDoubleExtra("Age", -1);
        Gender = intent.getDoubleExtra("Gender", -1);
        How = intent.getDoubleExtra("How", -1);


        retrofit = new Retrofit.Builder().baseUrl(base_url).addConverterFactory(GsonConverterFactory.create()).build();
        remoteService = retrofit.create(RemoteService.class);
        edtweight = findViewById(R.id.edtweight);
        edtheight = findViewById(R.id.edtheight);
        edtage = findViewById(R.id.edtage);
        radioman = findViewById(R.id.radioman);
        radiogirl = findViewById(R.id.radiogirl);
        radionope = findViewById(R.id.radionope);
        radionomal = findViewById(R.id.radionomal);
        radiosomtimes = findViewById(R.id.radiosomtimes);
        radioevery = findViewById(R.id.radioevery);

        edtweight.setText(Weight + "");
        edtheight.setText(Height + "");
        edtage.setText(Age + "");

        switch ((int) Gender){
            case 1:
                radioman.setChecked(true);
                break;
            case 2:
                radiogirl.setChecked(true);
                break;
        }
        switch ((int) How){
            case 1:
                radionope.setChecked(true);
                break;
            case 2:
                radionomal.setChecked(true);
                break;
            case 3:
                radiosomtimes.setChecked(true);
                break;
            case 4:
                radioevery.setChecked(true);
                break;
        }

        findViewById(R.id.btnuserinfo).setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                String txtweight = edtweight.getText().toString();
                String txtheight = edtheight.getText().toString();
                String txtage = edtage.getText().toString();
                int gender = -1;
                if (radioman.isChecked()) {
                    gender = 1;
                } else if (radiogirl.isChecked()) {
                    gender = 2;
                }
                int how = -1;

                if (radionope.isChecked()) {
                    how = 1;
                } else if (radionomal.isChecked()) {
                    how = 2;
                } else if (radiosomtimes.isChecked()) {
                    how = 3;
                } else if (radioevery.isChecked()) {
                    how = 4;
                }

                AlertDialog.Builder box = new AlertDialog.Builder(Userinfo2.this);
                box.setTitle("알림");
                box.setPositiveButton("확인", null);
                if (txtweight.equals("")) {
                    box.setMessage("몸무게가 없습니다");
                    box.show();
                } else if (txtheight.equals("")) {
                    box.setMessage("키가 없습니다");
                    box.show();
                } else if (txtage.equals("")) {
                    box.setMessage("나이가 없습니다");
                    box.show();
                } else if (gender < 0) {
                    box.setMessage("성별을 선택해주세요");
                    box.show();
                } else if (how < 0) {
                    box.setMessage("활동량을 정해주세요");
                    box.show();
                } else {
                    Call<Void> call = remoteService.updateuserinfo(stremail, txtweight, txtheight, txtage, gender + "", how + "");
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
            }
        });
    }
}
