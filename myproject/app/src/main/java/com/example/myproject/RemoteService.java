package com.example.myproject;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface RemoteService {

    public static final String base_url="http://192.168.0.8:8889/Myproject/";

    @GET("list.jsp")
    Call<List<VOfood>> FoodList(@Query("who") String who);

    @GET("read.jsp")
    Call<List<VOfood>> Foodsearch(@Query("name") String name,@Query("who") String who);

    @GET("readuserid.jsp")
    Call<List<String>> readuserid(@Query("_id") String _id);

    @GET("insertuserid.jsp")
    Call<Void> insertuserid(@Query("_id") String _id,@Query("password") String password);

    @GET("insertuserinfo.jsp")
    Call<Void> insertuserinfo(@Query("_id") String _id,@Query("weight") String weight,@Query("height") String height,@Query("age") String age,@Query("gender") String gender,@Query("how") String how);


    @GET("readuserinfo.jsp")
    Call<VOuser> readuserinfo(@Query("_id") String _id);

    @GET("updateuserinfo.jsp")
    Call<Void> updateuserinfo(@Query("_id") String _id,@Query("weight") String weight,@Query("height") String height,@Query("age") String age,@Query("gender") String gender,@Query("how") String how);

    @GET("inserteatlist.jsp")
    Call<Void> inserteatlist(@Query("eatdate") String eatdate,@Query("who") String who,@Query("foodid") String foodid,@Query("wheneat") String wheneat);

    @GET("readeatlist2.jsp")
    Call<List<VOeatlist>> readeatlist2(@Query("who") String who,@Query("eatdate") String eatdate,@Query("wheneat") String wheneat);

    @GET("readeatlist.jsp")
    Call<Double> readeatlist(@Query("who") String who,@Query("eatdate") String eatdate,@Query("wheneat") String wheneat);

    @GET("deleteeatlist.jsp")
    Call<Void> deleteeatlist(@Query("_id") String _id);

    @GET("listmettable.jsp")
    Call<List<VOmettable>> listmettable();

    @GET("readexcerciselist.jsp")
    Call<List<VOexcerciselist>> readexcerciselist(@Query("who") String who,@Query("dodate") String dodate);

    @GET("insertexercise.jsp")
    Call<Void> insertexercise(@Query("metid") String metid,@Query("time") long time,@Query("who") String who,@Query("dodate") String dodate);

    @GET("readexcersice.jsp")
    Call<VOexcerciselist> readexcersice(@Query("metid") String metid,@Query("who") String who,@Query("dodate") String dodate);

    @GET("updateexercise.jsp")
    Call<Void> updateexercise(@Query("_id") String _id,@Query("time") long time);

    @GET("carculexcercise.jsp")
    Call<Double> carculexcercise(@Query("Weight") String Weight,@Query("who") String who,@Query("dodate") String dodate);

    @GET("getdateeatlist.jsp")
    Call<List<String>> getdateeatlist(@Query("who") String who);

    @GET("getdateexcercise.jsp")
    Call<List<String>> getdateexcercise(@Query("who") String who);

    @GET("insert.jsp")
    Call<Void> insert(@Query("who") String who,@Query("name") String name,@Query("kcal") String kcal,@Query("once") String once);

    @GET("delete.jsp")
    Call<Void> delete(@Query("_id") String _id);

    @GET("update.jsp")
    Call<Void> update(@Query("who") String who,@Query("name") String name,@Query("kcal") String kcal,@Query("once") String once,@Query("_id") String _id);

    @GET("carculeleft.jsp")
    Call<Double> carculeleft(@Query("who") String who,@Query("recommand") String recommand,@Query("Weight") String Weight,@Query("dodate") String dodate);
}
