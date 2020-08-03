package com.example.firebasememo;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class MemoAdapter extends RecyclerView.Adapter<MemoAdapter.ViewHolder> {
    ArrayList<Memoclass> arraymemo;
    Context context;

    public MemoAdapter(ArrayList<Memoclass> arraymemo, Context context) {
        this.arraymemo = arraymemo;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_memo,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final Memoclass memoclass=arraymemo.get(position);
        holder.textcontent.setText(memoclass.getContent());
        holder.textdata.setText("생성날짜: "+memoclass.getCreateDate()+"|||수정 날짜"+memoclass.getUpdate());
        holder.memoitem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(context,ReadActivity.class);
                intent.putExtra("key",memoclass.getKey());
                intent.putExtra("content",memoclass.getContent());
                intent.putExtra("createdate",memoclass.getCreateDate());
                ((Activity)context).startActivityForResult(intent,2);
            }
        });
    }

    @Override
    public int getItemCount() {
        return arraymemo.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView textcontent,textdata;
        LinearLayout memoitem;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textcontent=itemView.findViewById(R.id.txtContent);
            textdata=itemView.findViewById(R.id.txtDate);
            memoitem=itemView.findViewById(R.id.memoitem);
        }
    }
}
