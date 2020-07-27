package com.example.ex01;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.sql.SQLException;

public class MainActivity extends AppCompatActivity {
    Database db;
    SQLiteDatabase sql;
    Cursor cursor;
    Myadapter ad;
    ListView list;
    int state;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().setTitle("메모장");
        db = new Database(this);



        sql = db.getReadableDatabase();
        cursor = sql.rawQuery("select*from memo order by wdate desc", null);
        state = 1;
        ad = new Myadapter(this, cursor);
        list = findViewById(R.id.list);
        list.setAdapter(ad);
        registerForContextMenu(list);
        findViewById(R.id.addbtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {//삽입
                Intent intent = new Intent(MainActivity.this, Input.class);
                startActivityForResult(intent, 1);
            }
        });

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            //어댑터를 받어서 그것에 해당하는 cursor를 사져옴
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {//수정
                Intent intent = new Intent(MainActivity.this, Input.class);
                intent.putExtra("id", cursor.getInt(0));
                startActivityForResult(intent, 2);
            }
        });
    }

    public class Myadapter extends CursorAdapter {
        public Myadapter(Context context, Cursor c) {
            super(context, c);
        }

        @Override
        public View newView(Context context, Cursor cursor, ViewGroup viewGroup) {
            View view = getLayoutInflater().inflate(R.layout.item, viewGroup, false);
            return view;
        }

        @Override
        public void bindView(View view, Context context, final Cursor c) {
            TextView date = view.findViewById(R.id.wdate);
            TextView subject = view.findViewById(R.id.subject);
            date.setText(c.getString(1));
            subject.setText(c.getString(2));
            view.findViewById(R.id.delbtn).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    AlertDialog.Builder box = new AlertDialog.Builder(MainActivity.this);
                    box.setTitle("확인");
                    box.setMessage("정말 삭제하시겠습니까");
                    box.setPositiveButton("예", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            sql.execSQL("delete from memo where _id=" + c.getInt(0));
                            if (state == 1) {
                                cursor = sql.rawQuery("select*from memo order by wdate desc", null);
                            } else if (state == 2) {
                                cursor = sql.rawQuery("select*from memo order by wdate", null);
                            } else if (state == 3) {
                                cursor = sql.rawQuery("select*from memo order by subject", null);
                            } else if (state == 4) {
                                cursor = sql.rawQuery("select*from memo order by subject desc", null);
                            }
                            ad.changeCursor(cursor);
                        }
                    });
                    box.setNegativeButton("아니요", null);
                    box.show();
                }
            });
        }
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {//롱클릭시
        menu.setHeaderTitle("정렬방식");
        menu.add(0, 1, 0, "날짜 최신순");
        menu.add(0, 2, 0, "날짜 오래된순");
        menu.add(0, 3, 0, "이름순(ㄱ~ㅎ)");
        menu.add(0, 4, 0, "이름순(ㅎ~ㄱ)");
        super.onCreateContextMenu(menu, v, menuInfo);
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {//롱클릭시 나오는거 이벤트
        switch (item.getItemId()) {
            case 1:
                cursor = sql.rawQuery("select*from memo order by wdate desc", null);
                state = 1;
                break;
            case 2:
                cursor = sql.rawQuery("select*from memo order by wdate", null);
                state = 2;
                break;
            case 3:
                cursor = sql.rawQuery("select*from memo order by subject", null);
                state = 3;
                break;
            case 4:
                cursor = sql.rawQuery("select*from memo order by subject desc", null);
                state = 3;
                break;
        }
        ad.changeCursor(cursor);
        return super.onContextItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {//intent끝날시 동작
        if (resultCode == RESULT_OK) {
            if (state == 1) {
                cursor = sql.rawQuery("select*from memo order by wdate desc", null);
            } else if (state == 2) {
                cursor = sql.rawQuery("select*from memo order by wdate", null);
            } else if (state == 3) {
                cursor = sql.rawQuery("select*from memo order by subject", null);
            } else if (state == 4) {
                cursor = sql.rawQuery("select*from memo order by subject desc", null);
            }
            ad.changeCursor(cursor);
            if(requestCode==1){
                Toast.makeText(MainActivity.this,"삽입완료",Toast.LENGTH_SHORT).show();
            }
            else{
                Toast.makeText(MainActivity.this,"수정완료",Toast.LENGTH_SHORT).show();
            }
        } else {
            if(requestCode==1){
                Toast.makeText(MainActivity.this,"삽입취소",Toast.LENGTH_SHORT).show();
            }
            else{
                Toast.makeText(MainActivity.this,"수정취소",Toast.LENGTH_SHORT).show();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {//메뉴 가져오기
        getMenuInflater().inflate(R.menu.findmenu, menu);
        MenuItem findbtn2 = menu.findItem(R.id.findbtn2);
        SearchView searchview = (SearchView) findbtn2.getActionView();//문자 검색할수있도록 사용
        searchview.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                cursor = sql.rawQuery("select * from memo where subject like '%" + s + "%' or content like '%" + s + "%'", null);
                ad.changeCursor(cursor);
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }
}
