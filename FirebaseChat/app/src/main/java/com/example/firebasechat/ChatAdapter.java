package com.example.firebasechat;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ViewHolder> {
    Context context;
    ArrayList<Chat> arraychat;
    String email;

    public ChatAdapter(Context context, ArrayList<Chat> arraychat, String email) {
        this.context = context;
        this.arraychat = arraychat;
        this.email = email;
    }

    @NonNull
    @Override
    public ChatAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chat, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ChatAdapter.ViewHolder holder, int position) {
        Chat chat = arraychat.get(position);
        holder.txtcontent.setText(chat.getContent());
        holder.txtdate.setText(chat.getWdate());
        holder.txtemail.setText((chat.getEmail()));
//        LinearLayout.LayoutParams prmContent= (LinearLayout.LayoutParams) holder.txtcontent.getLayoutParams(); 이것들은 layout을 안가져오고 하는 방법이다
//        LinearLayout.LayoutParams prmDate= (LinearLayout.LayoutParams) holder.txtcontent.getLayoutParams();

        if (chat.getEmail().equals(email)) {
//            prmContent.gravity=Gravity.RIGHT;
//            prmDate.gravity=Gravity.RIGHT;
            holder.itemlayout.setGravity(Gravity.END);
            holder.txtcontent.setTextColor(Color.RED);
            holder.txtemail.setVisibility(View.GONE);
        } else {
//            prmContent.gravity=Gravity.RIGHT;
//            prmDate.gravity=Gravity.RIGHT;
            holder.itemlayout.setGravity(Gravity.START);
            holder.txtcontent.setTextColor(Color.BLUE);
        }
    }

    @Override
    public int getItemCount() {
        return arraychat.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtdate, txtcontent,txtemail;
        LinearLayout itemlayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtdate = itemView.findViewById(R.id.txtDate);
            txtcontent = itemView.findViewById(R.id.txtcontent);
            itemlayout = itemView.findViewById(R.id.itemlayout);
            txtemail=itemView.findViewById(R.id.txtemail);
        }
    }
}
