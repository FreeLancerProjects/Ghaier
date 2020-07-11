package com.ghiar.activities_fragments.activity_accessories_spare_details;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;

import android.content.Context;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Bundle;

import com.ghiar.R;
import com.ghiar.databinding.ActivityAboutAppBinding;
import com.ghiar.databinding.ActivityAccessoriesSparePartsDetailsBinding;
import com.ghiar.interfaces.Listeners;
import com.ghiar.language.Language;

import io.paperdb.Paper;

public class AccessoriesSparePartsDetailsActivity extends AppCompatActivity implements Listeners.BackListener{
    private ActivityAccessoriesSparePartsDetailsBinding binding;
    private String lang;

    @Override
    protected void attachBaseContext(Context newBase) {
        Paper.init(newBase);
        super.attachBaseContext(Language.updateResources(newBase, Paper.book().read("lang","ar")));
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_accessories_spare_parts_details);
        initView();
    }




    private void initView()
    {
        Paper.init(this);
        lang = Paper.book().read("lang", "ar");
        binding.setBackListener(this);
        binding.setLang(lang);

    }


    @Override
    public void back() {
        finish();
    }

}
