package com.ghiar.activities_fragments.activtity_auction_detials;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ghiar.R;
import com.ghiar.adapters.AuctionSlideAdapter;
import com.ghiar.adapters.Image_Adapter;
import com.ghiar.adapters.SliderAdapter;
import com.ghiar.databinding.ActivityAddAuctionBinding;
import com.ghiar.databinding.ActivityAuctionDetailsBinding;
import com.ghiar.interfaces.Listeners;
import com.ghiar.language.Language;
import com.ghiar.models.SingleAuctionModel;
import com.ghiar.models.UserModel;
import com.ghiar.preferences.Preferences;
import com.ghiar.remote.Api;
import com.ghiar.share.Common;
import com.ghiar.tags.Tags;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import io.paperdb.Paper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AuctionDetialsActivity extends AppCompatActivity implements Listeners.BackListener {
    private ActivityAuctionDetailsBinding binding;
    private String lang;

    private Preferences preferences;
    private UserModel.User userModel;
    private String search_id;
    private List<SingleAuctionModel.Images> imagesList;
    private Image_Adapter image_adapter;
    private AuctionSlideAdapter sliderAdapter;

    @Override
    protected void attachBaseContext(Context newBase) {
        Paper.init(newBase);
        super.attachBaseContext(Language.updateResources(newBase, Paper.book().read("lang", "ar")));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_auction_details);
        initView();
        getdatafromintent();
        getsingleauction();
    }

    private void getdatafromintent() {
        if (getIntent().getStringExtra("search") != null) {
            search_id = getIntent().getStringExtra("search");
        }
    }


    private void initView() {
        Paper.init(this);
        lang = Paper.book().read("lang", Locale.getDefault().getLanguage());
        binding.setBackListener(this);
        binding.setLang(lang);
        preferences = Preferences.getInstance();
        userModel = preferences.getUserData(this);
        imagesList = new ArrayList<>();
        image_adapter = new Image_Adapter(imagesList, this);
        binding.recView.setLayoutManager(new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false));
        binding.recView.setAdapter(image_adapter);
        binding.progBar.setVisibility(View.GONE);
        binding.progBarslider.setVisibility(View.GONE);
    }

    @Override
    public void back() {
//
        finish();
    }

    @Override
    public void onBackPressed() {
        back();
    }

    public void getsingleauction() {
        //   Common.CloseKeyBoard(homeActivity, edt_name);

        ProgressDialog dialog = Common.createProgressDialog(AuctionDetialsActivity.this, getString(R.string.wait));
        dialog.setCancelable(false);
        dialog.show();
        // rec_sent.setVisibility(View.GONE);
        try {


            Api.getService(Tags.base_url)
                    .get_singleauction(search_id)
                    .enqueue(new Callback<SingleAuctionModel>() {
                        @Override
                        public void onResponse(Call<SingleAuctionModel> call, Response<SingleAuctionModel> response) {
                            dialog.dismiss();
                            Log.e("Error_code", response.code() + "");

                            //  binding.progBar.setVisibility(View.GONE);
                            if (response.isSuccessful() && response.body() != null) {
                                //binding.coord1.scrollTo(0,0);
                                setdata(response.body());

                            } else {
                                Log.e("Error_code", response.code() + "");

                                //   Toast.makeText(AuctionDetialsActivity.this, getString(R.string.failed), Toast.LENGTH_SHORT).show();
                                try {
                                    Log.e("Error_code", response.code() + "_" + response.errorBody().string());
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<SingleAuctionModel> call, Throwable t) {
                            try {

                                dialog.dismiss();

                                // Toast.makeText(AdsDetialsActivity.this, getString(R.string.something), Toast.LENGTH_SHORT).show();
                                Log.e("errorsssssss", t.getMessage());
                            } catch (Exception e) {
                            }
                        }
                    });
        } catch (Exception e) {
            Log.e("lllll", e.toString());
            dialog.dismiss();
        }
    }

    private void setdata(SingleAuctionModel body) {
        binding.setModel(body);
        if (body.getImages() != null) {
            imagesList.addAll(body.getImages());
            image_adapter.notifyDataSetChanged();
            sliderAdapter = new AuctionSlideAdapter(this, body.getImages());
            binding.tab.setupWithViewPager(binding.pager);
            binding.pager.setAdapter(sliderAdapter);
        }
    }


    public void showimage(int layoutPosition) {

    }
}
