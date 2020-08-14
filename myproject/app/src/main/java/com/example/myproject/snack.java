package com.example.myproject;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link snack#newInstance} factory method to
 * create an instance of this fragment.
 */
public class snack extends Fragment {
    String stremail;
    TabAdapter adapter;
    String date;

    public void setDate(String date) {
        this.date = date;
    }
    public snack(String stremail) {
        this.stremail = stremail;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_snack, container, false);

        RecyclerView list=view.findViewById(R.id.snacklist);
        LayoutInflater layoutInflater =getLayoutInflater();
         adapter=new TabAdapter(5,list,getActivity(),stremail,layoutInflater);
        Button btnadd=view.findViewById(R.id.btnsnack);
        if(date!=null){
            adapter.setTime2(date);
            btnadd.setVisibility(View.INVISIBLE);
        }
        adapter.run();


        btnadd.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view2) {
                Intent intent=new Intent(getActivity(),AddFood.class);
                intent.putExtra("stremail",stremail);
                intent.putExtra("wheneat",5);
                startActivityForResult(intent,1);
            }
        });
        return view;
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        adapter.run();
        super.onActivityResult(requestCode, resultCode, data);
    }
}
