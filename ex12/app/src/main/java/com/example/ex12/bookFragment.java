package com.example.ex12;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link bookFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class bookFragment extends Fragment {
    EditText edit;

    String url="https://dapi.kakao.com/v3/search/book?target=title";
    String query="게임";
    int size=10;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_book,container,false);
        edit=view.findViewById(R.id.bookeditsearch);
        edit.setHint("책검색");
        Kakaothread kakao=new Kakaothread();
        FragmentActivity activity=getActivity();
        kakao.get(url,query,size,activity,2);
        kakao.execute();

        ImageView btnmore=view.findViewById(R.id.bookbtnmore);
        btnmore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                size+=10;
                FragmentActivity activity=getActivity();
                Kakaothread kakao=new Kakaothread();
                kakao.get(url,query,size,activity,2);
                kakao.execute();
            }
        });

        ImageView btnsearch=view.findViewById(R.id.booksearchbtn);
        btnsearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                query=edit.getText().toString();
                size=10;
                FragmentActivity activity=getActivity();
                Kakaothread kakao=new Kakaothread();
                kakao.get(url,query,size,activity,2);
                kakao.execute();
            }
        });
        return view;
    }

}
