package com.example.mysqlfood;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface RemoteService {

    public static final String base_url="http://192.168.0.11:8889/food/";
    @GET("list.jsp")
    Call<List<FoodVO>> listFood();

    @GET("insert.jsp")
    Call<Void> insertFood(@Query("name") String name, @Query("tel") String tel,@Query("address") String address,@Query("latitude") String latitude,@Query("longitude") String longitude);
}
