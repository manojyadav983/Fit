package com.billBook.apiServices;

import android.util.Log;

import retrofit2.Callback;

public class ServiceManager extends ApiClient {

   /* public ServiceManager() {
    }*/

    public void getVideoList(Callback<ApiResponse> callback, String search, String media) {
        ApiClient.current().getVideoList(search, media).enqueue(callback);
        Log.e("URL===>>", String.valueOf(ApiClient.current().getVideoList(search, media).request().url()));
    }
}