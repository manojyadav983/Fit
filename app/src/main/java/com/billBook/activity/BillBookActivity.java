package com.billBook.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Context;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import com.billBook.R;
import com.billBook.adapter.BillBookAdapter;
import com.billBook.apiServices.ApiCallBack;
import com.billBook.apiServices.ApiResponse;
import com.billBook.apiServices.ApiResponseListener;
import com.billBook.apiServices.ServiceManager;
import com.billBook.databinding.ActivityBillBookBinding;
import com.billBook.model.BillBookModel;
import com.billBook.utils.AppConstant;
import com.billBook.utils.CommonUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

import cn.jzvd.JZUserActionStandard;
import cn.jzvd.JZVideoPlayer;
import cn.jzvd.JZVideoPlayerStandard;
import okhttp3.ResponseBody;

public class BillBookActivity extends AppCompatActivity implements ApiResponseListener<ApiResponse>, View.OnClickListener {

    private Context mContext;
    private ActivityBillBookBinding binding;
    private List<BillBookModel> alBillBook = new ArrayList<>();
    private BillBookAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_bill_book);
        mContext = this;
        setBillBookRecycler();
        requestVideoList();
    }

    private void setBillBookRecycler() {
        binding.rvBillBook.setHasFixedSize(true);
        binding.rvBillBook.setLayoutManager(new LinearLayoutManager(mContext));
        adapter = new BillBookAdapter(mContext, alBillBook);
        binding.rvBillBook.setAdapter(adapter);
    }

    //   method to fetch the Video from the Server
    private void requestVideoList() {
        if (CommonUtils.isNetworkAvailable(getApplication())) {
            binding.progressBar.setVisibility(View.VISIBLE);
            ServiceManager serviceManager = new ServiceManager();
            ApiCallBack<ApiResponse> callBack = new ApiCallBack<>(this, "videoList", mContext);
            serviceManager.getVideoList(callBack, "Michael jackson", "musicVideo");
        } else {
            CommonUtils.showToast(mContext, mContext.getResources().getString(R.string.internet_connection_error));
        }
    }

    private void setVideoView() {
        binding.videoController.setUp(alBillBook.get(0).getPreviewUrl(),
                JZVideoPlayerStandard.SCREEN_LAYOUT_NORMAL, "");
        JZVideoPlayer.WIFI_TIP_DIALOG_SHOWED = true;
        binding.videoController.startButton.performClick();

    }

    @Override
    protected void onPause() {
        super.onPause();
        if (binding.frameDemo.getVisibility() == View.VISIBLE) {
            JZVideoPlayer.releaseAllVideos();
        }
    }

    @Override
    public void onBackPressed() {
        if (JZVideoPlayer.backPress()) {
            return;
        }
        super.onBackPressed();
    }

    /**
     * @param response server response
     * @param apiName  to differentiate between number of apis by name
     */
    @Override
    public void onApiSuccess(ApiResponse response, String apiName) {
        binding.progressBar.setVisibility(View.GONE);
        if (response != null) {
            if (response.getResultCount() > 0) {
                if (response.getResults() instanceof List) {
                    Gson gson = new Gson();
                    String listData = gson.toJson(response.getResults());
                    List<BillBookModel> videoList = gson.fromJson(listData, new TypeToken<List<BillBookModel>>() {
                    }.getType());
                    alBillBook.clear();
                    alBillBook.addAll(videoList);
                    adapter.notifyDataSetChanged();

                    if (CommonUtils.getPreferencesBoolean(mContext, AppConstant.VIDEO_DATA)) {
                        binding.frameDemo.setVisibility(View.GONE);
                    } else {
                        binding.ivCross.setOnClickListener(this);
                        binding.frameDemo.setVisibility(View.VISIBLE);

                        DisplayMetrics displayMetrics = new DisplayMetrics();
                        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
                        int width = displayMetrics.widthPixels;

                        ViewGroup.LayoutParams layoutParams = binding.frameDemo.getLayoutParams();
                        layoutParams.height = width - width / 3;
                        binding.frameDemo.setLayoutParams(layoutParams);

                        setVideoView();
                    }
                }
            } else {
                alBillBook.clear();
                adapter.notifyDataSetChanged();
            }
        }
    }

    @Override
    public void onApiErrorBody(ResponseBody response, String apiName) {
        binding.progressBar.setVisibility(View.GONE);
        Log.e("Error", "error");
    }

    @Override
    public void onApiFailure(String failureMessage, String apiName) {
        binding.progressBar.setVisibility(View.GONE);
        Log.e("Failure", "failure");
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.ivCross) {
            binding.frameDemo.setVisibility(View.GONE);
            JZVideoPlayer.releaseAllVideos();
            CommonUtils.savePreferencesBoolean(mContext, AppConstant.VIDEO_DATA, true);
        }
    }
}