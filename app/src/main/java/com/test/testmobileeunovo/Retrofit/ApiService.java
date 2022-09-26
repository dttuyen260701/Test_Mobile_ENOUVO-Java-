package com.test.testmobileeunovo.Retrofit;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.test.testmobileeunovo.Models.Matrix;
import com.test.testmobileeunovo.Utils.Contants;

import java.util.ArrayList;
import java.util.Map;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.HTTP;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;

public interface ApiService {

    String temp = "DELETE";

    Gson gson = new GsonBuilder().
            setDateFormat("yyyy-MM-dd HH:mm:ss").
            create();

    ApiService apiService = new Retrofit.Builder().
            baseUrl(Contants.baseURL).
            addConverterFactory(GsonConverterFactory.create(gson)).
            build().
            create(ApiService.class);

    @Headers({
            "Accept: application/json",
            "Content-Type: application/json; charset=utf-8"
    })
    @GET("matrix/getAll.php")
    Call<Respone_Retrofit<ArrayList<Matrix>>> getData_Options(@QueryMap Map<String, String> options);

    @GET("feature/getAll.php")
    Call<Respone_Retrofit> getData(@Query("page") int page,
                                   @Query("step") int step,
                                   @Query("search_txt") String  search_txt);

    //feature/getAll.php/1/user
    @GET("feature/getAll.php/{id}/user")// 1 dấu "/" nếu đường dẫn k cso path
    Call<Respone_Retrofit> getData_Path(@Path("id") int id);

    //feature/getAll.php/1/user?text= ???
    @GET("feature/getAll.php/{id}/user")
    Call<Respone_Retrofit> getData_Path_Params(@Path("id") int id,
                                               @Query("search_txt") String  search_txt);

    @POST("matrix/insertMatrix.php")
    Call<Respone_Retrofit<Retrofit_Respone_Post<Integer>>> insert_Matrix(@Body Matrix matrix);

    @PUT("matrix/updateMatrix.php")
    Call<Respone_Retrofit<Retrofit_Respone_Post<Boolean>>> update(@Body Matrix matrix);

    @HTTP(method = temp, path = "matrix/deleteMatrix.php", hasBody = true)
    Call<Respone_Retrofit<Retrofit_Respone_Post<Boolean>>> delete(@Body Map<String, String> options);
}
