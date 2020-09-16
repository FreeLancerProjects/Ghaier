package com.ghiar.activities_fragments.activity_home.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.ghiar.R;
import com.ghiar.activities_fragments.activity_addauction.AddAuctionActivity;
import com.ghiar.activities_fragments.activity_home.HomeActivity;
import com.ghiar.adapters.AuctionAdapter;
import com.ghiar.databinding.FragmentAuctionBinding;
import com.ghiar.databinding.FragmentMoreBinding;
import com.ghiar.models.AuctionModel;
import com.ghiar.models.ProductModel;
import com.ghiar.models.SingleAuctionModel;
import com.ghiar.models.UserModel;
import com.ghiar.preferences.Preferences;
import com.ghiar.remote.Api;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import io.paperdb.Paper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.ghiar.tags.Tags.base_url;

public class Fragment_Auction extends Fragment {

    private HomeActivity activity;
    private FragmentAuctionBinding binding;
    private Preferences preferences;
    private UserModel userModel;
    private String lang;
    private List<SingleAuctionModel> singleAuctionModelList;
    private AuctionAdapter auctionAdapter;

    public static Fragment_Auction newInstance() {

        return new Fragment_Auction();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_auction, container, false);
        initView();
        return binding.getRoot();
    }

    private void initView() {
        singleAuctionModelList=new ArrayList<>();
        activity = (HomeActivity) getActivity();
        preferences = Preferences.getInstance();
        Paper.init(activity);
        lang = Paper.book().read("lang", "ar");
        userModel = preferences.getUserData(activity);
        auctionAdapter=new AuctionAdapter(activity,singleAuctionModelList);
        binding.recView.setLayoutManager(new LinearLayoutManager(activity));
        binding.recView.setAdapter(auctionAdapter);
        binding.llMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity, AddAuctionActivity.class);
                startActivity(intent);
            }
        });
        getAuctions();
    }

    private void getAuctions() {
        Api.getService(base_url).getAuctions( "off").enqueue(new Callback<AuctionModel>() {
            @Override
            public void onResponse(Call<AuctionModel> call, Response<AuctionModel> response) {
                binding.progBar.setVisibility(View.GONE);
                if (response.isSuccessful() && response.body() != null && response.body().getAuction() != null) {
                    singleAuctionModelList.clear();
                    singleAuctionModelList.addAll(response.body().getAuction());
                    if (response.body().getAuction().size() > 0) {
                        // rec_sent.setVisibility(View.VISIBLE);
                        //  Log.e("data",response.body().getData().get(0).getAr_title());

                        // binding.llNoStore.setVisibility(View.GONE);
                        auctionAdapter.notifyDataSetChanged();
                        //   total_page = response.body().getMeta().getLast_page();

                    } else {
                        auctionAdapter.notifyDataSetChanged();

                        //  binding.llNoStore.setVisibility(View.VISIBLE);

                    }
                }
                else {
                    auctionAdapter.notifyDataSetChanged();

                    // binding.llNoStore.setVisibility(View.VISIBLE);

                    //Toast.makeText(activity, getString(R.string.failed), Toast.LENGTH_SHORT).show();
                    try {
                        Log.e("Error_code", response.code() + "_" + response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

            }

            @Override
            public void onFailure(Call<AuctionModel> call, Throwable t) {
             //   Log.d(TAG, t.getMessage());
                binding.progBar.setVisibility(View.GONE);
            }
        });

    }

}
