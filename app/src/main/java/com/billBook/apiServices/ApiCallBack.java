package com.billBook.apiServices;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;

import com.billBook.R;
import com.google.gson.Gson;

import java.io.PrintWriter;
import java.io.StringWriter;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ApiCallBack<T> implements Callback<T> {
    private ApiResponseListener<T> apiListener;
    private String apiName;
    private Context mContext;

    public ApiCallBack(ApiResponseListener<T> apiListener, String apiName, Context mContext) {
        this.apiListener = apiListener;
        this.apiName = apiName;
        this.mContext = mContext;
    }

    @Override
    public void onResponse(@NonNull Call<T> call, @NonNull Response<T> response) {
        Log.e("Response:", new Gson().toJson(response));
        if (response.isSuccessful()) {
            apiListener.onApiSuccess(response.body(), apiName);
        } else {
            apiListener.onApiErrorBody(response.errorBody(), apiName);
        }
    }

    @Override
    public void onFailure(@NonNull Call<T> call, @NonNull Throwable t) {
        try {
            if (t.getMessage() != null) {
                Log.e("FAILURE", t.getMessage());
            }
            apiListener.onApiFailure(mContext.getString(R.string.server_not_responding), apiName);
        } catch (Exception e) {
            StringWriter errors = new StringWriter();
            e.printStackTrace(new PrintWriter(errors));
            Log.e("Exception", errors.toString());
        }
    }
}