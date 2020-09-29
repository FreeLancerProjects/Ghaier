package com.ghiar.activities_fragments.activity_model_details;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentStatePagerAdapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Bundle;

import com.ghiar.R;
import com.ghiar.activities_fragments.activity_accessories_spare_details.AccessoriesSparePartsDetailsActivity;
import com.ghiar.activities_fragments.activity_model_details.fragments.Fragment_Accessories;
import com.ghiar.activities_fragments.activity_model_details.fragments.Fragment_Service;
import com.ghiar.activities_fragments.activity_model_details.fragments.Fragment_Spare_Parts;
import com.ghiar.adapters.ViewPagerAdapter;
import com.ghiar.databinding.ActivityAboutAppBinding;
import com.ghiar.databinding.ActivityModelDetailsBinding;
import com.ghiar.interfaces.Listeners;
import com.ghiar.language.Language;

import java.util.ArrayList;
import java.util.List;

import io.paperdb.Paper;

public class ModelDetailsActivity extends AppCompatActivity implements Listeners.BackListener {
    private ActivityModelDetailsBinding binding;
    private String lang;
    private ViewPagerAdapter adapter;
    private List<Fragment> fragmentList;
    private List<String> titles;


    @Override
    protected void attachBaseContext(Context newBase) {
        Paper.init(newBase);
        super.attachBaseContext(Language.updateResources(newBase, Paper.book().read("lang", "ar")));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_model_details);
        initView();
    }


    private void initView() {
        fragmentList = new ArrayList<>();
        titles = new ArrayList<>();
        Paper.init(this);
        lang = Paper.book().read("lang", "ar");
        binding.setBackListener(this);
        binding.setLang(lang);


        fragmentList.add(Fragment_Spare_Parts.newInstance());
        fragmentList.add(Fragment_Accessories.newInstance());
        fragmentList.add(Fragment_Service.newInstance());

        titles.add(getString(R.string.spare_parts));
        titles.add(getString(R.string.accessories));
        titles.add(getString(R.string.services));

        binding.tab.setupWithViewPager(binding.pager);
        adapter = new ViewPagerAdapter(getSupportFragmentManager(), FragmentStatePagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        adapter.addFragments_Titles(fragmentList, titles);
        binding.pager.setAdapter(adapter);
        binding.pager.setOffscreenPageLimit(3);
        binding.image.setOnClickListener(v -> {
            Intent intent = new Intent(this, AccessoriesSparePartsDetailsActivity.class);
            startActivity(intent);
        });

    }


    @Override
    public void back() {
        finish();
    }

}
