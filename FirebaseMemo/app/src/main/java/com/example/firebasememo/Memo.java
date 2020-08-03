package com.example.firebasememo;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class Memo extends AppCompatActivity {
    FloatingActionButton btnaddmemo;
    FirebaseDatabase database;
    DatabaseReference myRef;
    RecyclerView listMemo;
    MemoAdapter memoAdapter;
    ArrayList<Memoclass> arraymemo = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_memo);
        Intent intent = getIntent();
        final String email = intent.getStringExtra("email");
        getSupportActionBar().setTitle(email + "의 메모장");




        memoAdapter = new MemoAdapter(arraymemo, this);
        listMemo = findViewById(R.id.listMemo);
        listMemo.setLayoutManager(new LinearLayoutManager(this));
        listMemo.setAdapter(memoAdapter);

        btnaddmemo = findViewById(R.id.btnAddmemo);
        btnaddmemo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Memo.this, AddActivity.class);
                intent.putExtra("email", email);
                startActivityForResult(intent,1);
            }
        });

        doit();


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==1&&resultCode==RESULT_OK){
            Toast.makeText(Memo.this,"저장완료",Toast.LENGTH_LONG).show();

        }
        else if(requestCode==2&&resultCode==RESULT_OK){
            Toast.makeText(Memo.this,"수정완료",Toast.LENGTH_LONG);

        }
        doit();
    }

    public void doit(){
        //--------------------------------------데이터 베이스 가져오기
        arraymemo.clear();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();//아이디 가져오기
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("memo/" + user.getUid());//~에있는값 가져오기
        myRef.addChildEventListener(new ChildEventListener() {//이벤트 발생시, 변경 발생시
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Memoclass memoclass = (Memoclass) snapshot.getValue(Memoclass.class);
                memoclass.setKey(snapshot.getKey());
                arraymemo.add(memoclass);
                memoAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

//-------------------------------------------------------------
    }
}
