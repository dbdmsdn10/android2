package com.example.myproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;

public class DayTab extends AppCompatActivity {
ViewPager pager;
TabLayout tabLayout;
    ArrayList<Fragment> array = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_day_tab);
        tabLayout=findViewById(R.id.tab);
        pager=findViewById(R.id.pager);
        tabLayout.addTab(tabLayout.newTab().setText("아침"));
        tabLayout.addTab(tabLayout.newTab().setText("점심"));
        tabLayout.addTab(tabLayout.newTab().setText("저녁"));
        tabLayout.addTab(tabLayout.newTab().setText("야식"));
        tabLayout.addTab(tabLayout.newTab().setText("간식"));

        pager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.BaseOnTabSelectedListener() {
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

        array.add(new Morning());
        array.add(new lunch());
        array.add(new dinner());
        array.add(new midnightsnack());
        array.add(new snack());

        Pageradapter ad = new Pageradapter(getSupportFragmentManager());
        pager.setAdapter(ad);
        Intent intent=getIntent();
        String aa=intent.getStringExtra("number");
        System.out.println("aa값은 ="+aa);
        int a=Integer.parseInt(aa);
        System.out.println("a의 값은 ="+a);
        TabLayout.Tab tab=tabLayout.getTabAt(a);
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

}
