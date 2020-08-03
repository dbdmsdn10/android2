package com.example.myproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class DayReview extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_day_review);
        getSupportActionBar().setTitle("오늘의 식사량");
    }

    public void mclick(View view) {
        Intent intent=new Intent(DayReview.this,DayTab.class);
        switch (view.getId()){
            case R.id.morningbtn:
               intent.putExtra("number","0");
               System.out.println("아침");
                break;
            case R.id.lunchbtn:
                intent.putExtra("number","1");
                System.out.println("점심");
                break;
            case R.id.dinnerbtn:
                intent.putExtra("number","2");
                System.out.println("저녁");
                break;
            case R.id.MidnightSnackbtn:
                intent.putExtra("number","3");
                System.out.println("야식");
                break;
            case R.id.snackbtn:
                intent.putExtra("number","4");
                System.out.println("간식");
                break;
        }
        startActivity(intent);
    }
}
