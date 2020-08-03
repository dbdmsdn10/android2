package com.example.firebasememo;

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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Date;

public class ReadActivity extends AppCompatActivity {
    Memoclass memoclass = new Memoclass();
    EditText editcontent;
    String key, content,createdate,update;
    FirebaseDatabase database;
    DatabaseReference myRef;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);
        getSupportActionBar().setTitle("메모읽기");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Intent intent = getIntent();
        key = intent.getStringExtra("key");
        content = intent.getStringExtra("content");
        createdate=intent.getStringExtra("createdate");



        memoclass.setContent(content);
        memoclass.setCreateDate(createdate);

        //----------------------------------데이터 베이스 가져와 특정메모창 key값 가져오기
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("memo/" + user.getUid() + "/" + key);//메모장 주소
//---------------------------------------------------
        editcontent = findViewById(R.id.edtContent);
        editcontent.setText(content);
        FloatingActionButton btnsave = findViewById(R.id.btnSave);
        btnsave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder box = new AlertDialog.Builder(ReadActivity.this);
                box.setTitle("확인");
                box.setMessage("현재값으로 수정하시겠습니까?");
                box.setPositiveButton("예", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        update=sdf.format(new Date());
                        memoclass.setUpdate(update);
                        content=editcontent.getText().toString();
                        memoclass.setContent(content);
                        myRef.setValue(memoclass);
                        setResult(RESULT_OK);
                        finish();
                    }
                });
                box.setNegativeButton("아니요", null);
                box.show();
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
            case R.id.itemdel:
                AlertDialog.Builder box=new AlertDialog.Builder(this);
                box.setTitle("확인");
                box.setMessage("삭제하시겠습니까? 복구되지않습니다");
                box.setPositiveButton("예", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        myRef.removeValue();

                        finish();
                    }
                });
                box.setNegativeButton("아니요",null);
                box.show();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_read,menu);
        return super.onCreateOptionsMenu(menu);
    }
}
