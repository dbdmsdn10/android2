package com.example.myproject;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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

public class ExerciseList extends AppCompatActivity {
    String stremail;
    Retrofit retrofit;
    RemoteService remoteService;
    RecyclerView exerciselist;
    ExcerciseAdapter ad=new ExcerciseAdapter();
    List<VOexcerciselist> arraylist = new ArrayList<>();
    TextView dayexcercise;
    double Weight=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercise_list);
        getSupportActionBar().setTitle("한 운동들");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        retrofit = new Retrofit.Builder().baseUrl(base_url).addConverterFactory(GsonConverterFactory.create()).build();
        remoteService = retrofit.create(RemoteService.class);
        final Intent intent=getIntent();
        stremail=intent.getStringExtra("stremail");
        Weight=intent.getDoubleExtra("Weight",0);
        dayexcercise=findViewById(R.id.dayexcercise);


        exerciselist=findViewById(R.id.exerciselist);
        exerciselist.setLayoutManager(new LinearLayoutManager(this));
        exerciselist.setHasFixedSize(true);
        exerciselist.setAdapter(ad);
        SimpleDateFormat format1 = new SimpleDateFormat( "yyyy/MM/dd");
        Date time = new Date();
        String time1 = format1.format(time);
        Call<List<VOexcerciselist>> call=remoteService.readexcerciselist(stremail,time1);
        call.enqueue(new Callback<List<VOexcerciselist>>() {
            @Override
            public void onResponse(Call<List<VOexcerciselist>> call, Response<List<VOexcerciselist>> response) {
                arraylist=response.body();
                System.out.println("운동갯수"+arraylist.size());
                ad.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<List<VOexcerciselist>> call, Throwable t) {
                System.out.println("운동리스트 가져오는것 오류"+t.toString());
            }
        });

        findViewById(R.id.btndo).setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent1=new Intent(ExerciseList.this,Exercise.class);
                intent1.putExtra("stremail",stremail);
                intent1.putExtra("Weight",Weight);
                startActivityForResult(intent1,1);
            }
        });

        Call<Double> call3=remoteService.carculexcercise(Weight+"",stremail,time1);
        call3.enqueue(new Callback<Double>() {
            @Override
            public void onResponse(Call<Double> call, Response<Double> response) {
                Double result=response.body();
                dayexcercise.setText(result+"Kcal");
            }

            @Override
            public void onFailure(Call<Double> call, Throwable t) {
                System.out.println("carcul에러"+t.toString());
            }
        });
    }

    class ExcerciseAdapter extends RecyclerView.Adapter<ExcerciseAdapter.ViewHolder>{
        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = getLayoutInflater().inflate(R.layout.item_exerciselist, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            VOexcerciselist vOexcerciselist=arraylist.get(position);
            holder.txtname.setText(vOexcerciselist.getName());
            long time2=vOexcerciselist.getTime();
            holder.txttime.setText(toTime(time2));

            double hcar = (vOexcerciselist.getMet() * 3.5 * Weight * 60 / 200) * (int) (time2 / 1000.0 / 60.0 / 60.0);
            double mincar = (vOexcerciselist.getMet() * 3.5 * Weight / 200) * (int) (time2 / 1000.0 / 60.0 % 60);
            double seccar = (vOexcerciselist.getMet() * 3.5 * Weight / 12000) * (int) (time2 % (1000.0 * 60) / 1000.0);
            double msseccar = (vOexcerciselist.getMet() * 3.5 * Weight / 1200000) * (int) (time2 % 1000 / 10);
            holder.txtkcal.setText((Math.round((hcar + mincar + seccar + msseccar) * 100) / 100.0)+"Kcal");

        }

        @Override
        public int getItemCount() {
            return arraylist.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            TextView txtname,txttime,txtkcal;
            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                txtname=itemView.findViewById(R.id.txtname);
                txttime=itemView.findViewById(R.id.txttime);
                txtkcal=itemView.findViewById(R.id.txtkcal);
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        SimpleDateFormat format1 = new SimpleDateFormat( "yyyy/MM/dd");
        Date time = new Date();
        String time1 = format1.format(time);

        Call<Double> call3=remoteService.carculexcercise(Weight+"",stremail,time1);
        call3.enqueue(new Callback<Double>() {
            @Override
            public void onResponse(Call<Double> call, Response<Double> response) {
                Double result=response.body();
                dayexcercise.setText(result+"Kcal");
            }

            @Override
            public void onFailure(Call<Double> call, Throwable t) {
                System.out.println("carcul에러"+t.toString());
            }
        });


        Call<List<VOexcerciselist>> call=remoteService.readexcerciselist(stremail,time1);
        call.enqueue(new Callback<List<VOexcerciselist>>() {
            @Override
            public void onResponse(Call<List<VOexcerciselist>> call, Response<List<VOexcerciselist>> response) {
                arraylist=response.body();
               // System.out.println("운동갯수"+arraylist.size());
                ad.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<List<VOexcerciselist>> call, Throwable t) {
                System.out.println("운동리스트 가져오는것 오류"+t.toString());
            }
        });
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId()==android.R.id.home){
            finish();
        }
        return super.onOptionsItemSelected(item);

    }
}
