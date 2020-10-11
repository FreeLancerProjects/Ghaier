package com.ghiar.activities_fragments.activity_home.fragments.fragment_profile.fragments;

import android.graphics.PorterDuff;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.ghiar.R;
import com.ghiar.activities_fragments.activity_home.HomeActivity;
import com.ghiar.adapters.MyRequiredAdapter;
import com.ghiar.databinding.FragmentOrdersBinding;
import com.ghiar.models.MyRequiredModel;
import com.ghiar.models.UserModel;
import com.ghiar.preferences.Preferences;
import com.ghiar.remote.Api;
import com.ghiar.tags.Tags;
import com.google.android.material.tabs.TabLayout;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import io.paperdb.Paper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FragmentRequired extends Fragment {
    private HomeActivity activity;
    private FragmentOrdersBinding binding;
    private Preferences preferences;
    private MyRequiredAdapter markets_adapter;
    private List<MyRequiredModel.Data> dataList;
    private UserModel userModel;
    private int type=0;
    private String lang;
    private boolean isLoading2 = false;
    private int current_page4=1;
    private LinearLayoutManager manager;
    public static FragmentRequired newInstance()
    {
        return new FragmentRequired();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_orders,container,false);
        initView();
            getClientcurrentrequired();


        return binding.getRoot();
    }

    private void initView() {
        binding.tab.setVisibility(View.GONE);

        dataList=new ArrayList<>();
        preferences = Preferences.getInstance();
        activity = (HomeActivity) getActivity();
        binding.progBar.getIndeterminateDrawable().setColorFilter(ContextCompat.getColor(activity, R.color.colorAccent), PorterDuff.Mode.SRC_IN);
        binding.progBar.setVisibility(View.GONE);

       // markets_adapter=new OrderAdapter(dataList,activity);
        Paper.init(activity);
lang = Paper.book().read("lang", Locale.getDefault().getLanguage());userModel=preferences.getUserData(activity);
        manager=new LinearLayoutManager(activity);
        binding.recView.setLayoutManager(manager);

        markets_adapter=new MyRequiredAdapter(dataList,activity);
        binding.recView.setAdapter(markets_adapter);
        binding.recView.setAdapter(markets_adapter);


    }


    private void getClientcurrentrequired() {
        current_page4=1;

        dataList.clear();
        markets_adapter.notifyDataSetChanged();
        binding.progBar.setVisibility(View.VISIBLE);
        try {


            Api.getService(Tags.base_url)
                    .getclentcurrentrequired(userModel.getUser().getId()+"")
                    .enqueue(new Callback<MyRequiredModel>() {
                        @Override
                        public void onResponse(Call<MyRequiredModel> call, Response<MyRequiredModel> response) {
                            binding.progBar.setVisibility(View.GONE);
                            Log.e("kkkkkk",response.code()+"");
                            if (response.isSuccessful() && response.body() != null && response.body().getData() != null) {
                                dataList.clear();
                                dataList.addAll(response.body().getData());
                                if (response.body().getData().size() > 0) {
                                    // rec_sent.setVisibility(View.VISIBLE);

                                    binding.tvNoOrder.setVisibility(View.GONE);
                                    markets_adapter.notifyDataSetChanged();
                                    //   total_page = response.body().getMeta().getLast_page();

                                } else {
                                    markets_adapter.notifyDataSetChanged();

                                    binding.tvNoOrder.setVisibility(View.VISIBLE);

                                }
                            } else {
                                markets_adapter.notifyDataSetChanged();

                                binding.tvNoOrder.setVisibility(View.VISIBLE);
                                Log.e("Error_code", response.code()+"");
                                //Toast.makeText(this, getString(R.string.failed), Toast.LENGTH_SHORT).show();
                                try {
                                    Log.e("Error_code", response.code() + "_" + response.errorBody().string());
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<MyRequiredModel> call, Throwable t) {
                            try {

                                binding.progBar.setVisibility(View.GONE);
                                binding.tvNoOrder.setVisibility(View.VISIBLE);
                                Toast.makeText(activity, getResources().getString(R.string.something), Toast.LENGTH_LONG).show();


                                Log.e("errorsssss", t.getMessage());
                            } catch (Exception e) {
                            }
                        }
                    });
        } catch (Exception e) {
            Log.e("errorsss", e.getMessage());

            binding.progBar.setVisibility(View.GONE);
            binding.tvNoOrder.setVisibility(View.VISIBLE);

        }
    }



}
