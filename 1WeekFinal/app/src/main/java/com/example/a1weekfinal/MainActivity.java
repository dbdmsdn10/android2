package com.example.a1weekfinal;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.LinearLayout;

import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    DrawerLayout mainlayout;
    LinearLayout drawerview;
    TabLayout tab;
    ViewPager pager;
    ArrayList<Fragment> array = new ArrayList<>();


    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().setTitle("카카오검색");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_menu_black_24dp);
        mainlayout = findViewById(R.id.mainlayout);
        drawerview = findViewById(R.id.drawerview);

        tab = findViewById(R.id.tab);
        pager = findViewById(R.id.pager);
        tab.addTab(tab.newTab().setText("블로그"));
        tab.getTabAt(0).setIcon(R.drawable.ic_signal_cellular_connected_no_internet_3_bar_black_24dp);
        tab.addTab(tab.newTab().setText("도서"));
        tab.getTabAt(1).setIcon(R.drawable.ic_book_black_24dp);
        tab.addTab(tab.newTab().setText("지역"));
        tab.getTabAt(2).setIcon(R.drawable.ic_map_black_24dp);

        array.add(new BlogFragment());
        array.add(new BookFragment());
        array.add(new localFragment());
        Pageradapter ad = new Pageradapter(getSupportFragmentManager());
        pager.setAdapter(ad);
        pager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tab));
        tab.addOnTabSelectedListener(new TabLayout.BaseOnTabSelectedListener() {
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
        switch (item.getItemId()) {
            case android.R.id.home:
                mainlayout.openDrawer(drawerview);
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
