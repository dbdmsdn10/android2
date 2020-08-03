package com.example.a1weekfinal;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link localFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class localFragment extends Fragment {
    EditText editsearch;
    String url="https://dapi.kakao.com/v2/local/search/keyword.json";
    String query="서울";
    int page=1;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_local,container,false);
        editsearch=view.findViewById(R.id.edtSearch);
        KakaoThread kaka=new KakaoThread();
        kaka.set(url,  query,  page, getActivity(),  3);
        kaka.execute();

        ImageView btnsearch=view.findViewById(R.id.btnSearch);
        btnsearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                query=editsearch.getText().toString();
                page=1;
                KakaoThread kaka=new KakaoThread();
                kaka.set(url,  query,  page, getActivity(),  3);
                kaka.execute();
            }
        });
        editsearch.setOnKeyListener(new View.OnKeyListener()//엔터시
        {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                if (keyEvent.getAction() == KeyEvent.ACTION_DOWN)
                {
                    switch (i)
                    {
                        case KeyEvent.KEYCODE_DPAD_CENTER:
                        case KeyEvent.KEYCODE_ENTER:
                            query = editsearch.getText().toString();
                            page=1;
                            KakaoThread kaka=new KakaoThread();
                            kaka.set(url,  query,  page, getActivity(),  3);
                            kaka.execute();
                            return true;
                        default:
                            break;
                    }
                }
                return false;
            }
        });
        return view;
    }
}
