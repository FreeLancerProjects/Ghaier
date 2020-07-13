package com.ghiar.activities_fragments.activtity_auction_detials;

import android.content.Context;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.ghiar.R;
import com.ghiar.databinding.ActivityAddAuctionBinding;
import com.ghiar.databinding.ActivityAuctionDetailsBinding;
import com.ghiar.interfaces.Listeners;
import com.ghiar.language.Language;
import com.ghiar.models.UserModel;
import com.ghiar.preferences.Preferences;

import java.util.Locale;

import io.paperdb.Paper;

public class AuctionDetialsActivity extends AppCompatActivity implements Listeners.BackListener {
    private ActivityAuctionDetailsBinding binding;
    private String lang;

    private Preferences preferences;
    private UserModel userModel;


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
    }


    private void initView() {
        Paper.init(this);
        lang = Paper.book().read("lang", Locale.getDefault().getLanguage());
        binding.setBackListener(this);
        binding.setLang(lang);

        preferences = Preferences.getInstance();
        userModel = preferences.getUserData(this);
        Paper.init(this);
        lang = Paper.book().read("lang", Locale.getDefault().getLanguage());
        binding.setBackListener(this);
        binding.setLang(lang);



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


}
