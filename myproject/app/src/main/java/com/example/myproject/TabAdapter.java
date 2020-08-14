package com.example.myproject;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.example.myproject.RemoteService.base_url;

public class TabAdapter extends Thread {
    Retrofit retrofit;
    RemoteService remoteService;
    List<VOeatlist> arraylist = new ArrayList<>();
    int wheneat = 0;
    RecyclerView list;
    Context context;
    String stremail;
    tabAdapter ad;
    LayoutInflater layoutInflater;
    String time2 = "";

    public TabAdapter(int wheneat, RecyclerView list, Context context, String stremail, LayoutInflater layoutInflater) {
        this.wheneat = wheneat;
        this.list = list;
        this.context = context;
        this.stremail = stremail;
        this.layoutInflater = layoutInflater;
    }

    public void setTime2(String time2) {
        this.time2 = time2;
    }

    public void run() {
        arraylist = new ArrayList<>();

        ad = new tabAdapter();
        list.setLayoutManager(new LinearLayoutManager(context));
        list.setHasFixedSize(true);
        list.setAdapter(ad);
        retrofit = new Retrofit.Builder().baseUrl(base_url).addConverterFactory(GsonConverterFactory.create()).build();
        remoteService = retrofit.create(RemoteService.class);
        doit();
    }

    public void doit() {
        String time1;
        if (time2.equals("")) {
            SimpleDateFormat format1 = new SimpleDateFormat("yyyy/MM/dd");
            Date time = new Date();
            time1 = format1.format(time);
        } else {
            time1 = time2;
        }

        Call<List<VOeatlist>> call = remoteService.readeatlist2(stremail, time1, wheneat + "");
        call.enqueue(new Callback<List<VOeatlist>>() {
            @Override
            public void onResponse(Call<List<VOeatlist>> call, Response<List<VOeatlist>> response) {
                arraylist = response.body();
                ad.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<List<VOeatlist>> call, Throwable t) {
                System.out.println("가져오기 오류" + t);
            }
        });
    }


    class tabAdapter extends RecyclerView.Adapter<tabAdapter.ViewHolder> {
        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = layoutInflater.inflate(R.layout.item_add_food, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
            final VOeatlist list = arraylist.get(position);
            holder.txtname.setText(list.getName() + "(" + list.getOnce() + "g or ml)");
            holder.txtkcal.setText(list.getKcal() + "Kcal");
            if (time2.equals("")) {
                holder.which.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        AlertDialog.Builder box = new AlertDialog.Builder(context);
                        box.setTitle("알림");
                        box.setMessage("삭제하시겠습니까?\n" + list.getName() + "(" + list.getOnce() + "g or ml)");
                        box.setPositiveButton("예", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Call<Void> call = remoteService.deleteeatlist(list.get_id());
                                call.enqueue(new Callback<Void>() {
                                    @Override
                                    public void onResponse(Call<Void> call, Response<Void> response) {
                                        doit();
                                    }

                                    @Override
                                    public void onFailure(Call<Void> call, Throwable t) {

                                    }
                                });
                            }
                        });
                        box.setNegativeButton("아니요", null);
                        box.show();
                    }
                });
            }
        }

        @Override
        public int getItemCount() {
            return arraylist.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            TextView txtname, txtkcal;
            RelativeLayout which;

            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                txtname = itemView.findViewById(R.id.txtname);
                txtkcal = itemView.findViewById(R.id.txtkcal);
                which = itemView.findViewById(R.id.which);
            }
        }
    }
}


