package com.ghiar.activities_fragments.activity_service_center;

import android.content.Context;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ghiar.R;
import com.ghiar.activities_fragments.activity_home.HomeActivity;
import com.ghiar.adapters.ServiceCenterAdapter;
import com.ghiar.databinding.ActivityNotificationBinding;
import com.ghiar.databinding.ActivityServicesCenterBinding;
import com.ghiar.interfaces.Listeners;
import com.ghiar.language.Language;
import com.ghiar.models.ServiceCenterModel;
import com.ghiar.models.UserModel;
import com.ghiar.preferences.Preferences;
import com.ghiar.remote.Api;

import java.util.Locale;

import io.paperdb.Paper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.ghiar.tags.Tags.base_url;

public class ServiceCenterActivity extends AppCompatActivity implements Listeners.BackListener {

    private static final String TAG = "ServiceCenterActivity";
    private ActivityServicesCenterBinding binding;
    private String lang;
    // private List<NotificationDataModel.NotificationModel> notificationModelList;
    //private NotificationAdapter adapter;
    private Preferences preferences;
    private UserModel userModel;
    private ServiceCenterAdapter serviceCenterAdapter;
    private int serviceId;

    @Override
    protected void attachBaseContext(Context newBase) {
        Paper.init(newBase);
        super.attachBaseContext(Language.updateResources(newBase, Paper.book().read("lang", "ar")));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_services_center);
        initView();
    }


    private void initView() {
        Paper.init(this);
        serviceId = getIntent().getIntExtra("serviceId",1);
        lang = Paper.book().read("lang", Locale.getDefault().getLanguage());
        binding.setBackListener(this);
        binding.setLang(lang);
        binding.progBar.getIndeterminateDrawable().setColorFilter(ContextCompat.getColor(this, R.color.colorPrimary), PorterDuff.Mode.SRC_IN);

        preferences = Preferences.getInstance();
        userModel = preferences.getUserData(this);
        Paper.init(this);
        lang = Paper.book().read("lang", Locale.getDefault().getLanguage());
        binding.setBackListener(this);
        binding.setLang(lang);
        binding.progBar.getIndeterminateDrawable().setColorFilter(ContextCompat.getColor(this, R.color.colorPrimary), PorterDuff.Mode.SRC_IN);
        binding.recView.setLayoutManager(new LinearLayoutManager(this));
        serviceCenterAdapter = new ServiceCenterAdapter(this);
        getSerciceCenerData();


    }



    // get Service Centers by service id
    private void getSerciceCenerData(){

        Api.getService(base_url).getServiceCenterData(serviceId).enqueue(new Callback<ServiceCenterModel>() {
            @Override
            public void onResponse(Call<ServiceCenterModel> call, Response<ServiceCenterModel> response) {
                Log.d(TAG,"centers size:"+response.body().getMarkets());
                binding.progBar.setVisibility(View.GONE);
                binding.setServiceCenter(response.body());
                serviceCenterAdapter.setList(response.body().getMarkets());
                binding.recView.setAdapter(serviceCenterAdapter);
            }

            @Override
            public void onFailure(Call<ServiceCenterModel> call, Throwable t) {
                binding.progBar.setVisibility(View.GONE);
                Log.d(TAG,t.getMessage());
            }
        });

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
