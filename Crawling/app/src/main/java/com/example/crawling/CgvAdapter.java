package com.example.crawling;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;

public class CgvAdapter extends RecyclerView.Adapter<CgvAdapter.ViewHolder> {
    Context context;
    ArrayList<HashMap<String,String>> arracgv;

    public CgvAdapter(Context context, ArrayList<HashMap<String, String>> arracgv) {
        this.context = context;
        this.arracgv = arracgv;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_cgv,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        HashMap<String,String> map=arracgv.get(position);
        holder.title.setText(map.get("title"));
        holder.rank.setText(map.get("rank"));

        Picasso.with(context).load(map.get("image")).into(holder.img);
    }

    @Override
    public int getItemCount() {
        return arracgv.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView title,rank;
        ImageView img;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            title=itemView.findViewById(R.id.title2);
            rank=itemView.findViewById(R.id.rank2);
            img=itemView.findViewById(R.id.img);
        }
    }
}
