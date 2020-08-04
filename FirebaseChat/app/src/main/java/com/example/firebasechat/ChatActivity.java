package com.example.firebasechat;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class ChatActivity extends AppCompatActivity {
    ArrayList<Chat> arraychat = new ArrayList<>();
    EditText edtcontent;
    ImageView btnSend;
    RecyclerView listchat;

    String strEmail;
    FirebaseDatabase database;
    DatabaseReference myRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        getSupportActionBar().setTitle("채팅창");
        edtcontent = findViewById(R.id.edtcontent);
        listchat = findViewById(R.id.list);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();//지금의 유저가져오기
        strEmail = user.getEmail();
        database = FirebaseDatabase.getInstance();
        getSupportActionBar().setTitle(strEmail + ": 내용");


        final ChatAdapter ad=new ChatAdapter(this,arraychat,strEmail);
        listchat.setLayoutManager(new LinearLayoutManager(this));
        listchat.setAdapter(ad);


        database=FirebaseDatabase.getInstance();
        myRef=database.getReference("cahts");
        myRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                Chat chat=snapshot.getValue(Chat.class);

                arraychat.add(chat);
                listchat.scrollToPosition(arraychat.size()-1);
                ad.notifyDataSetChanged();
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


        btnSend = findViewById(R.id.btnsend);
        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String strcontent = edtcontent.getText().toString();
                if (strcontent.equals("")) {
                    Toast.makeText(ChatActivity.this, "내용을 입력하시오", Toast.LENGTH_SHORT);
                } else {//데이터 삽입
                    Chat chat = new Chat();
                    chat.setEmail(strEmail);
                    chat.setContent(strcontent);
                    SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    String strdate=sdf.format(new Date());
                    chat.setWdate(strdate);
                    myRef =database.getReference("cahts").child(strdate);//데이터 저장위치지정, 챗밑의 날짜밑
                    myRef.setValue(chat);
                    edtcontent.setText("");
                }
            }
        });
    }
}
