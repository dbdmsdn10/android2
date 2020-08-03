package com.example.firebasememo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Date;

public class AddActivity extends AppCompatActivity {
EditText edtContent;
FirebaseAuth mAuth;
FirebaseDatabase database;
DatabaseReference myRef;
String email;
String setId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);
        Intent intent=getIntent();
        email=intent.getStringExtra("email");
        getSupportActionBar().setTitle("메모쓰기");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        edtContent=findViewById(R.id.edtContent);
        mAuth=FirebaseAuth.getInstance();
        FirebaseUser user=mAuth.getCurrentUser();//아이디값가져오기
        setId=user.getUid();//이아이디는 개인의 아이디이다
        FloatingActionButton btnsave=findViewById(R.id.btnSave);

        btnsave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String strcontent=edtContent.getText().toString();
                if(strcontent.equals("")){
                    Toast.makeText(AddActivity.this,"내용을 입력하시오",Toast.LENGTH_SHORT);
                }else{
                    Memoclass memoclass=new Memoclass();
                    memoclass.setContent(strcontent);
                    SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    memoclass.setCreateDate(sdf.format(new Date()));
                    memoclass.setUpdate(sdf.format(new Date()));
                    database=FirebaseDatabase.getInstance();
                    myRef=database.getReference("memo").child(setId).push();
                    myRef.setValue(memoclass);
                    setResult(RESULT_OK);
                    finish();

                }
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
