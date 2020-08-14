package com.example.myproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

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

public class AddFood extends AppCompatActivity {
    String stremail;
    Retrofit retrofit;
    RemoteService remoteService;
    List<VOfood> arraylist = new ArrayList<>();
    FoodAdapter ad = new FoodAdapter();
    ProgressDialog progress;
    int wheneat;
    boolean what = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_food);
        getSupportActionBar().setTitle("음식검색");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        RecyclerView list = findViewById(R.id.list);
        list.setLayoutManager(new LinearLayoutManager(this));
        list.setHasFixedSize(true);
        list.setAdapter(ad);
        Intent intent = getIntent();
        wheneat=intent.getIntExtra("wheneat",0);
        stremail=intent.getStringExtra("stremail");
        retrofit = new Retrofit.Builder().baseUrl(base_url).addConverterFactory(GsonConverterFactory.create()).build();
        remoteService = retrofit.create(RemoteService.class);
        new LoadingThread().execute();


    }

    //-----------------------------------------------------
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_addfood, menu);
        MenuItem itemfoodsearch = menu.findItem(R.id.itemfoodsearch);
        SearchView searchView = (SearchView) itemfoodsearch.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {//검색
            @Override
            public boolean onQueryTextSubmit(String s) {
                Call<List<VOfood>> call;
                if (!what) {
                    call = remoteService.Foodsearch(s, "original");
                } else {
                    call = remoteService.Foodsearch(s, stremail);
                }
                call.enqueue(new Callback<List<VOfood>>() {
                    @Override
                    public void onResponse(Call<List<VOfood>> call, Response<List<VOfood>> response) {
                        arraylist = response.body();
                        ad.notifyDataSetChanged();
                    }

                    @Override
                    public void onFailure(Call<List<VOfood>> call, Throwable t) {
                        System.out.println("검색오류" + t);
                    }
                });
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
            case R.id.dooriginal:
                what = false;
                new LoadingThread().execute();
                break;
            case R.id.dopersonal:
                what = true;
                new LoadingThread().execute();
                break;
            case R.id.insert:
                AlertDialog.Builder box = new AlertDialog.Builder(AddFood.this);
                box.setTitle("새로운 음식만들기");
                final LayoutInflater inflater = getLayoutInflater();
                final View dialoglayout = inflater.inflate(R.layout.item_insert_food, null);
                box.setView(dialoglayout);
                box.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        EditText foodname = dialoglayout.findViewById(R.id.foodname);
                        EditText foodmount = dialoglayout.findViewById(R.id.foodmount);
                        EditText foodkcal = dialoglayout.findViewById(R.id.foodkcal);
                        String txtfoodname = foodname.getText().toString();
                        String txtfoodmount = foodmount.getText().toString();
                        String txtfoodkcal = foodkcal.getText().toString();
                        if (txtfoodname.equals("") || txtfoodkcal.equals("")) {
                            Toast.makeText(AddFood.this, "음식이름과 열량은 필수값입니다", Toast.LENGTH_LONG);
                        } else {
                            Call<Void> call = remoteService.insert(stremail, txtfoodname, txtfoodkcal, txtfoodmount);
                            call.enqueue(new Callback<Void>() {
                                @Override
                                public void onResponse(Call<Void> call, Response<Void> response) {
                                    new LoadingThread().execute();
                                }

                                @Override
                                public void onFailure(Call<Void> call, Throwable t) {

                                }
                            });
                        }
                    }
                });
                box.setNegativeButton("취소", null);
                box.show();
                break;
        }
        return super.onOptionsItemSelected(item);

    }

    //----------------------------------------------------------
    class FoodAdapter extends RecyclerView.Adapter<FoodAdapter.ViewHolder> {
        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = getLayoutInflater().inflate(R.layout.item_add_food, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            final VOfood food = arraylist.get(position);
            holder.txtname.setText(food.getName() + "(" + food.getOnce() + " g or ml)");
            holder.txtkcal.setText(food.getKcal() + "Kcal");
            holder.which.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    AlertDialog.Builder box = new AlertDialog.Builder(AddFood.this);
                    box.setTitle("음식정보");

                    box.setMessage("섭취하시겠습니까?\n"+food.getName() + "(" + food.getOnce() + " g or ml)\n" + food.getKcal() + "Kcal");
                    box.setPositiveButton("예", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            SimpleDateFormat format1 = new SimpleDateFormat("yyyy/MM/dd");
                            Date time = new Date();
                            String time1 = format1.format(time);
                            Call<Void> call = remoteService.inserteatlist(time1, stremail, food.get_id() + "", wheneat + "");
                            call.enqueue(new Callback<Void>() {
                                @Override
                                public void onResponse(Call<Void> call, Response<Void> response) {

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

            holder.which.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    AlertDialog.Builder box = new AlertDialog.Builder(AddFood.this);
                    box.setTitle("알림");
                    if (!what) {
                        box.setMessage("즐겨찾기에 추가하시겠습니까?\n 음식정보:" + food.getName() + "(" + food.getOnce() + " g or ml)\n" + food.getKcal() + "Kcal");
                        box.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Call<Void> call = remoteService.insert(stremail, food.getName(), food.getKcal(), food.getOnce());
                                call.enqueue(new Callback<Void>() {
                                    @Override
                                    public void onResponse(Call<Void> call, Response<Void> response) {

                                    }

                                    @Override
                                    public void onFailure(Call<Void> call, Throwable t) {

                                    }
                                });
                            }
                        });
                        box.setNegativeButton("취소", null);
                    } else {
                        box.setMessage("즐겨찾기에서 수정, 삭제가 가능합니다");
                        box.setPositiveButton("삭제", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Call<Void> call = remoteService.delete(food.get_id());
                                call.enqueue(new Callback<Void>() {
                                    @Override
                                    public void onResponse(Call<Void> call, Response<Void> response) {
                                        new LoadingThread().execute();
                                    }

                                    @Override
                                    public void onFailure(Call<Void> call, Throwable t) {

                                    }
                                });
                            }
                        });
                        box.setNegativeButton("수정", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                AlertDialog.Builder box = new AlertDialog.Builder(AddFood.this);
                                box.setTitle("새로운 음식만들기");
                                final LayoutInflater inflater = getLayoutInflater();
                                final View dialoglayout = inflater.inflate(R.layout.item_insert_food, null);
                                EditText foodname = dialoglayout.findViewById(R.id.foodname);
                                EditText foodmount = dialoglayout.findViewById(R.id.foodmount);
                                EditText foodkcal = dialoglayout.findViewById(R.id.foodkcal);
                                foodname.setText(food.getName());
                                foodmount.setText(food.getOnce());
                                foodkcal.setText(food.getKcal());
                                box.setView(dialoglayout);
                                box.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        EditText foodname = dialoglayout.findViewById(R.id.foodname);
                                        EditText foodmount = dialoglayout.findViewById(R.id.foodmount);
                                        EditText foodkcal = dialoglayout.findViewById(R.id.foodkcal);
                                        String txtfoodname = foodname.getText().toString();
                                        String txtfoodmount = foodmount.getText().toString();
                                        String txtfoodkcal = foodkcal.getText().toString();
                                        if (txtfoodname.equals("") || txtfoodkcal.equals("")) {
                                            Toast.makeText(AddFood.this, "음식이름과 열량은 필수값입니다", Toast.LENGTH_LONG);
                                        } else {
                                            Call<Void> call = remoteService.update(stremail, txtfoodname, txtfoodkcal, txtfoodmount,food.get_id());
                                            System.out.println(stremail+"  "+txtfoodname+"  "+txtfoodkcal+"  "+txtfoodmount+"  "+food.get_id()+"  ");
                                            call.enqueue(new Callback<Void>() {
                                                @Override
                                                public void onResponse(Call<Void> call, Response<Void> response) {

                                                    new LoadingThread().execute();
                                                }

                                                @Override
                                                public void onFailure(Call<Void> call, Throwable t) {
                                                    System.out.println("수정오류"+t.toString());
                                                }
                                            });
                                        }
                                    }
                                });
                                box.setNegativeButton("취소",null);
                                box.show();
                            }
                        });
                    }

                    box.show();
                    return false;
                }
            });
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


    public void calllist() {
        Call<List<VOfood>> call;
        if (what) {
            call = remoteService.FoodList(stremail);
        } else {
            call = remoteService.FoodList("original");
        }
        call.enqueue(new Callback<List<VOfood>>() {
            @Override
            public void onResponse(Call<List<VOfood>> call, Response<List<VOfood>> response) {
                arraylist = response.body();
                ad.notifyDataSetChanged();
                progress.dismiss();//더이상 안보이게
            }

            @Override
            public void onFailure(Call<List<VOfood>> call, Throwable t) {
                System.out.println("add오류" + t);
            }
        });
    }


    //로딩창 쓰레드
    class LoadingThread extends AsyncTask<String, String, String> {//네이버에 접속 및 값 가져오기

        @Override
        protected void onPreExecute() {//가장 먼저 시작됨
            progress = new ProgressDialog(AddFood.this);
            progress.setMessage("검색중입니다");
            progress.show();
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... strings) {
            calllist();
            return null;
        }
    }
}
