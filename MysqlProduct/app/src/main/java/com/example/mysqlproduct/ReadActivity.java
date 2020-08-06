package com.example.mysqlproduct;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.example.mysqlproduct.RemoteService.base_url;

public class ReadActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read);
        getSupportActionBar().setTitle("상품정보");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Intent intent=getIntent();
        String code=intent.getStringExtra("code");

        Retrofit retrofit = new Retrofit.Builder().baseUrl(base_url).addConverterFactory(GsonConverterFactory.create()).build();
        RemoteService remoteService=retrofit.create(RemoteService.class);
        Call<ProductVO> call=remoteService.readProduct2(code);
        call.enqueue(new Callback<ProductVO>() {
            @Override
            public void onResponse(Call<ProductVO> call, Response<ProductVO> response) {
                ProductVO productVO=response.body();
                TextView txtcode=findViewById(R.id.txtcode2);
                TextView txtname=findViewById(R.id.txtname2);
                TextView txtprice=findViewById(R.id.txtprice2);
                ImageView img=findViewById(R.id.image3);
                txtcode.setText(productVO.getCode());
                txtname.setText(productVO.getName());
                txtprice.setText(productVO.getPrice());

                Picasso.with(ReadActivity.this).load(base_url+"image/"+productVO.getImage()).into(img);

            }

            @Override
            public void onFailure(Call<ProductVO> call, Throwable t) {

            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId())
        {
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
