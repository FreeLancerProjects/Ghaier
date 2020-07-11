package com.ghiar.activities_fragments.activity_model_details.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.ghiar.R;
import com.ghiar.activities_fragments.activity_model_details.ModelDetailsActivity;
import com.ghiar.databinding.FragmentServicesBinding;
import com.ghiar.databinding.FragmentSpareAccessoriesBinding;
import com.ghiar.models.UserModel;
import com.ghiar.preferences.Preferences;

import io.paperdb.Paper;

public class Fragment_Service extends Fragment {

    private ModelDetailsActivity activity;
    private FragmentServicesBinding binding;
    private Preferences preferences;
    private UserModel userModel;
    private String lang;


    public static Fragment_Service newInstance() {

        return new Fragment_Service();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_services, container, false);
        initView();
        return binding.getRoot();
    }

    private void initView() {
        activity = (ModelDetailsActivity) getActivity();
        preferences = Preferences.getInstance();
        Paper.init(activity);
        lang = Paper.book().read("lang","ar");
        userModel = preferences.getUserData(activity);
        binding.setLang(lang);
    }











}

