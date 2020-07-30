package com.example.ex12;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.HashMap;

public class blogfragment extends Fragment {
    EditText edit;
    String url="https://dapi.kakao.com/v2/search/blog";
    String query="게임";
    int size=10;
    ArrayList<HashMap<String,String>> array;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_blog,container,false);
        edit =view.findViewById(R.id.editsearch);
        edit.setHint("블로그검색");

        Kakaothread kakao=new Kakaothread();
        FragmentActivity activity=getActivity();
        kakao.get(url,query,size,activity,1);
        kakao.execute();

        ImageView btnmore=view.findViewById(R.id.btnmore);
        btnmore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                size+=10;
                FragmentActivity activity=getActivity();
                Kakaothread kakao=new Kakaothread();
                kakao.get(url,query,size,activity,1);
                kakao.execute();
            }
        });

        ImageView btnsearch=view.findViewById(R.id.searchbtn);
        btnsearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                query=edit.getText().toString();
                size=10;
                FragmentActivity activity=getActivity();
                Kakaothread kakao=new Kakaothread();
                kakao.get(url,query,size,activity,1);
                kakao.execute();
            }
        });
        return view;
    }


}
