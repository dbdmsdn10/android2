package com.example.mysqlproduct;



import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;


public interface RemoteService {
    public static final String base_url="http://192.168.0.5:8889/product/";

    @GET("productinfo.jsp")
    Call<List<ProductVO>> listProduct();

    @GET("productinfo.jsp")
    Call<List<ProductVO>> listProduct2(@Query("order") String oeder,@Query("query") String query);
    @GET("read.jsp")
    Call<ProductVO> readProduct2(@Query("code") String code);

    @Multipart
    @POST("insert.jsp")
    Call<ResponseBody> uploadProduct(
            @Part("code") RequestBody strCode,
            @Part("pname") RequestBody strPname,
            @Part("price") RequestBody strPrice,
            @Part MultipartBody.Part image);

    @GET("delete.jsp")
    Call<Void> delete(@Query("code") String code);

}
