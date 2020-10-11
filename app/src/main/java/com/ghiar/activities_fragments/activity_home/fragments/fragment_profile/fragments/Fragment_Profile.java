package com.ghiar.activities_fragments.activity_home.fragments.fragment_profile.fragments;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;


import com.ghiar.R;
import com.ghiar.activities_fragments.activity_home.HomeActivity;
import com.ghiar.adapters.View2PagerAdapter;
import com.ghiar.adapters.ViewPagerAdapter;
import com.ghiar.databinding.FragmentProfileBinding;
import com.ghiar.models.UserModel;
import com.ghiar.preferences.Preferences;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import io.paperdb.Paper;

public class Fragment_Profile extends Fragment {
    private static Dialog dialog;
    private HomeActivity activity;
    private FragmentProfileBinding binding;
    private String lang;
    private List<Fragment> fragmentList;
    private List<String> titles;
    private View2PagerAdapter adapter;

    private Preferences preferences;
    private UserModel userModel;

    public static Fragment_Profile newInstance() {
        return new Fragment_Profile();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_profile, container, false);
        initView();


        return binding.getRoot();
    }


    private void initView() {
        activity = (HomeActivity) getActivity();
        preferences = Preferences.getInstance();
        userModel = preferences.getUserData(activity);
        Paper.init(activity);
        lang = Paper.book().read("lang", Locale.getDefault().getLanguage());binding.setLang(lang);
        fragmentList = new ArrayList<>();
        titles = new ArrayList<>();
        binding.tab.setupWithViewPager(binding.pager);
        addFragments_Titles();
        binding.pager.setOffscreenPageLimit(fragmentList.size());
binding.setUserModel(userModel.getUser());
binding.setLang(lang);
        adapter = new View2PagerAdapter(getChildFragmentManager());
        adapter.addFragments(fragmentList);
        adapter.addTitles(titles);
        binding.pager.setAdapter(adapter);


    }

    private void addFragments_Titles() {
        fragmentList.add(FragmentMyOrder.newInstance());
        fragmentList.add(FragmentMyAuction.newInstance());

        fragmentList.add(FragmentRequired.newInstance());
        titles.add(getString(R.string.my_orders));
        titles.add(getString(R.string.my_auction));
        titles.add(getString(R.string.require));


    }

}
