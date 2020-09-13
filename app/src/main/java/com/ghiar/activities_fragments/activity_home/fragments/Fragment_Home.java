package com.ghiar.activities_fragments.activity_home.fragments;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.ghiar.R;
import com.ghiar.activities_fragments.activity_home.HomeActivity;
import com.ghiar.adapters.ServicesAdapter;
import com.ghiar.databinding.FragmentHomeBinding;
import com.ghiar.databinding.FragmentMoreBinding;
import com.ghiar.interfaces.Listeners;
import com.ghiar.models.ServiceModel;
import com.ghiar.models.UserModel;
import com.ghiar.preferences.Preferences;
import com.ghiar.remote.Api;
import com.ghiar.services.Service;
import com.ghiar.tags.Tags;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Locale;

import io.paperdb.Paper;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.ghiar.tags.Tags.base_url;

public class Fragment_Home extends Fragment {

    private static final String TAG = "Fragment_Home";
    private HomeActivity activity;
    private FragmentHomeBinding binding;
    private Preferences preferences;
    private UserModel userModel;
    private String lang;
    private ServicesAdapter servicesAdapter;

    public static Fragment_Home newInstance() {

        return new Fragment_Home();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false);
        initView();
        getServices();
        return binding.getRoot();
    }

    private void initView() {
        activity = (HomeActivity) getActivity();
        preferences = Preferences.getInstance();
        servicesAdapter = new ServicesAdapter(new ArrayList<ServiceModel>(), this.getContext(), this);
        binding.recViewService.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL,false));

        Paper.init(activity);
        lang = Paper.book().read("lang", "ar");
        userModel = preferences.getUserData(activity);
        binding.setLang(lang);

    }


    private void getServices() {

        Api.getService(base_url).getHomeServices().enqueue(new Callback<ServiceModel>() {
            @Override
            public void onResponse(Call<ServiceModel> call, Response<ServiceModel> response) {
                binding.progBarService.setVisibility(View.GONE);
                servicesAdapter.setList(response.body().getMain_services());
                binding.recViewService.setAdapter(servicesAdapter);
            }

            @Override
            public void onFailure(Call<ServiceModel> call, Throwable t) {
                Log.d(TAG, t.getMessage());
                binding.progBarService.setVisibility(View.GONE);

            }
        });

    }


}
