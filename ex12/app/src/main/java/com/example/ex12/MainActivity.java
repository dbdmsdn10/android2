package com.example.ex12;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.LinearLayout;

import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    DrawerLayout mainlay;
    LinearLayout drawer;
    TabLayout tab;
    ViewPager pager;
    ArrayList<Fragment> array;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mainlay = findViewById(R.id.drawerlayout);
        drawer = findViewById(R.id.drawer);
        tab = findViewById(R.id.tab);
        pager = findViewById(R.id.pager);

        tab.addTab(tab.newTab().setText("블로그"));
        tab.addTab(tab.newTab().setText("도서"));
        tab.addTab(tab.newTab().setText("지역"));
        tab.getTabAt(0).setIcon(R.drawable.ic_computer_black_24dp);
        tab.getTabAt(1).setIcon(R.drawable.ic_book_black_24dp);
        tab.getTabAt(2).setIcon(R.drawable.ic_map_black_24dp);


        getSupportActionBar().setTitle("검색");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_menu_black_24dp);//뒤로가기 버튼에 그림넣기


        array = new ArrayList<>();
        array.add(new blogfragment());
        array.add(new bookFragment());
        array.add(new localFragment());

        Pageradapter ad = new Pageradapter(getSupportFragmentManager());
        pager.setAdapter(ad);
        pager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tab));//이동시 동기화
        tab.addOnTabSelectedListener(new TabLayout.BaseOnTabSelectedListener() {//클릭시 동시화
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
            System.out.println("포지션" + (position-1));
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
                mainlay.openDrawer(drawer);
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
