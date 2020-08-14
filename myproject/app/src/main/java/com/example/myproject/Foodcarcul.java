package com.example.myproject;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.example.myproject.RemoteService.base_url;

public class Foodcarcul {
    String stremail;
    String[] wheneat;
    double total = 0;
    TextView list;
    String when = "";
    View list2;
    String wehneat2 = "";

    public void setwehneat2(String wehneat2) {
        this.wehneat2 = wehneat2;
    }

    public Foodcarcul(String stremail, TextView list, String[] wheneat) {
        this.stremail = stremail;
        this.list = list;
        this.wheneat = wheneat;
    }



    public void setList2(View list2) {
        this.list2 = list2;
    }


    public void setWhen(String when) {
        this.when = when;
    }

    Retrofit retrofit;
    RemoteService remoteService;


    public void run() {
        retrofit = new Retrofit.Builder().baseUrl(base_url).addConverterFactory(GsonConverterFactory.create()).build();
        remoteService = retrofit.create(RemoteService.class);
        SimpleDateFormat format1 = new SimpleDateFormat("yyyy/MM/dd");
        Date time = new Date();
        String time1 = format1.format(time);

        for (int i = 0; i < wheneat.length; i++) {
            Call<Double> call;
            if (when.equals("")) {
                call = remoteService.readeatlist(stremail, time1, wheneat[i]);
            } else {
                call = remoteService.readeatlist(stremail, when, wheneat[i]);
            }
            final int finalI = i;
            call.enqueue(new Callback<Double>() {
                @Override
                public void onResponse(Call<Double> call, Response<Double> response) {
                    //System.out.println("response= " +response.body());
                    total += response.body();
                    total = Math.round(total * 100) / 100.0;
                    int total2 = (int) total;
                    list.setText(total + "Kcal");
                    if (!when.equals("")) {

                        list.setText(wehneat2 + "\n" + total + "Kcal");
                        LinearLayout.LayoutParams loparams = (LinearLayout.LayoutParams) list2.getLayoutParams();
                        loparams.width = 0;
                        loparams.weight = total2;
                        System.out.println(wehneat2+" 의total2="+total2);
                        list2.setLayoutParams(loparams);
                        TextView list3 = (TextView) list2;
                        list3.setText(total + "Kcal");

                    }

                }
                @Override
                public void onFailure(Call<Double> call, Throwable t) {
                    System.out.println("계산오류" + t.toString());
                }
            });
        }


    }
}
