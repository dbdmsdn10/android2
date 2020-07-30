package com.example.viewpager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;

import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
TabLayout tab;
ArrayList<Fragment> fragment;
PagerAdapter ad;
ViewPager pager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tab=findViewById(R.id.tab);
        tab.addTab(tab.newTab().setText("도서검색"));//add해서 늘어나는건 자동, 갯수제한 없는듯
        tab.addTab(tab.newTab().setText("영화검색"));
        tab.addTab(tab.newTab().setText("지역검색"));

        tab.getTabAt(0).setIcon(R.drawable.book);
        tab.getTabAt(1).setIcon(R.drawable.movie);
        fragment=new ArrayList<>();
        fragment.add(new Book());
        fragment.add(new movie());
        fragment.add(new local());
        ad=new PagerAdapter(getSupportFragmentManager(),0);
        pager=findViewById(R.id.pager);
        pager.setAdapter(ad);
        pager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tab));//이동시 동기화
        tab.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
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
    }
    class PagerAdapter extends FragmentPagerAdapter{
        public PagerAdapter(@NonNull FragmentManager fm, int behavior) {
            super(fm, behavior);
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {
            return fragment.get(position);
        }

        @Override
        public int getCount() {
            return fragment.size();
        }
    }
}
