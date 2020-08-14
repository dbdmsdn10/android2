package com.example.myproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;

public class DayTab extends AppCompatActivity {
    String stremail;
    ViewPager pager;
    TabLayout tabLayout;
    ArrayList<Fragment> array = new ArrayList<>();
    String past = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_day_tab);
        getSupportActionBar().setTitle("섭취 상세내역");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Intent intent = getIntent();
        stremail = intent.getStringExtra("stremail");
        past = intent.getStringExtra("past");

        tabLayout = findViewById(R.id.tab);
        pager = findViewById(R.id.pager);
        tabLayout.addTab(tabLayout.newTab().setText("아침"));
        tabLayout.addTab(tabLayout.newTab().setText("점심"));
        tabLayout.addTab(tabLayout.newTab().setText("저녁"));
        tabLayout.addTab(tabLayout.newTab().setText("야식"));
        tabLayout.addTab(tabLayout.newTab().setText("간식"));


        pager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));//버튼클릭시 이동 동기화
        tabLayout.addOnTabSelectedListener(new TabLayout.BaseOnTabSelectedListener() {//움직일시 동기화
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                pager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        if (past == null||past.equals("")) {
            array.add(new Morning(stremail));
            array.add(new lunch(stremail));
            array.add(new dinner(stremail));
            array.add(new midnightsnack(stremail));
            array.add(new snack(stremail));
        } else {
            Morning morning=new Morning(stremail);
            lunch lunch=new lunch(stremail);
            dinner dinner=new dinner(stremail);
            midnightsnack midnightsnack=new midnightsnack(stremail);
            snack snack=new snack(stremail);
            morning.setDate(past);
            lunch.setDate(past);
            dinner.setDate(past);
            midnightsnack.setDate(past);
            snack.setDate(past);
            array.add(morning);
            array.add(lunch);
            array.add(dinner);
            array.add(midnightsnack);
            array.add(snack);
        }
        Pageradapter ad = new Pageradapter(getSupportFragmentManager());
        pager.setAdapter(ad);

        String aa = intent.getStringExtra("number");
        int a = Integer.parseInt(aa);
        TabLayout.Tab tab = tabLayout.getTabAt(a);
        tab.select();
    }

    class Pageradapter extends FragmentPagerAdapter {
        public Pageradapter(@NonNull FragmentManager fm) {
            super(fm);
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {
            return array.get(position);
        }

        @Override
        public int getCount() {
            return array.size();
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

}
