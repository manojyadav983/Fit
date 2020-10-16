package com.billBook.apiServices;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface Api {

    @GET("search/")
    Call<ApiResponse> getVideoList(@Query("term") String terms, @Query("media") String media);

}