package com.billBook.apiServices;

import okhttp3.ResponseBody;

public interface ApiResponseListener<T> {
    void onApiSuccess(T response, String apiName);
    void onApiErrorBody(ResponseBody response, String apiName);
    void onApiFailure(String failureMessage, String apiName);
}
