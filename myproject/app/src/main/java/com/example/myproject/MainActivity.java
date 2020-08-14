package com.example.myproject;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.example.myproject.RemoteService.base_url;

public class MainActivity extends AppCompatActivity {
    FirebaseAuth mauth;
    EditText txtid, txtpassword;
    Retrofit retrofit;
    RemoteService remoteService;
    String stremail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mauth = FirebaseAuth.getInstance();
        txtid = findViewById(R.id.txtid);
        txtpassword = findViewById(R.id.txtpassword);
        retrofit = new Retrofit.Builder().baseUrl(base_url).addConverterFactory(GsonConverterFactory.create()).build();
        remoteService = retrofit.create(RemoteService.class);
    }

    public void registUser(String strEmail, String strPasswd) {
        mauth.createUserWithEmailAndPassword(strEmail, strPasswd).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    FirebaseUser user = mauth.getCurrentUser();
                    Toast.makeText(MainActivity.this, "등록 성공", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(MainActivity.this, "등록 성공", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void lohinUser(String strEmail, String strPasswd) {

        mauth.signInWithEmailAndPassword(strEmail, strPasswd).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(MainActivity.this, "로그인 성공", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(MainActivity.this, Main2Activity.class);
                    intent.putExtra("stremail", stremail);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(MainActivity.this, "로그인 실패", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void mclick(View view) {
        stremail = txtid.getText().toString();
        final String strpasswd = txtpassword.getText().toString();
        if (view.getId() == R.id.btnfinish) {
            finish();
        } else if (stremail.indexOf('@') < 0) {
            Toast.makeText(MainActivity.this, "이메일 형식이 아닙니다", Toast.LENGTH_SHORT).show();
        } else if (strpasswd.length() < 8) {
            Toast.makeText(MainActivity.this, "비밀번호는 8자리 이상이여야 합니다", Toast.LENGTH_SHORT).show();
        } else {
            switch (view.getId()) {
                case R.id.btnregist:
                    Call<List<String>> call = remoteService.readuserid(stremail);
                    call.enqueue(new Callback<List<String>>() {
                        @Override
                        public void onResponse(Call<List<String>> call, Response<List<String>> response) {
                            List<String> arraylist = new ArrayList<>();
                            arraylist=response.body();
                            if (arraylist.size() > 0) {
                                AlertDialog.Builder box = new AlertDialog.Builder(MainActivity.this);
                                box.setTitle("알림");
                                box.setMessage("이미 존재하는 e-mail입니다");
                                box.setPositiveButton("확인", null);
                                box.show();
                            } else {
                                registUser(stremail, strpasswd);
                                Call<Void> call2 = remoteService.insertuserid(stremail, strpasswd);
                                call2.enqueue(new Callback<Void>() {
                                    @Override
                                    public void onResponse(Call<Void> call, Response<Void> response) {
                                        Toast.makeText(MainActivity.this, "등록완료", Toast.LENGTH_SHORT);
                                        Intent intent = new Intent(MainActivity.this, Userinfo.class);
                                        intent.putExtra("stremail", stremail);
                                        startActivityForResult(intent,1);
                                    }

                                    @Override
                                    public void onFailure(Call<Void> call, Throwable t) {
                                    }
                                });
                            }
                        }

                        @Override
                        public void onFailure(Call<List<String>> call, Throwable t) {
                        }
                    });
                    break;
                case R.id.btnlogin:
                    lohinUser(stremail, strpasswd);
                    break;
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode==RESULT_OK){
            finish();
        }
    }
}
