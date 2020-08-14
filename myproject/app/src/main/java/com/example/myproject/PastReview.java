package com.example.myproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.example.myproject.RemoteService.base_url;

public class PastReview extends AppCompatActivity {
    Spinner spinner;
    List<String> arrayList = new ArrayList<>();

    Retrofit retrofit;
    RemoteService remoteService;
    View morningbar, lunchbar, dinnerbar, midnightsnackbar, snackbar, leftbar;
    TextView pasttxtmoning, pasttxtlunch, pasttxtdinner, pasttxtmidnight, pasttxtsnack, pastleft, excercise, pasttxtleft;
    double recommand;
    String stremail;
    double Weight;
    List<VOexcerciselist> arraylistex = new ArrayList<>();
    RecyclerView excerciseview;
    ExcerciseAdapter ad = new ExcerciseAdapter();
    double excerciseresult = 0;
    String nowdate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setTitle("과거조회");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Intent intent = getIntent();
        recommand = intent.getDoubleExtra("recommand", 0);
        stremail = intent.getStringExtra("stremail");
        Weight = intent.getDoubleExtra("Weight", 0);


        setContentView(R.layout.activity_past_review);
        spinner = findViewById(R.id.dayspinner);
        retrofit = new Retrofit.Builder().baseUrl(base_url).addConverterFactory(GsonConverterFactory.create()).build();
        remoteService = retrofit.create(RemoteService.class);
        morningbar = findViewById(R.id.morningbar);
        lunchbar = findViewById(R.id.lunchbar);
        dinnerbar = findViewById(R.id.dinnerbar);
        midnightsnackbar = findViewById(R.id.midnightbar);
        snackbar = findViewById(R.id.snackbar);
        leftbar = findViewById(R.id.leftbar);

        pasttxtmoning = findViewById(R.id.pasttxtmoning);
        pasttxtlunch = findViewById(R.id.pasttxtlunch);
        pasttxtdinner = findViewById(R.id.pasttxtdinner);
        pasttxtmidnight = findViewById(R.id.pasttxtmidnight);
        pasttxtsnack = findViewById(R.id.pasttxtsnack);

        pasttxtleft = findViewById(R.id.pasttxtleft);

        excerciseview = findViewById(R.id.excerciseview);
        excerciseview.setLayoutManager(new LinearLayoutManager(this));
        excerciseview.setHasFixedSize(true);
        excerciseview.setAdapter(ad);
        excercise = findViewById(R.id.excercise);

        Call<List<String>> call = remoteService.getdateeatlist(stremail);
        call.enqueue(new Callback<List<String>>() {
            @Override
            public void onResponse(Call<List<String>> call, Response<List<String>> response) {
                arrayList = response.body();
                if (arrayList.size() > 0) {
                    Call<List<String>> call2 = remoteService.getdateexcercise(stremail);
                    call2.enqueue(new Callback<List<String>>() {
                        @Override
                        public void onResponse(Call<List<String>> call, Response<List<String>> response) {
                            List<String> arrayList2 = new ArrayList<>();
                            try {
                                arrayList2 = response.body();
                                for (int i = 0; i < arrayList2.size(); i++) {
                                    arrayList.add(arrayList2.get(i));
                                }
                            } catch (Exception e) {
                            }
                            Collections.sort(arrayList,Collections.<String>reverseOrder());
                            SimpleDateFormat format1 = new SimpleDateFormat("yyyy/MM/dd");
                            Date time = new Date();
                            String time1 = format1.format(time);
                            if (arrayList.get(0).equals(time1)) {
                                arrayList.remove(0);
                            }
                            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(PastReview.this, android.R.layout.simple_spinner_item, arrayList);
                            arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            spinner.setAdapter(arrayAdapter);
                            spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                @Override
                                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                    final String tutorialsName = parent.getItemAtPosition(position).toString();
                                    nowdate = tutorialsName;
                                    //--------------------------------------------------------------------------------------------------클릭시 이벤트


                                    //--------------------------------------------------------------------------------------------
                                    Call<List<VOexcerciselist>> call = remoteService.readexcerciselist(stremail, tutorialsName);
                                    call.enqueue(new Callback<List<VOexcerciselist>>() {
                                        @Override
                                        public void onResponse(Call<List<VOexcerciselist>> call, Response<List<VOexcerciselist>> response) {
                                            arraylistex = response.body();
                                            ad.notifyDataSetChanged();
                                        }

                                        @Override
                                        public void onFailure(Call<List<VOexcerciselist>> call, Throwable t) {

                                        }
                                    });
//---------------------------------------------------------------------------
                                    Call<Double> call3 = remoteService.carculexcercise(Weight + "", stremail, tutorialsName);
                                    call3.enqueue(new Callback<Double>() {
                                        @Override
                                        public void onResponse(Call<Double> call, Response<Double> response) {
                                            excerciseresult = response.body();
                                            excercise.setText("그날한 운동양=" + excerciseresult + "Kcal");
                                            String wheneat[] = {"1"};
                                            Foodcarcul foodcarcul = new Foodcarcul(stremail, pasttxtmoning, wheneat);
                                            foodcarcul.setWhen(tutorialsName);
                                            foodcarcul.setList2(morningbar);
                                            foodcarcul.setwehneat2("아침");
                                            foodcarcul.run();
                                            wheneat[0] = "2";
                                            foodcarcul = new Foodcarcul(stremail, pasttxtlunch, wheneat);
                                            foodcarcul.setWhen(tutorialsName);
                                            foodcarcul.setList2(lunchbar);
                                            foodcarcul.setwehneat2("점심");
                                            foodcarcul.run();
                                            wheneat[0] = "3";
                                            foodcarcul = new Foodcarcul(stremail, pasttxtdinner, wheneat);
                                            foodcarcul.setWhen(tutorialsName);
                                            foodcarcul.setList2(dinnerbar);
                                            foodcarcul.setwehneat2("저녁");
                                            ;
                                            foodcarcul.run();
                                            wheneat[0] = "4";
                                            foodcarcul = new Foodcarcul(stremail, pasttxtmidnight, wheneat);
                                            foodcarcul.setWhen(tutorialsName);
                                            foodcarcul.setList2(midnightsnackbar);
                                            foodcarcul.setwehneat2("야식");
                                            foodcarcul.run();
                                            wheneat[0] = "5";
                                            foodcarcul = new Foodcarcul(stremail, pasttxtsnack, wheneat);
                                            foodcarcul.setWhen(tutorialsName);
                                            foodcarcul.setList2(snackbar);
                                            foodcarcul.setwehneat2("간식");
                                            foodcarcul.run();
                                            Call<Double> call4 = remoteService.carculeleft(stremail, recommand + "", Weight + "", tutorialsName);
                                            call4.enqueue(new Callback<Double>() {
                                                @Override
                                                public void onResponse(Call<Double> call, Response<Double> response) {
                                                    double result = response.body();

                                                    pasttxtleft.setText("남음\n" + result + "Kcal");
                                                    LinearLayout.LayoutParams loparams = (LinearLayout.LayoutParams) leftbar.getLayoutParams();
                                                    loparams.width = 0;
                                                    if(result>0){
                                                        int total2 = (int) result;
                                                        loparams.weight = total2;
                                                    }
                                                    else{
                                                        loparams.weight=0;
                                                    }
                                                    leftbar.setLayoutParams(loparams);
                                                    TextView list3 = (TextView) leftbar;
                                                    list3.setText(result + "Kcal");

                                                }

                                                @Override
                                                public void onFailure(Call<Double> call, Throwable t) {

                                                }
                                            });
                                        }

                                        @Override
                                        public void onFailure(Call<Double> call, Throwable t) {
                                            System.out.println("carcul에러" + t.toString());
                                        }
                                    });

                                    Toast.makeText(parent.getContext(), "Selected: " + tutorialsName, Toast.LENGTH_SHORT).show();
                                    //--------------------------------------------------------------------------------------------------------------
                                }

                                @Override
                                public void onNothingSelected(AdapterView<?> adapterView) {

                                }
                            });
                        }

                        @Override
                        public void onFailure(Call<List<String>> call, Throwable t) {

                        }
                    });
                }
            }

            @Override
            public void onFailure(Call<List<String>> call, Throwable t) {
                System.out.println("과거 가져오기 실패" + t.toString());
            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    public void mclick(View view) {
        Intent intent = new Intent(PastReview.this, DayTab.class);
        intent.putExtra("stremail", stremail);
        switch (view.getId()) {
            case R.id.pasttxtmoning:
                intent.putExtra("number", "0");
                break;
            case R.id.pasttxtlunch:
                intent.putExtra("number", "1");
                break;
            case R.id.pasttxtdinner:
                intent.putExtra("number", "2");
                break;
            case R.id.pasttxtmidnight:
                intent.putExtra("number", "3");
                break;
            case R.id.pasttxtsnack:
                intent.putExtra("number", "4");
                break;
        }
        intent.putExtra("past", nowdate);
        startActivity(intent);
    }

    class ExcerciseAdapter extends RecyclerView.Adapter<ExcerciseAdapter.ViewHolder> {

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = getLayoutInflater().inflate(R.layout.item_exerciselist, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            VOexcerciselist vOexcerciselist = arraylistex.get(position);
            holder.txtname.setText(vOexcerciselist.getName());
            long time2 = vOexcerciselist.getTime();
            holder.txttime.setText(toTime(time2));

            double hcar = (vOexcerciselist.getMet() * 3.5 * Weight * 60 / 200) * (int) (time2 / 1000.0 / 60.0 / 60.0);
            double mincar = (vOexcerciselist.getMet() * 3.5 * Weight / 200) * (int) (time2 / 1000.0 / 60.0 % 60);
            double seccar = (vOexcerciselist.getMet() * 3.5 * Weight / 12000) * (int) (time2 % (1000.0 * 60) / 1000.0);
            double msseccar = (vOexcerciselist.getMet() * 3.5 * Weight / 1200000) * (int) (time2 % 1000 / 10);
            holder.txtkcal.setText((Math.round((hcar + mincar + seccar + msseccar) * 100) / 100.0) + "Kcal");

        }

        @Override
        public int getItemCount() {
            return arraylistex.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            TextView txtname, txttime, txtkcal;

            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                txtname = itemView.findViewById(R.id.txtname);
                txttime = itemView.findViewById(R.id.txttime);
                txtkcal = itemView.findViewById(R.id.txtkcal);
            }
        }
    }

    public String toTime(long time) {
        int h = (int) (time / 1000.0 / 60.0 / 60.0);
        int m = (int) (time / 1000.0 / 60.0 % 60);
        int s = (int) (time % (1000.0 * 60) / 1000.0);
        int ms = (int) (time % 1000 / 10.0);

        return String.format("%02d h : %02d m: %02d s: %02d ms", h, m, s, ms);
    }
}
