package com.example.ex01;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class Database extends SQLiteOpenHelper {

    public Database(@Nullable Context context) {
        super(context, "project.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table memo(_id integer primary key autoincrement, wdate text, subject text,content text);");
        db.execSQL("insert into memo(wdate,subject,content) values('2017/10/10','안드로이드','어렵지않아요');");
        db.execSQL("insert into memo(wdate,subject,content) values('2017/10/09','장마시작','장마가 시작됨');");
        db.execSQL("insert into memo(wdate,subject,content) values('2017/10/08','생일파티','**의 생일이다');");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
