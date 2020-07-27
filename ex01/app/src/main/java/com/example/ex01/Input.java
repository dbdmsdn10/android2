package com.example.ex01;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;
import java.util.GregorianCalendar;

public class Input extends AppCompatActivity {
    Database db;
    SQLiteDatabase sql;
    TextView wdate;
    int myear, mmonth, mday;
    EditText subject, content;
    int id;
    Cursor cursor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input);
        db = new Database(this);
        sql = db.getReadableDatabase();
        wdate = findViewById(R.id.wdate);
        subject = findViewById(R.id.subject);
        content = findViewById(R.id.context);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);//뒤로가기버턴
        Intent intent = getIntent();
       id = intent.getIntExtra("id", -1);

        if(id < 0) {
            getSupportActionBar().setTitle("새로운창");
            GregorianCalendar calendar = new GregorianCalendar();
            myear = calendar.get(Calendar.YEAR);
            mmonth = calendar.get(Calendar.MONTH);
            mday = calendar.get(Calendar.DAY_OF_MONTH);
            wdate.setText(String.format("%04d/%02d/%02d", myear, mmonth + 1, mday));
        }else{
            getSupportActionBar().setTitle("수정");
            cursor=sql.rawQuery("select*from memo where _id="+id,null);
            cursor.moveToNext();
            wdate.setText(cursor.getString(1));
            subject.setText(cursor.getString(2));
            content.setText(cursor.getString(3));
        }
    }

    public void mclick(View view) {
        DatePickerDialog.OnDateSetListener mDate = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {//달력 클릭시 작동
                myear = i;
                mmonth = i1;
                mday = i2;
                wdate.setText(String.format("%04d/%02d/%02d", myear, mmonth + 1, mday));
            }
        };
        switch (view.getId()) {
            case R.id.savebtn:
                String strwdate = wdate.getText().toString();
                String strsubject = subject.getText().toString();
                String strcontent = content.getText().toString();
                if(id<0){
                sql.execSQL("insert into memo(wdate,subject,content) values('" + strwdate + "' , '" + strsubject + "', '" + strcontent + "');");}
                else{
                    sql.execSQL("update memo set wdate='"+strwdate+"',subject='"+strsubject+"',content='"+strcontent+"' where _id="+id);
                }
                setResult(RESULT_OK);
                finish();
                break;

            case R.id.delbtn:
                if(id<0){
                    Toast.makeText(this, "내용이없습니다",Toast.LENGTH_SHORT);
                }else{
                    sql.execSQL("delete from memo where _id="+id);
                    setResult(RESULT_OK);
                    finish();
                }

                break;

            case R.id.canclebtn:
                finish();
                break;
            case R.id.calbtn:
                new DatePickerDialog(this, mDate, myear, mmonth, mday).show();
                break;
        }


    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home://뒤로가기버튼 아이디
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
