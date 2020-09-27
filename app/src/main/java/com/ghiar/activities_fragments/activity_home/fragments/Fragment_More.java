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
import com.ghiar.activities_fragments.activity_about_app.AboutAppActivity;
import com.ghiar.activities_fragments.activity_contact_us.ContactUsActivity;
import com.ghiar.activities_fragments.activity_home.HomeActivity;
import com.ghiar.databinding.FragmentMoreBinding;
import com.ghiar.interfaces.Listeners;
import com.ghiar.models.UserModel;
import com.ghiar.preferences.Preferences;
import com.ghiar.remote.Api;
import com.ghiar.tags.Tags;

import java.io.IOException;
import java.util.Locale;

import io.paperdb.Paper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Fragment_More extends Fragment implements Listeners.SettingActions {

    private HomeActivity activity;
    private FragmentMoreBinding binding;
    private Preferences preferences;
    private UserModel.User userModel;
    private String lang;


    public static Fragment_More newInstance() {

        return new Fragment_More();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_more, container, false);
        initView();
        return binding.getRoot();
    }

    private void initView() {
        activity = (HomeActivity) getActivity();
        preferences = Preferences.getInstance();
        Paper.init(activity);
        lang = Paper.book().read("lang", Locale.getDefault().getLanguage());
        userModel = preferences.getUserData(activity);
        binding.setLang(lang);
        binding.setAction(this);

    }


    @Override
    public void commission() {

    }

    @Override
    public void shop() {

    }

    @Override
    public void contactUs() {
        Intent intent = new Intent(activity, ContactUsActivity.class);
        startActivity(intent);
    }

    @Override
    public void terms() {
        Intent intent = new Intent(activity, AboutAppActivity.class);
        intent.putExtra("type", 1);
        startActivity(intent);
    }

    @Override
    public void aboutApp() {
        Intent intent = new Intent(activity, AboutAppActivity.class);
        intent.putExtra("type", 2);
        startActivity(intent);
    }

    @Override
    public void logout() {
        activity.logout();
    }

    @Override
    public void share() {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TEXT, "https://play.google.com/store/apps/details?id=" + activity.getPackageName());
        startActivity(intent);
    }

    @Override
    public void rateApp() {
        try {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + activity.getPackageName())));
        } catch (ActivityNotFoundException e1) {
            try {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://play.google.com/store/apps/details?id=" + activity.getPackageName())));
            } catch (ActivityNotFoundException e2) {
                Toast.makeText(activity, "You don't have any app that can open this link", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void arLang() {
        if (lang.equals("en")) {
            activity.refreshActivity("ar");
        }
    }

    @Override
    public void enLang() {
        if (lang.equals("ar")) {
            activity.refreshActivity("en");
        }
    }




}
