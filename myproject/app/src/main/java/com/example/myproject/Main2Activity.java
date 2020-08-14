package com.example.myproject;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.example.myproject.RemoteService.base_url;

public class Main2Activity extends AppCompatActivity {
    String stremail;
    Retrofit retrofit;
    RemoteService remoteService;
    VOuser userinfo=new VOuser();
    TextView txtrecommand, txtdateat, txtdaywork, txtdaypenalty, txttotal;
    Daumweather daumweather;
    double excerciseresult;
    double recommand;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        //----------------------------------------------------------------------------------------------------이칸은 gps권한 요청이다
        String[] REQUIRED_PERMISSIONS = {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION};
        int PERMISSIONS_REQUEST_CODE = 100;
        int hasFineLocationPermission = ContextCompat.checkSelfPermission(Main2Activity.this, Manifest.permission.ACCESS_FINE_LOCATION);
        int hasCoarseLocationPermission = ContextCompat.checkSelfPermission(Main2Activity.this, Manifest.permission.ACCESS_COARSE_LOCATION);
        if (hasFineLocationPermission == PackageManager.PERMISSION_GRANTED && hasCoarseLocationPermission == PackageManager.PERMISSION_GRANTED) {
        } else {
            ActivityCompat.requestPermissions(Main2Activity.this, REQUIRED_PERMISSIONS, PERMISSIONS_REQUEST_CODE);//권한 요청
            System.out.println("권한없음");
        }
        //------------------------------------------------------------------------------------------------------------------
        final Intent intent = getIntent();
        retrofit = new Retrofit.Builder().baseUrl(base_url).addConverterFactory(GsonConverterFactory.create()).build();
        remoteService = retrofit.create(RemoteService.class);
        stremail = intent.getStringExtra("stremail");
        txtrecommand = findViewById(R.id.txtrecommand);
        txtdateat = findViewById(R.id.txtdayeat);
        txtdaywork = findViewById(R.id.txtdaywork);
        txtdaypenalty = findViewById(R.id.txtdaypenalty);
        txttotal = findViewById(R.id.txttotal);

        getSupportActionBar().setTitle("메인창");

        settig();


        daumweather = new Daumweather(getSupportActionBar());
        daumweather.execute();
    }

    public void settig() {

        Call<VOuser> call = remoteService.readuserinfo(stremail);
        call.enqueue(new Callback<VOuser>() {
            @Override
            public void onResponse(Call<VOuser> call, Response<VOuser> response) {
                userinfo = response.body();
//                System.out.println("id= " + userinfo.get_id());
//                System.out.println("Weight= " + userinfo.getWeight());
//                System.out.println("Height= " + userinfo.getHeight());
//                System.out.println("Age= " + userinfo.getAge());
//                System.out.println("Gender= " + userinfo.getGender());
//                System.out.println("Height= " + userinfo.getHeight());
                if (userinfo.get_id() == null) {
                    Intent intent2 = new Intent(Main2Activity.this, Userinfo.class);
                    intent2.putExtra("stremail", stremail);
                    startActivity(intent2);
                    finish();
                } else {
                    recommand = userinfo.getWeight() * 10 + 6.25 * userinfo.getHeight() + -5 * userinfo.getAge();
                    if (userinfo.getGender() == 1) {
                        recommand += 5;
                    } else {
                        recommand -= 151;
                    }
                    switch ((int) userinfo.getHow()) {
                        case 1:
                            recommand = recommand * 1.2;
                            break;
                        case 2:
                            recommand = recommand * 1.35;
                            break;
                        case 3:
                            recommand = recommand * 1.5;
                            break;
                        case 4:
                            recommand = recommand * 1.7;
                            break;
                    }
                    txtrecommand.setText(recommand + "Kcal");

                    SimpleDateFormat format1 = new SimpleDateFormat("yyyy/MM/dd");
                    Date time = new Date();
                    String time1 = format1.format(time);
                    Call<Double> call4 = remoteService.carculeleft(stremail,recommand+"",userinfo.getWeight()+"",time1);
                    call4.enqueue(new Callback<Double>() {
                        @Override
                        public void onResponse(Call<Double> call, Response<Double> response) {
                            txttotal.setText(response.body()+"Kcal");
                        }

                        @Override
                        public void onFailure(Call<Double> call, Throwable t) {

                        }
                    });

                    Call<Double> call3 = remoteService.carculexcercise(userinfo.getWeight() + "", stremail, time1);
                    call3.enqueue(new Callback<Double>() {
                        @Override
                        public void onResponse(Call<Double> call, Response<Double> response) {
                            excerciseresult = response.body();
                            txtdaywork.setText(excerciseresult + "Kcal");
                            String wheneat[] = {"1", "2", "3", "4", "5"};
                            Foodcarcul foodcarcul = new Foodcarcul(stremail, txtdateat, wheneat);
                            foodcarcul.run();

                        }

                        @Override
                        public void onFailure(Call<Double> call, Throwable t) {
                            System.out.println("carcul에러" + t.toString());
                        }
                    });
                }
            }
            @Override
            public void onFailure(Call<VOuser> call, Throwable t) {
                System.out.println("오류" + t);

            }
        });


    }



    public void mclick(View view) {
        switch (view.getId()) {
            case R.id.searchbtn:
                Intent intent = new Intent(Main2Activity.this, Websearch.class);
                intent.putExtra("index", daumweather.getIndex());
                startActivity(intent);
                break;
            case R.id.mapbtn:
                intent = new Intent(Main2Activity.this, MapsActivity.class);
                startActivity(intent);
                break;
            case R.id.btndayreview:
                intent = new Intent(Main2Activity.this, DayReview.class);
                intent.putExtra("stremail", stremail);
                startActivityForResult(intent, 2);
                break;
            case R.id.btnend:
                finish();
                break;

            case R.id.btnexcercise:
                intent = new Intent(Main2Activity.this, ExerciseList.class);
                intent.putExtra("stremail", stremail);
                intent.putExtra("Weight", userinfo.getWeight());

                startActivityForResult(intent, 3);
                break;
            case R.id.past:
                intent=new Intent(Main2Activity.this,PastReview.class);
                intent.putExtra("stremail",stremail);
                intent.putExtra("recommand",recommand);
                intent.putExtra("Weight",userinfo.getWeight());
                startActivity(intent);
                break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.editinfo:
                Intent intent = new Intent(Main2Activity.this, Userinfo2.class);
                intent.putExtra("stremail", stremail);
                intent.putExtra("Weight", userinfo.getWeight());
                intent.putExtra("Height", userinfo.getHeight());
                intent.putExtra("Age", userinfo.getAge());
                intent.putExtra("Gender", userinfo.getGender());
                intent.putExtra("How", userinfo.getHow());
                startActivityForResult(intent, 1);
                break;
            case R.id.logout:
                intent = new Intent(Main2Activity.this, MainActivity.class);
                startActivity(intent);
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        settig();


    }
}
