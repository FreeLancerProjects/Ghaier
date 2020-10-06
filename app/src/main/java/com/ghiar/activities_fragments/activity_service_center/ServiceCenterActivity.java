package com.ghiar.activities_fragments.activity_service_center;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.PorterDuff;
import android.location.Location;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ghiar.R;
import com.ghiar.activities_fragments.activity_home.HomeActivity;
import com.ghiar.activities_fragments.activity_service_center_detials.ServiceCenterDetialsActivity;
import com.ghiar.adapters.CityAdapter;
import com.ghiar.adapters.MarksAdapter;
import com.ghiar.adapters.ServiceCenterAdapter;
import com.ghiar.databinding.ActivityNotificationBinding;
import com.ghiar.databinding.ActivityServicesCenterBinding;
import com.ghiar.interfaces.Listeners;
import com.ghiar.language.Language;
import com.ghiar.models.CityDataModel;
import com.ghiar.models.MarkModel;
import com.ghiar.models.MarksModel;
import com.ghiar.models.ServiceCenterModel;
import com.ghiar.models.ServiceCentersModel;
import com.ghiar.models.UserModel;
import com.ghiar.preferences.Preferences;
import com.ghiar.remote.Api;
import com.ghiar.share.Common;
import com.ghiar.tags.Tags;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import io.paperdb.Paper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.ghiar.tags.Tags.base_url;

public class ServiceCenterActivity extends AppCompatActivity implements Listeners.BackListener, GoogleApiClient.OnConnectionFailedListener, GoogleApiClient.ConnectionCallbacks, LocationListener {

    private static final String TAG = "ServiceCenterActivity";
    private ActivityServicesCenterBinding binding;
    private String lang;
    // private List<NotificationDataModel.NotificationModel> notificationModelList;
    //private NotificationAdapter adapter;
    private Preferences preferences;
    private UserModel userModel;
    private ServiceCenterAdapter serviceCenterAdapter;
    private List<ServiceCenterModel> serviceCenterModelList;
    private String serviceId = "", markid = "";
    private List<MarkModel> markModelList;
    private MarksAdapter marksAdapter;
    private MarkModel markModel;
    private List<CityDataModel.CityModel> cityList;
    private CityAdapter cityAdapter;
    private CityDataModel.CityModel cityModel;
    private String country_id = "";
    private Intent intent;
    private static final int REQUEST_PHONE_CALL = 1;
    private final String gps_perm = Manifest.permission.ACCESS_FINE_LOCATION;
    private final int gps_req = 22;
    public Location location = null;
    private GoogleApiClient googleApiClient;
    private LocationRequest locationRequest;
    private LocationCallback locationCallback;
    private String address;
    private double lat, lng;

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
        CheckPermission();

        getMarks();
        getCities();
        getSerciceCenerData();
    }

    private void getMarks() {
        ProgressDialog dialog = Common.createProgressDialog(this, getString(R.string.wait));
        dialog.show();

        Api.getService(Tags.base_url)
                .getMarks()
                .enqueue(new Callback<MarksModel>() {
                    @Override
                    public void onResponse(Call<MarksModel> call, Response<MarksModel> response) {
                        dialog.dismiss();
                        if (response.isSuccessful() && response.body() != null) {
                            if (response.body().getMarks().size() > 0) {
                                markModelList.clear();
                                markModelList.add(markModel);
                                markModelList.addAll(response.body().getMarks());
                                Log.e("data", markModelList.size() + "__");
                                runOnUiThread(() -> {
                                    marksAdapter.notifyDataSetChanged();
                                });
                            }
                        } else {
                            try {

                                Log.e("error", response.code() + "_" + response.errorBody().string());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }

                            if (response.code() == 500) {
                                Toast.makeText(ServiceCenterActivity.this, "Server Error", Toast.LENGTH_SHORT).show();


                            } else {
                                Toast.makeText(ServiceCenterActivity.this, getString(R.string.failed), Toast.LENGTH_SHORT).show();


                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<MarksModel> call, Throwable t) {
                        try {
                            dialog.dismiss();
                            if (t.getMessage() != null) {
                                Log.e("error", t.getMessage());
                                if (t.getMessage().toLowerCase().contains("failed to connect") || t.getMessage().toLowerCase().contains("unable to resolve host")) {
                                    Toast.makeText(ServiceCenterActivity.this, R.string.something, Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(ServiceCenterActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }

                        } catch (Exception e) {
                        }
                    }
                });
    }


    private void initView() {
        serviceCenterModelList = new ArrayList<>();
        markModelList = new ArrayList<>();
        cityList = new ArrayList<>();
        Paper.init(this);
        serviceId = getIntent().getIntExtra("serviceId", 1) + "";
        lang = Paper.book().read("lang", Locale.getDefault().getLanguage());
        binding.setBackListener(this);
        binding.setLang(lang);
        binding.progBar.getIndeterminateDrawable().setColorFilter(ContextCompat.getColor(this, R.color.colorPrimary), PorterDuff.Mode.SRC_IN);
        marksAdapter = new MarksAdapter(markModelList, this);
        binding.spinnerMarks.setAdapter(marksAdapter);
        markModel = new MarkModel();
        markModel.setId(0);
        markModel.setTitle_ar(getString(R.string.choose));
        markModelList.add(markModel);
        cityModel = new CityDataModel.CityModel();
        cityModel.setId_city("0");
        cityModel.setAr_city_title(getString(R.string.choose));
        cityList.add(cityModel);
        cityAdapter = new CityAdapter(cityList, this);
        binding.spinnerCity.setAdapter(cityAdapter);
        preferences = Preferences.getInstance();
        userModel = preferences.getUserData(this);
        Paper.init(this);
        lang = Paper.book().read("lang", Locale.getDefault().getLanguage());
        binding.setBackListener(this);
        binding.setLang(lang);
        binding.progBar.getIndeterminateDrawable().setColorFilter(ContextCompat.getColor(this, R.color.colorPrimary), PorterDuff.Mode.SRC_IN);
        binding.recView.setLayoutManager(new LinearLayoutManager(this));
        serviceCenterAdapter = new ServiceCenterAdapter(this, serviceCenterModelList);
        binding.recView.setAdapter(serviceCenterAdapter);
        getSerciceCenerData();
        binding.spinnerMarks.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    markModel.setId(0);
                } else {
                    markid = markModelList.get(position).getId() + "";
                    markModel.setId(markModelList.get(position).getId());
                    getSerciceCenerData();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        binding.spinnerCity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                } else {
                    country_id = cityList.get(position).getId_city() + "";
                    getSerciceCenerData();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void getCities() {
        ProgressDialog dialog = Common.createProgressDialog(this, getString(R.string.wait));
        dialog.show();

        Api.getService(Tags.base_url)
                .getCity()
                .enqueue(new Callback<CityDataModel>() {
                    @Override
                    public void onResponse(Call<CityDataModel> call, Response<CityDataModel> response) {
                        dialog.dismiss();
                        if (response.isSuccessful() && response.body() != null) {
                            if (response.body().getCity().size() > 0) {
                                cityList.clear();
                                cityList.add(cityModel);
                                cityList.addAll(response.body().getCity());
                                Log.e("data", cityList.size() + "__");
                                runOnUiThread(() -> {
                                    cityAdapter.notifyDataSetChanged();
                                });
                            }
                        } else {
                            try {

                                Log.e("error", response.code() + "_" + response.errorBody().string());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }

                            if (response.code() == 500) {
                                Toast.makeText(ServiceCenterActivity.this, "Server Error", Toast.LENGTH_SHORT).show();


                            } else {
                                Toast.makeText(ServiceCenterActivity.this, getString(R.string.failed), Toast.LENGTH_SHORT).show();


                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<CityDataModel> call, Throwable t) {
                        try {
                            dialog.dismiss();
                            if (t.getMessage() != null) {
                                Log.e("error", t.getMessage());
                                if (t.getMessage().toLowerCase().contains("failed to connect") || t.getMessage().toLowerCase().contains("unable to resolve host")) {
                                    Toast.makeText(ServiceCenterActivity.this, R.string.something, Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(ServiceCenterActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }

                        } catch (Exception e) {
                        }
                    }
                });
    }


    // get Service Centers by service id
    private void getSerciceCenerData() {

        serviceCenterModelList.clear();
        serviceCenterAdapter.notifyDataSetChanged();
        binding.progBar.setVisibility(View.VISIBLE);

        try {


            Api.getService(Tags.base_url)
                    .getServiceCenterData(markid + "", country_id, "", serviceId + "",lat,lng)
                    .enqueue(new Callback<ServiceCentersModel>() {
                        @Override
                        public void onResponse(Call<ServiceCentersModel> call, Response<ServiceCentersModel> response) {
                            binding.progBar.setVisibility(View.GONE);
                            Log.e("lkllkkk", response.code() + "" + serviceId + country_id + markid);
                            if (response.isSuccessful() && response.body() != null && response.body().getMarkets() != null) {
                                serviceCenterModelList.clear();
                                serviceCenterModelList.addAll(response.body().getMarkets());
                                if (response.body().getMarkets().size() > 0) {
                                    // rec_sent.setVisibility(View.VISIBLE);
                                    //  Log.e("data",response.body().getData().get(0).getAr_title());

                                    binding.llNoNotification.setVisibility(View.GONE);
                                    serviceCenterAdapter.notifyDataSetChanged();
                                    //   total_page = response.body().getMeta().getLast_page();

                                } else {
                                    serviceCenterAdapter.notifyDataSetChanged();

                                    binding.llNoNotification.setVisibility(View.VISIBLE);

                                }
                            } else {
                                serviceCenterAdapter.notifyDataSetChanged();

                                binding.llNoNotification.setVisibility(View.VISIBLE);

                                //Toast.makeText(activity, getString(R.string.failed), Toast.LENGTH_SHORT).show();
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
                                binding.progBar.setVisibility(View.GONE);
                                binding.llNoNotification.setVisibility(View.VISIBLE);


                            } catch (Exception e) {
                            }
                        }
                    });
        } catch (Exception e) {
            binding.progBar.setVisibility(View.GONE);
            binding.llNoNotification.setVisibility(View.VISIBLE);

        }
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
            case gps_req:{
            if ( grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                initGoogleApiClient();
            }
        }}
    }

    private void CheckPermission() {
        if (ActivityCompat.checkSelfPermission(this, gps_perm) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{gps_perm}, gps_req);
        } else {

            initGoogleApiClient();
           /* if (isGpsOpen())
            {
                StartService(LocationRequest.PRIORITY_HIGH_ACCURACY);
            }else
                {
                    CreateGpsDialog();

                }*/
        }
    }

    private void initGoogleApiClient() {
        googleApiClient = new GoogleApiClient.Builder(this)
                .addOnConnectionFailedListener(this)
                .addConnectionCallbacks(this)
                .addApi(LocationServices.API)
                .build();
        googleApiClient.connect();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1255) {
            if (resultCode == RESULT_OK) {
                startLocationUpdate();
            } else {
                //create dialog to open_gps
            }
        }


        /*if (requestCode == 33) {
            if (isGpsOpen()) {
                StartService(LocationRequest.PRIORITY_LOW_POWER);
            } else {
                CreateGpsDialog();
            }
        }*/
    }



    private void intLocationRequest() {
        locationRequest = new LocationRequest();
        locationRequest.setFastestInterval(1000 * 60 * 2);
        locationRequest.setInterval(1000 * 60 * 2);
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(locationRequest);
        PendingResult<LocationSettingsResult> result = LocationServices.SettingsApi.checkLocationSettings(googleApiClient, builder.build());

        result.setResultCallback(new ResultCallback<LocationSettingsResult>() {
            @Override
            public void onResult(@NonNull LocationSettingsResult result) {

                Status status = result.getStatus();
                switch (status.getStatusCode()) {
                    case LocationSettingsStatusCodes.SUCCESS:
                        startLocationUpdate();
                        break;
                    case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                        try {
                            status.startResolutionForResult(ServiceCenterActivity.this, 1255);
                        } catch (Exception e) {
                        }
                        break;
                    case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                        Log.e("not available", "not available");
                        break;
                }
            }
        });

    }

    @SuppressLint("MissingPermission")
    private void startLocationUpdate() {
        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                onLocationChanged(locationResult.getLastLocation());
            }
        };
        LocationServices.getFusedLocationProviderClient(this)
                .requestLocationUpdates(locationRequest, locationCallback, Looper.myLooper());
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        intLocationRequest();

    }

    @Override
    public void onConnectionSuspended(int i) {
        if (googleApiClient != null) {
            googleApiClient.connect();
        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
    }

    @Override
    public void onLocationChanged(Location location) {
        LocationListener(location);


    }

    private void LocationListener(final Location location) {

        if (location != null) {
            lat = location.getLatitude();
            lng = location.getLongitude();
            getSerciceCenerData();
        }
    }

    public void show(int id) {
        Intent intent=new Intent(ServiceCenterActivity.this, ServiceCenterDetialsActivity.class);
        intent.putExtra("search",id+"");
        startActivity(intent);
    }
}
