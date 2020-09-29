package com.ghiar.activities_fragments.activity_signup;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.ghiar.R;
import com.ghiar.activities_fragments.activity_about_app.AboutAppActivity;
import com.ghiar.activities_fragments.activity_home.HomeActivity;
import com.ghiar.adapters.CityAdapter;
import com.ghiar.databinding.ActivitySignupAsCustomerBinding;
import com.ghiar.interfaces.Listeners;
import com.ghiar.language.Language;
import com.ghiar.models.CityDataModel;
import com.ghiar.models.SignUpModel;
import com.ghiar.models.UserModel;
import com.ghiar.preferences.Preferences;
import com.ghiar.remote.Api;
import com.ghiar.share.Common;

import com.ghiar.tags.Tags;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import io.paperdb.Paper;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignUpActivity extends AppCompatActivity implements Listeners.SignUpListener {
    private ActivitySignupAsCustomerBinding binding;
    private final String READ_PERM = Manifest.permission.READ_EXTERNAL_STORAGE;
    private final String write_permission = Manifest.permission.WRITE_EXTERNAL_STORAGE;
    private final String camera_permission = Manifest.permission.CAMERA;
    private final int READ_REQ = 1, CAMERA_REQ = 2;
    private Uri uri = null;
    private SignUpModel signUpModel;
    private CityDataModel.CityModel cityModel;
    private Preferences preferences;
    private String phone;
    private String phone_code;
    private List<CityDataModel.CityModel> cityList;
    private CityAdapter cityAdapter;

    @Override
    protected void attachBaseContext(Context newBase) {
        Paper.init(newBase);
        super.attachBaseContext(Language.updateResources(newBase, Paper.book().read("lang", "ar")));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_signup_as_customer);
        getDataFromIntent();
        initView();

    }

    private void initView() {
        cityList = new ArrayList<>();
        preferences = Preferences.getInstance();
        signUpModel = new SignUpModel();
        binding.setSignUpListener(this);
        signUpModel.setPhone_code(phone_code);
        signUpModel.setPhone(phone);
        binding.setModel(signUpModel);

        cityAdapter = new CityAdapter(cityList,this);
        binding.spinnercity.setAdapter(cityAdapter);

        cityModel = new CityDataModel.CityModel();
        cityModel.setId_city("0");
        cityModel.setAr_city_title(getString(R.string.choose));
        cityList.add(cityModel);


        binding.checkbox.setOnClickListener(v -> {
            if (binding.checkbox.isChecked())
            {
                signUpModel.setTermsAccepted(true);
                navigateToAboutAppActivity();
            }else {
                signUpModel.setTermsAccepted(false);
            }

            binding.setModel(signUpModel);

        });

        binding.spinnercity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position==0)
                {
                    signUpModel.setCity_id("");
                }else
                {
                    signUpModel.setCity_id(cityList.get(position).getId_city());

                }
                binding.setModel(signUpModel);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        getCities();
    }

    private void getCities() {
        ProgressDialog dialog = Common.createProgressDialog(this,getString(R.string.wait));
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
                                Log.e("data",cityList.size()+"__");
                                SignUpActivity.this.runOnUiThread(() -> {
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
                                Toast.makeText(SignUpActivity.this, "Server Error", Toast.LENGTH_SHORT).show();


                            } else {
                                Toast.makeText(SignUpActivity.this, getString(R.string.failed), Toast.LENGTH_SHORT).show();


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
                                    Toast.makeText(SignUpActivity.this, R.string.something, Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(SignUpActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }

                        } catch (Exception e) {
                        }
                    }
                });
    }

    private void navigateToAboutAppActivity() {

        Intent intent = new Intent(this, AboutAppActivity.class);
        intent.putExtra("type",1);
        startActivity(intent);
    }

    private void getDataFromIntent() {
        Intent intent = getIntent();
        if (intent != null) {
            phone_code = intent.getStringExtra("phone_code");
            phone = intent.getStringExtra("phone");

        }
    }


    @Override
    public void checkDataValid() {

        if (signUpModel.isDataValid(this)) {
            Common.CloseKeyBoard(this, binding.edtName);
            signUp();
        }

    }





    private void signUp() {


        if (uri==null) {
            signUpWithoutImage();
        }
    }

    private void signUpWithoutImage() {
        ProgressDialog dialog = Common.createProgressDialog(this,getString(R.string.wait));
        dialog.setCancelable(false);
        dialog.show();
        Api.getService(Tags.base_url)
                .signUp(signUpModel.getName(),signUpModel.getEmail(),signUpModel.getPhone_code(),signUpModel.getPhone(),signUpModel.getCity_id())
                .enqueue(new Callback<UserModel>() {
                    @Override
                    public void onResponse(Call<UserModel> call, Response<UserModel> response) {
                        dialog.dismiss();
                        if (response.isSuccessful()&&response.body()!=null)
                        {
                            preferences.create_update_userdata(SignUpActivity.this,response.body());
                            navigateToHomeActivity();
                        }else
                        {

                            if (response.code()==500)
                            {
                                Toast.makeText(SignUpActivity.this, "Server Error", Toast.LENGTH_SHORT).show();
                            }else if (response.code()==422)
                            {

                                Toast.makeText(SignUpActivity.this, response.errorBody()+"", Toast.LENGTH_SHORT).show();
                            }else if (response.code()==409)
                            {

                                Log.e("99999999",response.message()+"");

                                Toast.makeText(SignUpActivity.this, response.errorBody()+"", Toast.LENGTH_SHORT).show();
                            }else if (response.code()==406)
                            {

                                Log.e("6666666",response.message()+"");

                                Toast.makeText(SignUpActivity.this, response.errorBody()+"", Toast.LENGTH_SHORT).show();
                            }
                            else
                            {
                                Toast.makeText(SignUpActivity.this,getString(R.string.failed), Toast.LENGTH_SHORT).show();
                            }

                            try {
                                Log.e("error",response.errorBody().string());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<UserModel> call, Throwable t) {
                        try {
                            dialog.dismiss();
                            if (t.getMessage() != null) {
                                Log.e("msg_category_error", t.getMessage() + "__");

                                if (t.getMessage().toLowerCase().contains("failed to connect") || t.getMessage().toLowerCase().contains("unable to resolve host")) {
                                    Toast.makeText(SignUpActivity.this, getString(R.string.something), Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(SignUpActivity.this, getString(R.string.failed), Toast.LENGTH_SHORT).show();
                                }
                            }
                        }catch (Exception e)
                        {
                            Log.e("Error",e.getMessage()+"__");
                        }
                    }
                });
    }


    private void navigateToHomeActivity() {
        Intent intent = new Intent(this, HomeActivity.class);
        startActivity(intent);
        finish();
    }
}
