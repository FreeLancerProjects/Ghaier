package com.ghiar.activities_fragments.activity_cart;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.ghiar.R;
import com.ghiar.activities_fragments.activity_cart.fragments.Fragment_Address;
import com.ghiar.activities_fragments.activity_cart.fragments.Fragment_Cart_Purchases;
import com.ghiar.activities_fragments.activity_cart.fragments.Fragment_Payment_Type;
import com.ghiar.databinding.ActivityAboutAppBinding;
import com.ghiar.databinding.ActivityCartBinding;
import com.ghiar.interfaces.Listeners;
import com.ghiar.language.Language;
import com.ghiar.models.AddOrderModel;
import com.ghiar.models.Create_Order_Model;
import com.ghiar.models.UserModel;
import com.ghiar.preferences.Preferences;
import com.ghiar.remote.Api;
import com.ghiar.share.Common;
import com.ghiar.tags.Tags;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import io.paperdb.Paper;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CartActivity extends AppCompatActivity implements Listeners.BackListener {
    private ActivityCartBinding binding;
    private String lang;
    private FragmentManager fragmentManager;
    private Fragment_Address fragment_address;
    private Fragment_Cart_Purchases fragment_cart_purchases;
    private Fragment_Payment_Type fragment_payment_type;
    private AddOrderModel addOrderModel;
    private Preferences preferences;
    private Create_Order_Model create_order_model;
    private UserModel usermodel;

    @Override
    protected void attachBaseContext(Context newBase) {
        Paper.init(newBase);
        super.attachBaseContext(Language.updateResources(newBase, Paper.book().read("lang", "ar")));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_cart);
        initView();
        if (savedInstanceState == null) {
            displayFragmentPurchases();
        }
    }


    private void initView() {
        fragmentManager = getSupportFragmentManager();
        preferences = Preferences.getInstance();
        usermodel = preferences.getUserData(this);
        create_order_model = preferences.getUserOrder(this);
        Paper.init(this);
        lang = Paper.book().read("lang", "ar");
        binding.setBackListener(this);
        binding.setLang(lang);


        binding.flNext.setOnClickListener(v -> {
            if (fragment_cart_purchases != null && fragment_cart_purchases.isAdded() && fragment_cart_purchases.isVisible()) {

                displayFragmentAddress();
            } else if (fragment_address != null && fragment_address.isAdded() && fragment_address.isVisible()) {

                if (fragment_address.isDataValid(this)) {
                    displayFragmentPaymentType();
                }

            } else if (fragment_payment_type != null && fragment_payment_type.isAdded() && fragment_payment_type.isVisible()) {

                //createOrder
                if (usermodel != null) {
                    create_order_model.setAddress(addOrderModel.getAddress());
                    create_order_model.setFull_name(addOrderModel.getName());
                    create_order_model.setUser_id(usermodel.getUser().getId() + "");
                    create_order_model.setPayment_method(addOrderModel.getPayment_type());
                    create_order_model.setPhone(addOrderModel.getPhone_code() + addOrderModel.getPhone());
                    create_order_model.setGoogle_lat(addOrderModel.getLat() + "");
                    create_order_model.setGoogle_long(addOrderModel.getLng() + "");
                    accept_order();
                } else {
                    Common.CreateDialogAlert(this, getResources().getString(R.string.please_sign_in_or_sign_up));
                }
            }
        });

        binding.flBack.setOnClickListener(v -> {
            if (fragment_cart_purchases != null && fragment_cart_purchases.isAdded() && fragment_cart_purchases.isVisible()) {
                back();
            } else if (fragment_address != null && fragment_address.isAdded() && fragment_address.isVisible()) {
                displayFragmentPurchases();
            } else if (fragment_payment_type != null && fragment_payment_type.isAdded() && fragment_payment_type.isVisible()) {
                displayFragmentAddress();
            }
        });

        getdata();
    }

    private void getdata() {
        if (create_order_model == null) {
            binding.flBack.setVisibility(View.GONE);
            binding.flNext.setVisibility(View.GONE);
            binding.ll.setVisibility(View.GONE);
            binding.llAction.setVisibility(View.GONE);
            Log.e("dlldldldl", "dlldldl");
        } else {
            binding.flBack.setVisibility(View.VISIBLE);
            binding.flNext.setVisibility(View.VISIBLE);
            binding.ll.setVisibility(View.VISIBLE);
            binding.llAction.setVisibility(View.VISIBLE);
        }
        finish();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (preferences != null && preferences.getUserOrder(this) == null) {
            binding.flBack.setVisibility(View.GONE);
            binding.flNext.setVisibility(View.GONE);
            binding.ll.setVisibility(View.GONE);
            binding.llAction.setVisibility(View.GONE);
        }
    }

    public void displayFragmentPurchases() {
        try {
            if (fragment_cart_purchases == null) {
                fragment_cart_purchases = Fragment_Cart_Purchases.newInstance(addOrderModel);
            } else {
                fragment_cart_purchases.setModel(addOrderModel);
            }

            if (fragment_address != null && fragment_address.isAdded()) {
                fragmentManager.beginTransaction().hide(fragment_address).commit();
            }
            if (fragment_payment_type != null && fragment_payment_type.isAdded()) {
                fragmentManager.beginTransaction().hide(fragment_payment_type).commit();
            }

            if (fragment_cart_purchases.isAdded()) {
                fragmentManager.beginTransaction().show(fragment_cart_purchases).commit();

            } else {
                fragmentManager.beginTransaction().add(R.id.fragment_container, fragment_cart_purchases, "fragment_cart_purchases").addToBackStack("fragment_cart_purchases").commit();

            }

            Log.e("1", "1");
            updateUi1();


        } catch (Exception e) {
        }

    }


    public void displayFragmentAddress() {
        try {
            if (fragment_address == null) {
                fragment_address = Fragment_Address.newInstance(addOrderModel);
            } else {
                fragment_address.setModel(addOrderModel);
            }

            if (fragment_cart_purchases != null && fragment_cart_purchases.isAdded()) {
                fragmentManager.beginTransaction().hide(fragment_cart_purchases).commit();
            }
            if (fragment_payment_type != null && fragment_payment_type.isAdded()) {
                fragmentManager.beginTransaction().hide(fragment_payment_type).commit();
            }

            if (fragment_address.isAdded()) {
                fragmentManager.beginTransaction().show(fragment_address).commit();

            } else {
                fragmentManager.beginTransaction().add(R.id.fragment_container, fragment_address, "fragment_address").addToBackStack("fragment_address").commit();

            }

            updateUi2();

        } catch (Exception e) {
        }

    }

    public void displayFragmentPaymentType() {
        try {
            if (fragment_payment_type == null) {
                fragment_payment_type = Fragment_Payment_Type.newInstance(addOrderModel);
            } else {
                fragment_payment_type.setModel(addOrderModel);
            }

            if (fragment_cart_purchases != null && fragment_cart_purchases.isAdded()) {
                fragmentManager.beginTransaction().hide(fragment_cart_purchases).commit();
            }
            if (fragment_address != null && fragment_address.isAdded()) {
                fragmentManager.beginTransaction().hide(fragment_address).commit();
            }

            if (fragment_payment_type.isAdded()) {
                fragmentManager.beginTransaction().show(fragment_payment_type).commit();

            } else {
                fragmentManager.beginTransaction().add(R.id.fragment_container, fragment_payment_type, "fragment_payment_type").addToBackStack("fragment_payment_type").commit();

            }

            updateUi3();

        } catch (Exception e) {
        }

    }

    private void updateUi1() {
        binding.imagePurchases.setColorFilter(ContextCompat.getColor(this, R.color.colorPrimary));
        binding.tvPurchases.setTextColor(ContextCompat.getColor(this, R.color.colorPrimary));
        binding.imageArrow1.setColorFilter(ContextCompat.getColor(this, R.color.colorPrimary));

        binding.imageAddress.setColorFilter(ContextCompat.getColor(this, R.color.gray6));
        binding.tvAddress.setTextColor(ContextCompat.getColor(this, R.color.gray6));
        binding.imageArrow2.setColorFilter(ContextCompat.getColor(this, R.color.gray6));


        binding.imagePayment.setColorFilter(ContextCompat.getColor(this, R.color.gray6));
        binding.tvPayment.setTextColor(ContextCompat.getColor(this, R.color.gray6));


    }

    private void updateUi2() {

        binding.imagePurchases.setColorFilter(ContextCompat.getColor(this, R.color.colorPrimary));
        binding.tvPurchases.setTextColor(ContextCompat.getColor(this, R.color.colorPrimary));
        binding.imageArrow1.setColorFilter(ContextCompat.getColor(this, R.color.colorPrimary));

        binding.imageAddress.setColorFilter(ContextCompat.getColor(this, R.color.colorPrimary));
        binding.tvAddress.setTextColor(ContextCompat.getColor(this, R.color.colorPrimary));
        binding.imageArrow2.setColorFilter(ContextCompat.getColor(this, R.color.colorPrimary));


        binding.imagePayment.setColorFilter(ContextCompat.getColor(this, R.color.gray6));
        binding.tvPayment.setTextColor(ContextCompat.getColor(this, R.color.gray6));

    }

    private void updateUi3() {
        binding.imagePurchases.setColorFilter(ContextCompat.getColor(this, R.color.colorPrimary));
        binding.tvPurchases.setTextColor(ContextCompat.getColor(this, R.color.colorPrimary));
        binding.imageArrow1.setColorFilter(ContextCompat.getColor(this, R.color.colorPrimary));

        binding.imageAddress.setColorFilter(ContextCompat.getColor(this, R.color.colorPrimary));
        binding.tvAddress.setTextColor(ContextCompat.getColor(this, R.color.colorPrimary));
        binding.imageArrow2.setColorFilter(ContextCompat.getColor(this, R.color.colorPrimary));


        binding.imagePayment.setColorFilter(ContextCompat.getColor(this, R.color.colorPrimary));
        binding.tvPayment.setTextColor(ContextCompat.getColor(this, R.color.colorPrimary));

    }

    public void updateAddOrderModel(AddOrderModel addOrderModel) {
        this.addOrderModel = addOrderModel;
    }

//    @Override
//    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        List<Fragment> fragmentList = fragmentManager.getFragments();
//        for (Fragment fragment : fragmentList) {
//            fragment.onActivityResult(requestCode, resultCode, data);
//        }
//
//
//    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        List<Fragment> fragmentList = fragmentManager.getFragments();
        for (Fragment fragment : fragmentList) {
            fragment.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }

    }

    @Override
    public void back() {
        finish();
    }

    @Override

    public void onBackPressed() {
        back();
    }

    private void accept_order() {
create_order_model.setTotal_cost(fragment_cart_purchases.total+"");
        final ProgressDialog dialog = Common.createProgressDialog(this, getString(R.string.wait));
        dialog.setCancelable(false);
        dialog.show();
        Api.getService(Tags.base_url).accept_orders(create_order_model).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                dialog.dismiss();
                Log.e("kkmkmkm",response.code()+"");
                if (response.isSuccessful()) {
                    getdata();
// Common.CreateSignAlertDialog(activity, getResources().getString(R.string.sucess));

                    //  activity.refresh(Send_Data.getType());
                } else {
                    Common.CreateDialogAlert(CartActivity.this, getString(R.string.failed));

                    try {
                        Log.e("Error_code", response.code() + "_" + response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                try {
                    dialog.dismiss();
                    Toast.makeText(CartActivity.this, getString(R.string.something), Toast.LENGTH_SHORT).show();
                    Log.e("Error", t.getMessage());
                } catch (Exception e) {
                }
            }
        });
    }

}
