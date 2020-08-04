package com.example.firebasechat;

import androidx.annotation.NonNull;
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

public class MainActivity extends AppCompatActivity {
    FirebaseAuth mauth;
    EditText Eamil, passwd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mauth = FirebaseAuth.getInstance();
        Eamil = findViewById(R.id.edtEmail);
        passwd = findViewById(R.id.edtPassword);
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
                    Intent intent=new Intent(MainActivity.this, ChatActivity.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(MainActivity.this, "로그인 실패", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void mclick(View view) {
        String stremail = Eamil.getText().toString();
        String strpasswd = passwd.getText().toString();
        if(view.getId()==R.id.btnCancle){
            finish();
        }
        else if (stremail.indexOf('@') < 0) {
            Toast.makeText(MainActivity.this, "이메일 형식이 아닙니다", Toast.LENGTH_SHORT).show();
        } else if (strpasswd.length() < 8) {
            Toast.makeText(MainActivity.this, "비밀번호는 8자리 이상이여야 합니다", Toast.LENGTH_SHORT).show();
        } else {
            switch (view.getId()) {
                case R.id.btnRegister:
                    registUser(stremail,strpasswd);
                    break;
                case R.id.btnLogin:
                    lohinUser(stremail,strpasswd);
                    break;
            }
        }
    }
}
