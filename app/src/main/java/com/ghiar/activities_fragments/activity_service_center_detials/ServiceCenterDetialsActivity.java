package com.ghiar.activities_fragments.activity_service_center_detials;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ghiar.R;
import com.ghiar.activities_fragments.activity_service_center.ServiceCenterActivity;
import com.ghiar.activities_fragments.activtity_auction_detials.AuctionDetialsActivity;
import com.ghiar.adapters.AllCenterAdapter;
import com.ghiar.adapters.ProductSlideAdapter;
import com.ghiar.adapters.SameServiceCenterAdapter;
import com.ghiar.databinding.ActivityServicesCenterBinding;
import com.ghiar.databinding.ActivityServicesCenterDetialsBinding;
import com.ghiar.interfaces.Listeners;
import com.ghiar.language.Language;
import com.ghiar.models.MarksDataModel;
import com.ghiar.models.ServiceCenterModel;
import com.ghiar.models.ServiceCentersModel;
import com.ghiar.models.SingleAuctionModel;
import com.ghiar.models.UserModel;
import com.ghiar.preferences.Preferences;
import com.ghiar.remote.Api;
import com.ghiar.share.Common;
import com.ghiar.tags.Tags;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import io.paperdb.Paper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ServiceCenterDetialsActivity extends AppCompatActivity implements Listeners.BackListener, OnMapReadyCallback {
    private ActivityServicesCenterDetialsBinding binding;
    private String lang;
    // private List<NotificationDataModel.NotificationModel> notificationModelList;
    //private NotificationAdapter adapter;
    private Preferences preferences;
    private UserModel userModel;
    private String search_id;
    private ServiceCentersModel singleadversimet;
    private double lat = 0.0, lng = 0.0;
    private GoogleMap mMap;
    private Marker marker;
    private float zoom = 15.0f;
    private Intent intent;
    private static final int REQUEST_PHONE_CALL = 1;
    private List<ServiceCentersModel.All> allList;
    private List<ServiceCentersModel.Like> likeList;
    private SameServiceCenterAdapter sameServiceCenterAdapter;
    private AllCenterAdapter allCenterAdapter;

    @Override
    protected void attachBaseContext(Context newBase) {
        Paper.init(newBase);
        super.attachBaseContext(Language.updateResources(newBase, (Locale.getDefault().getLanguage())));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_services_center_detials);
        initView();
        getdatafromintent();
        getsingleservicecenter();
        updateUI();

    }

    private void updateUI() {

        SupportMapFragment fragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        fragment.getMapAsync(this);


    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        if (googleMap != null) {
            mMap = googleMap;
            mMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(this, R.raw.maps));
            mMap.setTrafficEnabled(false);
            mMap.setBuildingsEnabled(false);
            mMap.setIndoorEnabled(true);
            AddMarker(lat, lng);


        }
    }


    private void AddMarker(double lat, double lng) {

        this.lat = lat;
        this.lng = lng;

        if (marker == null) {
            marker = mMap.addMarker(new MarkerOptions().position(new LatLng(lat, lng)).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(lat, lng), zoom));
        } else {
            marker.setPosition(new LatLng(lat, lng));
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(lat, lng), zoom));


        }
    }

    private void getdatafromintent() {
        Intent intent = getIntent();
        if (intent.getStringExtra("search") != null) {
            search_id = intent.getStringExtra("search");
        }
    }


    private void initView() {
        allList = new ArrayList<>();
        likeList = new ArrayList<>();
        Paper.init(this);
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
        sameServiceCenterAdapter = new SameServiceCenterAdapter(this, likeList);
        allCenterAdapter = new AllCenterAdapter(this, allList);
        binding.progBar.getIndeterminateDrawable().setColorFilter(ContextCompat.getColor(this, R.color.colorPrimary), PorterDuff.Mode.SRC_IN);
        binding.recView.setLayoutManager(new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false));
        binding.recViewall.setLayoutManager(new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false));
        binding.recViewall.setAdapter(allCenterAdapter);
        binding.recView.setAdapter(sameServiceCenterAdapter);
        binding.btnCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (singleadversimet != null) {
                    call(singleadversimet.getMarket().getPhone_code() + singleadversimet.getMarket().getPhone());
                }
            }
        });
        binding.llshare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                share(Integer.parseInt(search_id));
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

    public void getsingleservicecenter() {
        //   Common.CloseKeyBoard(homeActivity, edt_name);

        ProgressDialog dialog = Common.createProgressDialog(ServiceCenterDetialsActivity.this, getString(R.string.wait));
        dialog.setCancelable(false);
        dialog.show();
        // rec_sent.setVisibility(View.GONE);
        try {


            Api.getService(Tags.base_url)
                    .get_singleservicecenter(search_id)
                    .enqueue(new Callback<ServiceCentersModel>() {
                        @Override
                        public void onResponse(Call<ServiceCentersModel> call, Response<ServiceCentersModel> response) {
                            dialog.dismiss();
                            Log.e("Error_code", response.code() + "");

                            //  binding.progBar.setVisibility(View.GONE);
                            if (response.isSuccessful() && response.body() != null) {
                                //binding.coord1.scrollTo(0,0);
                                setdata(response.body());

                            } else {
                                Log.e("Error_code", response.code() + "");

                                //   Toast.makeText(AuctionDetialsActivity.this, getString(R.string.failed), Toast.LENGTH_SHORT).show();
                                try {
                                    Log.e("Error_code", response.code() + "_" + response.errorBody().string());
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<ServiceCentersModel> call, Throwable t) {
                            try {

                                dialog.dismiss();

                                // Toast.makeText(AdsDetialsActivity.this, getString(R.string.something), Toast.LENGTH_SHORT).show();
                                Log.e("errorsssssss", t.getMessage());
                            } catch (Exception e) {
                            }
                        }
                    });
        } catch (Exception e) {
            Log.e("lllll", e.toString());
            dialog.dismiss();
        }
    }

    private void setdata(ServiceCentersModel body) {
        this.singleadversimet = body;
        binding.setModel(body.getMarket());
        lat = Double.parseDouble(body.getMarket().getGoogle_lat());
        lng = Double.parseDouble(body.getMarket().getGoogle_long());
        AddMarker(lat, lng);
        String service = "";
        for (int i = 0; i < body.getMarket().getServices().size(); i++) {
            if (lang.equals("ar")) {
                service += body.getMarket().getServices().get(i).getTitle_ar();
            } else {
                service += body.getMarket().getServices().get(i).getTitle_en();
            }
        }
        binding.tvservice.setText(service);
        //   Log.e("ddldlld", singleadversimet.getAll().size() + "");
        if (singleadversimet.getAll() != null) {
            allList.addAll(singleadversimet.getAll());
            allCenterAdapter.notifyDataSetChanged();
        } else {
            binding.tv1.setVisibility(View.GONE);
        }
        if (singleadversimet.getLike() != null) {
            likeList.addAll(singleadversimet.getLike());
            sameServiceCenterAdapter.notifyDataSetChanged();
        } else {
            binding.tv2.setVisibility(View.GONE);
        }
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

    public void show(int id) {
        Intent intent = new Intent(ServiceCenterDetialsActivity.this, ServiceCenterDetialsActivity.class);
        intent.putExtra("search", id + "");
        startActivity(intent);
    }

    public void share(int id) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TEXT, Tags.base_url + "api/RedirectLink/market/" + id);
        startActivity(intent);
    }
}
