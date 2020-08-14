package com.example.myproject;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

public class DayReview extends AppCompatActivity {
String stremail;
TextView txtmorning,txtlunch,txtdinner,txtsnack,txtmidnightsnack,txtdayeat;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_day_review);
        getSupportActionBar().setTitle("오늘의 식사량");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Intent intent=getIntent();
        stremail=intent.getStringExtra("stremail");
        txtmorning=findViewById(R.id.txtmoning);
        txtlunch=findViewById(R.id.txtlunch);
        txtdinner=findViewById(R.id.txtdinner);
        txtmidnightsnack=findViewById(R.id.txtmidnight);
        txtsnack=findViewById(R.id.txtsnack);
        txtdayeat=findViewById(R.id.txtdayeat);
        doit();
    }

    public void doit(){
        String wheneat[]={"1"};
        Foodcarcul foodcarcul=new Foodcarcul(stremail,txtmorning,wheneat);
        foodcarcul.run();
        wheneat[0]="2";
        foodcarcul=new Foodcarcul(stremail,txtlunch,wheneat);
        foodcarcul.run();
        wheneat[0]="3";
        foodcarcul=new Foodcarcul(stremail,txtdinner,wheneat);
        foodcarcul.run();
        wheneat[0]="4";
        foodcarcul=new Foodcarcul(stremail,txtmidnightsnack,wheneat);
        foodcarcul.run();
        wheneat[0]="5";
        foodcarcul=new Foodcarcul(stremail,txtsnack,wheneat);
        foodcarcul.run();
        wheneat= new String[]{"1", "2", "3", "4", "5"};
        foodcarcul = new Foodcarcul(stremail, txtdayeat, wheneat);
        foodcarcul.run();
    }

    public void mclick(View view) {
        Intent intent = new Intent(DayReview.this, DayTab.class);
        intent.putExtra("stremail",stremail);
        switch (view.getId()) {
            case R.id.morningbtn:
                intent.putExtra("number", "0");
                break;
            case R.id.lunchbtn:
                intent.putExtra("number", "1");
                break;
            case R.id.dinnerbtn:
                intent.putExtra("number", "2");
                break;
            case R.id.MidnightSnackbtn:
                intent.putExtra("number", "3");
                break;
            case R.id.snackbtn:
                intent.putExtra("number", "4");
                break;
        }
        startActivityForResult(intent,1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        doit();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId()==android.R.id.home){
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        doit();
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
}
