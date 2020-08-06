package com.example.mysqlproduct;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.example.mysqlproduct.RemoteService.base_url;

public class MainActivity extends AppCompatActivity {
Retrofit retrofit;
RemoteService remoteService;
List<ProductVO> arrayproduct=new ArrayList<>();
ProductAdapter productAdapter=new ProductAdapter();

String strorder="code";
String strquery="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().setTitle("상품관리");
        ListView list=findViewById(R.id.listproduct);
        list.setAdapter(productAdapter);

        retrofit=new Retrofit.Builder().baseUrl(base_url).addConverterFactory(GsonConverterFactory.create()).build();
        remoteService=retrofit.create(RemoteService.class);
        callData(strorder,strquery);

        FloatingActionButton btnadd=findViewById(R.id.btnadd);
        btnadd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(MainActivity.this,AddActivity.class);
                startActivityForResult(intent,2);
            }
        });



    }

    public void callData(String order,String query){
        Call<List<ProductVO>> call=remoteService.listProduct2(order,query);
        call.enqueue(new Callback<List<ProductVO>>() {
            @Override
            public void onResponse(Call<List<ProductVO>> call, Response<List<ProductVO>> response) {
                arrayproduct=response.body();
                //System.out.println("갯수"+arrayproduct.size());
                productAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<List<ProductVO>> call, Throwable t) {
                System.out.println("오류"+t);
            }
        });
    }

    class ProductAdapter extends BaseAdapter{
        @Override
        public int getCount() {
            return arrayproduct.size();
        }

        @Override
        public Object getItem(int i) {

            return null;
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            view=getLayoutInflater().inflate(R.layout.item_product,viewGroup,false);
            TextView txtname=view.findViewById(R.id.txtname);
            TextView txtcode=view.findViewById(R.id.txtcode);
            TextView txtprice=view.findViewById(R.id.txtprice);
            ImageView img=view.findViewById(R.id.image2);
            final ProductVO productVO=arrayproduct.get(i);

            txtcode.setText(productVO.getCode());
            txtname.setText(productVO.getName());
            txtprice.setText(productVO.getPrice()+" 만원");

            Picasso.with(MainActivity.this).load(base_url+"image/"+productVO.getImage()).into(img);

            RelativeLayout item=view.findViewById(R.id.item);
            item.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent=new Intent(MainActivity.this,ReadActivity.class);
                    intent.putExtra("code",productVO.getCode());
                    startActivityForResult(intent,1);
                }
            });
            
            return view;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main,menu);
        MenuItem itemsearch=menu.findItem(R.id.itemSearch);
        SearchView searchview= (SearchView) itemsearch.getActionView();
        searchview.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {

                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                strquery=s;
                callData(strorder,strquery);
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.itemcode:
                strorder="code";
                break;
            case R.id.itemname:
                strorder="pname";
                break;
            case R.id.itemasc:
                strorder="asc";
                break;
            case R.id.itemdesc:
                strorder="desc";
                break;
        }
        callData(strorder,strquery);
        return super.onOptionsItemSelected(item);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(requestCode==2&&resultCode==RESULT_OK){
            Toast.makeText(this,"상품등록완료",Toast.LENGTH_SHORT).show();

        }
        callData("code","");
        super.onActivityResult(requestCode, resultCode, data);
    }
}
