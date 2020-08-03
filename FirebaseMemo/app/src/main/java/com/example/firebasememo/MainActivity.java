package com.example.firebasememo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {
    EditText editEmail, editPasswd;
    Button btnRegister, btnLogin, btnCancel;
    FirebaseAuth mauth;//인증에 대한 정보가 있음

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setTitle("로그인");
        mauth = FirebaseAuth.getInstance();//인증값주기
        setContentView(R.layout.activity_main);
        editEmail = findViewById(R.id.edtEmail);
        editPasswd = findViewById(R.id.edtPassword);
        btnRegister = findViewById(R.id.btnRegister);
        btnLogin = findViewById(R.id.btnLogin);
        btnCancel = findViewById(R.id.btnCancle);

    }

    public void mclick(View view) {
        String strEmail = editEmail.getText().toString();
        String strPasswd = editPasswd.getText().toString();
        if (strEmail.indexOf('@') < 0) {
            Toast.makeText(MainActivity.this, "이메일 형식이 아닙니다", Toast.LENGTH_LONG).show();
        } else if (strPasswd.length() < 8) {
            Toast.makeText(MainActivity.this, "비밀번호는 8자리 이상이여야합니다", Toast.LENGTH_LONG).show();
        } else {
            switch (view.getId()) {
                case R.id.btnCancle:
                    finish();
                    break;
                case R.id.btnRegister:
                    registerUser(strEmail, strPasswd);
                    break;
                case R.id.btnLogin:
                    loginUser(strEmail, strPasswd);
                    break;
            }
        }
    }

    public void registerUser(String strEmail, String strPasswd) {
        mauth.createUserWithEmailAndPassword(strEmail, strPasswd).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    FirebaseUser user = mauth.getCurrentUser();
                    Toast.makeText(MainActivity.this, "등록 성공", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(MainActivity.this, "등록 실패", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void loginUser(final String strEmail, final String strPasswd) {
        mauth.signInWithEmailAndPassword(strEmail, strPasswd).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(MainActivity.this, "로그인 성공", Toast.LENGTH_SHORT).show();
                    Intent intent=new Intent(MainActivity.this,Memo.class);
                    intent.putExtra("email",strEmail);
                    intent.putExtra("passwd",strPasswd);
                    startActivity(intent);
                } else {
                    Toast.makeText(MainActivity.this, "로그인 실패", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
