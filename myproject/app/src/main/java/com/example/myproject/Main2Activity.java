package com.example.myproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import java.util.Map;

public class Main2Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

    }

    public void mclick(View view) {
        switch (view.getId())
        {
            case R.id.searchbtn:
                Intent intent=new Intent(Main2Activity.this,Websearch.class);
                startActivity(intent);
                break;
            case R.id.mapbtn:
                intent=new Intent(Main2Activity.this, MapsActivity.class);
                startActivity(intent);
                break;
        }
    }
}
