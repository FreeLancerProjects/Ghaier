package com.ghiar.activities_fragments.activity_model_details;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentStatePagerAdapter;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import com.ghiar.R;
import com.ghiar.activities_fragments.activity_accessories_spare_details.AccessoriesSparePartsDetailsActivity;
import com.ghiar.activities_fragments.activity_model_details.fragments.Fragment_Accessories;
import com.ghiar.activities_fragments.activity_model_details.fragments.Fragment_Service;
import com.ghiar.activities_fragments.activity_model_details.fragments.Fragment_Spare_Parts;
import com.ghiar.activities_fragments.activity_service_center.ServiceCenterActivity;
import com.ghiar.activities_fragments.activity_service_center_detials.ServiceCenterDetialsActivity;
import com.ghiar.adapters.ViewPagerAdapter;
import com.ghiar.databinding.ActivityAboutAppBinding;
import com.ghiar.databinding.ActivityModelDetailsBinding;
import com.ghiar.interfaces.Listeners;
import com.ghiar.language.Language;
import com.ghiar.models.MarkDataInModel;
import com.ghiar.models.MarkModel;

import java.util.ArrayList;
import java.util.List;

import io.paperdb.Paper;

public class ModelDetailsActivity extends AppCompatActivity implements Listeners.BackListener {
    private ActivityModelDetailsBinding binding;
    private String lang;
    private ViewPagerAdapter adapter;
    private List<Fragment> fragmentList;
    private List<String> titles;
    public String markid;
    private MarkModel markModel;
    private Intent intent;
    private static final int REQUEST_PHONE_CALL = 1;

    @Override
    protected void attachBaseContext(Context newBase) {
        Paper.init(newBase);
        super.attachBaseContext(Language.updateResources(newBase, Paper.book().read("lang", "ar")));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_model_details);
        getDataFromIntent();
        initView();

    }

    private void getDataFromIntent() {
        Intent intent = getIntent();
        if (intent.getStringExtra("search") != null) {
            markid = intent.getStringExtra("search");
            markModel = (MarkModel) intent.getSerializableExtra("data");
        }
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
//        binding.image.setOnClickListener(v -> {
//            Intent intent = new Intent(this, AccessoriesSparePartsDetailsActivity.class);
//            startActivity(intent);
//        });

    }

    public void call(String s) {
        intent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", s, null));


        if (intent != null) {
            if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CALL_PHONE}, REQUEST_PHONE_CALL);
                } else {
                    startActivity(intent);
                }
            } else {
                startActivity(intent);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case REQUEST_PHONE_CALL: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        if (this.checkSelfPermission(Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                            // TODO: Consider calling
                            //    Activity#requestPermissions
                            // here to request the missing permissions, and then overriding
                            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                            //                                          int[] grantResults)
                            // to handle the case where the user grants the permission. See the documentation
                            // for Activity#requestPermissions for more details.
                            return;
                        }
                    }
                    startActivity(intent);
                } else {

                }
                return;
            }
        }
    }

    @Override
    public void back() {
        finish();
    }

    public void showservicecenter(MarkDataInModel a) {
        Intent intent = new Intent(ModelDetailsActivity.this, AccessoriesSparePartsDetailsActivity.class);
        intent.putExtra("search", a.getId() + "");
        startActivity(intent);
    }
}
