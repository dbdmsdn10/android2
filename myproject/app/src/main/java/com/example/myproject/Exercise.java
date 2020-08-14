package com.example.myproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

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

public class Exercise extends AppCompatActivity {
    String stremail;
    double Weight = 50;
    Retrofit retrofit;
    RemoteService remoteService;
    List<VOmettable> arraylist = new ArrayList<>();
    tabAdapter ad = new tabAdapter();
    RecyclerView list;
    TextView txtstopwatch, doname, txtkcal;
    double nowmet = 0;
    long time = 0, preTime = 0, pauseTime = 0, time2 = 0, time3 = 0;
    double nowkcal = 0, used = 0, kkk = 0;
    TimeThread timeThread = new TimeThread();
    boolean whether = true;
    SimpleDateFormat format1;
    String metid = "";
    boolean whetherback=false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercise);
        getSupportActionBar().setTitle("운동종류");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        retrofit = new Retrofit.Builder().baseUrl(base_url).addConverterFactory(GsonConverterFactory.create()).build();
        remoteService = retrofit.create(RemoteService.class);
        Intent intent = getIntent();
        stremail = intent.getStringExtra("stremail");
        Weight = intent.getDoubleExtra("Weight", -1);
        format1 = new SimpleDateFormat("yyyy/MM/dd");

        list = findViewById(R.id.metlist);
        list.setLayoutManager(new LinearLayoutManager(this));
        list.setHasFixedSize(true);
        list.setAdapter(ad);
        Call<List<VOmettable>> call = remoteService.listmettable();
        call.enqueue(new Callback<List<VOmettable>>() {
            @Override
            public void onResponse(Call<List<VOmettable>> call, Response<List<VOmettable>> response) {
                arraylist = response.body();
                // System.out.println("갯수="+arraylist.size());
                ad.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<List<VOmettable>> call, Throwable t) {
                System.out.println("운동오류" + t.toString());
            }
        });
        txtstopwatch = findViewById(R.id.txtstopwatch);
        doname = findViewById(R.id.doname);
        txtkcal = findViewById(R.id.txtkcal);
    }

    public void mclick(View view) {
        switch (view.getId()) {
            case R.id.stopwatchstart:
                if (nowmet != 0) {
                    whether = false;
                    ad.notifyDataSetChanged();
                    if (timeThread == null || !timeThread.isAlive()) {
                        if (time != 0) {
                            preTime = System.currentTimeMillis() - time;
                        } else {
                            preTime = System.currentTimeMillis();
                            if (nowkcal != 0) {//db에 저장하기
                            }
                        }

                        timeThread = new TimeThread();
                        pauseTime = System.currentTimeMillis();
                        timeThread.start();
                    }
                } else {
                    AlertDialog.Builder box = new AlertDialog.Builder(Exercise.this);
                    box.setTitle("알림");
                    box.setMessage("운동을 골라주세요");
                    box.setPositiveButton("확인", null);
                    box.show();
                }
                break;
            case R.id.stopwatchpause:
                if (timeThread.isAlive()) {
                    timeThread.interrupt();
                    list.setEnabled(true);
                    used += kkk;
                    //db저장
                    kkk = 0;
                }
                break;
            case R.id.stopwatchstop:
                if (timeThread.isAlive()) {
                    timeThread.interrupt();
                    list.setEnabled(true);
                    //db저장
                    whether = true;
                    ad.notifyDataSetChanged();
                }
                save();

                break;
        }
    }


    class TimeThread extends Thread {
        public void run() {
            try {
                while (!Thread.currentThread().isInterrupted()) {
                    list.setEnabled(false);
                    list.setFocusable(false);
                    sleep(10);
//                    if (DoOr) {// 스톱워치 역으로
//                        time = time3 - (System.currentTimeMillis() - preTime);
//
//                    } else {
                    time = System.currentTimeMillis() - preTime;
                    //  }
                    time2 = System.currentTimeMillis() - pauseTime;

                    double hcar = (nowmet * 3.5 * Weight * 60 / 200) * (int) (time2 / 1000.0 / 60.0 / 60.0);
                    double mincar = (nowmet * 3.5 * Weight / 200) * (int) (time2 / 1000.0 / 60.0 % 60);
                    double seccar = (nowmet * 3.5 * Weight / 12000) * (int) (time2 % (1000.0 * 60) / 1000.0);
                    double msseccar = (nowmet * 3.5 * Weight / 1200000) * (int) (time2 % 1000 / 10);
                    kkk = Math.round((hcar + mincar + seccar + msseccar) * 100) / 100.0;
                    nowkcal = (Math.round((kkk + used) * 100) / 100.0);
                    Message msg = handler.obtainMessage();
                    handler.sendMessage(msg);
                }
            } catch (Exception e) {
                System.out.println("스톱워치 오류" + e.toString());
            }
        }
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            txtstopwatch.setText(toTime(time));
            txtkcal.setText(Double.toString(Math.round((kkk + used) * 100) / 100.0) + "Kcal");
        }
    };


    public String toTime(long time) {
        int h = (int) (time / 1000.0 / 60.0 / 60.0);
        int m = (int) (time / 1000.0 / 60.0 % 60);
        int s = (int) (time % (1000.0 * 60) / 1000.0);
        int ms = (int) (time % 1000 / 10.0);

        return String.format("%02d h: %02d m: %02d s: %02d ms", h, m, s, ms);
    }


    class tabAdapter extends RecyclerView.Adapter<tabAdapter.ViewHolder> {
        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = getLayoutInflater().inflate(R.layout.item_add_food, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            final VOmettable vOmettable = arraylist.get(position);
            holder.txtname.setText(vOmettable.getName());
            holder.txtkcal.setText("운동강도= " + vOmettable.getMet());
            if (whether) {
                holder.layout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        doname.setText(vOmettable.getName() + "   운동강도=" + vOmettable.getMet());
                        nowmet = Long.parseLong(vOmettable.getMet());
                        metid = vOmettable.get_id();
                    }
                });
            } else {
                holder.layout.setOnClickListener(null);
            }
        }

        @Override
        public int getItemCount() {
            return arraylist.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            TextView txtname, txtkcal;
            RelativeLayout layout;

            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                txtname = itemView.findViewById(R.id.txtname);
                txtkcal = itemView.findViewById(R.id.txtkcal);
                layout = itemView.findViewById(R.id.which);
            }
        }
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (item.getItemId() == android.R.id.home) {
            save();
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    public void save() {
        if (kkk != 0 || used != 0) {
            final Date date = new Date();
            final String today = format1.format(date);
            Call<VOexcerciselist> call = remoteService.readexcersice(metid, stremail,today);
            call.enqueue(new Callback<VOexcerciselist>() {
                @Override
                public void onResponse(Call<VOexcerciselist> call, Response<VOexcerciselist> response) {
                    VOexcerciselist vOexcerciselist=response.body();
                    if(vOexcerciselist.get_id()==null){
                        Call<Void> call2=remoteService.insertexercise(metid,time,stremail,today);
                        call2.enqueue(new Callback<Void>() {
                            @Override
                            public void onResponse(Call<Void> call, Response<Void> response) {
                                System.out.println("운동 입력성공");
                                pauseTime = 0;
                                time = 0;
                                used = 0;
                                kkk = 0;
                                if(whetherback){
                                    finish();
                                }
                            }

                            @Override
                            public void onFailure(Call<Void> call, Throwable t) {
                                System.out.println("운동 입력실패"+t.toString());
                            }
                        });
                    }
                    else{
                        long updatetime=time+vOexcerciselist.getTime();
                        Call<Void> call2=remoteService.updateexercise(vOexcerciselist.get_id(),updatetime);
                        call2.enqueue(new Callback<Void>() {
                            @Override
                            public void onResponse(Call<Void> call, Response<Void> response) {
                                System.out.println("운동 수정성공");
                                if(whetherback){
                                    finish();
                                }
                            }

                            @Override
                            public void onFailure(Call<Void> call, Throwable t) {
                                System.out.println("운동 수정실패");
                            }
                        });
                    }
                }

                @Override
                public void onFailure(Call<VOexcerciselist> call, Throwable t) {

                }
            });

        }
    }

    @Override
    public void onBackPressed() {
        save();
        super.onBackPressed();
    }
}
