package com.ghiar.activities_fragments.activity_signup;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.ghiar.R;
import com.ghiar.databinding.ActivitySignupAsCustomerBinding;
import com.ghiar.interfaces.Listeners;
import com.ghiar.language.Language;
import com.ghiar.models.SignUpModel;
import com.ghiar.preferences.Preferences;
import com.ghiar.share.Common;

import java.util.Locale;

import io.paperdb.Paper;

public class SignUpActivity extends AppCompatActivity implements Listeners.SignUpListener {
    private ActivitySignupAsCustomerBinding binding;

    private SignUpModel signUpModel;
    private Preferences preferences;
    private String phone;
    private String phone_code;
    @Override
    protected void attachBaseContext(Context newBase) {
        Paper.init(newBase);
        super.attachBaseContext(Language.updateResources(newBase, Locale.getDefault().getLanguage()));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_signup_as_customer);
        getDataFromIntent();
        initView();

    }

    private void initView() {
        preferences = Preferences.getInstance();
        signUpModel = new SignUpModel();
        binding.setSignUpListener(this);

  //      binding.setModel(signUpModel);
//
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

//        Intent intent = new Intent(this, HomeActivity.class);
//        startActivity(intent);

        if (signUpModel.isDataValid(this)) {
           // Common.CloseKeyBoard(this, binding.edtName);
            signUp();
        }

    }







    private void signUp() {



            signUpWithoutImage();

    }

    private void signUpWithoutImage() {
//        ProgressDialog dialog = Common.createProgressDialog(this,getString(R.string.wait));
//        dialog.setCancelable(false);
//        dialog.show();
//        Api.getService(Tags.base_url)
//                .signUpWithoutImage(signUpModel.getName(),signUpModel.getPhone_code(),signUpModel.getPhone(),signUpModel.getEmail())
//                .enqueue(new Callback<UserModel>() {
//                    @Override
//                    public void onResponse(Call<UserModel> call, Response<UserModel> response) {
//                        dialog.dismiss();
//                        if (response.isSuccessful()&&response.body()!=null)
//                        {
//                            preferences.create_update_userdata(SignUpActivity.this,response.body());
//                            navigateToHomeActivity();
//                        }else
//                        {
//                            Log.e("nnnnnnnnnnnn",response.code()+"");
//                            Log.e("555555",response.message());
//                            if (response.code()==500)
//                            {
//                                Toast.makeText(SignUpActivity.this, "Server Error", Toast.LENGTH_SHORT).show();
//                            }else if (response.code()==422)
//                            {
//                                Log.e("2222222",response.errorBody()+"");
//
//                                Toast.makeText(SignUpActivity.this, response.errorBody()+"", Toast.LENGTH_SHORT).show();
//                            }else if (response.code()==409)
//                            {
//
//                                Log.e("99999999",response.message()+"");
//
//                                Toast.makeText(SignUpActivity.this, response.errorBody()+"", Toast.LENGTH_SHORT).show();
//                            }else if (response.code()==406)
//                            {
//
//                                Log.e("6666666",response.message()+"");
//
//                                Toast.makeText(SignUpActivity.this, response.errorBody()+"", Toast.LENGTH_SHORT).show();
//                            }
//                            else
//                            {
//                                Toast.makeText(SignUpActivity.this,getString(R.string.failed), Toast.LENGTH_SHORT).show();
//                            }
//
//                            try {
//                                Log.e("error",response.errorBody().string());
//                            } catch (IOException e) {
//                                e.printStackTrace();
//                            }
//                        }
//                    }
//
//                    @Override
//                    public void onFailure(Call<UserModel> call, Throwable t) {
//                        try {
//                            dialog.dismiss();
//                            if (t.getMessage() != null) {
//                                Log.e("msg_category_error", t.getMessage() + "__");
//
//                                if (t.getMessage().toLowerCase().contains("failed to connect") || t.getMessage().toLowerCase().contains("unable to resolve host")) {
//                                    Toast.makeText(SignUpActivity.this, getString(R.string.something), Toast.LENGTH_SHORT).show();
//                                } else {
//                                    Toast.makeText(SignUpActivity.this, getString(R.string.failed), Toast.LENGTH_SHORT).show();
//                                }
//                            }
//                        }catch (Exception e)
//                        {
//                            Log.e("Error",e.getMessage()+"__");
//                        }
//                    }
//                });
    }


    private void navigateToHomeActivity() {
//        Intent intent = new Intent(this, HomeActivity.class);
//        startActivity(intent);
//        finish();
    }
}
