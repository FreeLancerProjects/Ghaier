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

import com.ghiar.R;
import com.ghiar.activities_fragments.activity_home.HomeActivity;
import com.ghiar.databinding.FragmentHomeBinding;
import com.ghiar.databinding.FragmentMoreBinding;
import com.ghiar.interfaces.Listeners;
import com.ghiar.models.UserModel;
import com.ghiar.preferences.Preferences;
import com.ghiar.remote.Api;
import com.ghiar.tags.Tags;

import java.io.IOException;
import java.util.Locale;

import io.paperdb.Paper;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Fragment_Home extends Fragment{

    private HomeActivity activity;
    private FragmentHomeBinding binding;
    private Preferences preferences;
    private UserModel userModel;
    private String lang;


    public static Fragment_Home newInstance() {

        return new Fragment_Home();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false);
        initView();
        return binding.getRoot();
    }

    private void initView() {
        activity = (HomeActivity) getActivity();
        preferences = Preferences.getInstance();
        Paper.init(activity);
        lang = Paper.book().read("lang","ar");
        userModel = preferences.getUserData(activity);
        binding.setLang(lang);
    }











}
