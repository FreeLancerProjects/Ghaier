package com.ghiar.activities_fragments.activity_home;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.annotation.SuppressLint;
import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;

import com.aurelhubert.ahbottomnavigation.AHBottomNavigation;
import com.aurelhubert.ahbottomnavigation.AHBottomNavigationItem;
import com.ghiar.R;
import com.ghiar.activities_fragments.activity_accessories_spare_details.AccessoriesSparePartsDetailsActivity;
import com.ghiar.activities_fragments.activity_cart.CartActivity;
import com.ghiar.activities_fragments.activity_home.fragments.Fragment_Auction;
import com.ghiar.activities_fragments.activity_home.fragments.Fragment_Home;
import com.ghiar.activities_fragments.activity_home.fragments.Fragment_More;
import com.ghiar.activities_fragments.activity_home.fragments.Fragment_Reguired;
import com.ghiar.activities_fragments.activity_home.fragments.fragment_profile.fragments.Fragment_Profile;
import com.ghiar.activities_fragments.activity_login.LoginActivity;
import com.ghiar.activities_fragments.activity_model_details.ModelDetailsActivity;
import com.ghiar.activities_fragments.activity_notification.NotificationActivity;
import com.ghiar.activities_fragments.activity_room.ChatRoomActivity;
import com.ghiar.activities_fragments.chat_activity.ChatActivity;
import com.ghiar.adapters.MarkAdapter;
import com.ghiar.databinding.ActivityHomeBinding;
import com.ghiar.language.Language;
import com.ghiar.models.Create_Order_Model;
import com.ghiar.models.MarkModel;
import com.ghiar.models.MarksModel;
import com.ghiar.models.ProductModel;
import com.ghiar.models.UserModel;
import com.ghiar.preferences.Preferences;
import com.ghiar.remote.Api;
import com.ghiar.share.Common;
import com.ghiar.tags.Tags;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import io.paperdb.Paper;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeActivity extends AppCompatActivity {

    private static final String TAG = "HomeActivity";
    private ActivityHomeBinding binding;
    private FragmentManager fragmentManager;
    private Preferences preferences;
    private UserModel userModel;
    private Fragment_Home fragment_home;
    private Fragment_Auction fragment_auction;
    private Fragment_Reguired fragment_reguired;
    private Fragment_Profile fragment_profile;
    private Fragment_More fragment_more;
    private ActionBarDrawerToggle toggle;
    private MarkAdapter markAdapter;
    private List<MarkModel> markModelList;
    private String lang;
    private Create_Order_Model add_order_model;

    protected void attachBaseContext(Context newBase) {
        Paper.init(newBase);
        super.attachBaseContext(Language.updateResources(newBase, Paper.book().read("lang", Locale.getDefault().getLanguage())));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_home);
//        NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
//
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
//
//        if (manager != null) {
//            manager.cancel(Tags.not_tag, Tags.not_id);
//
//        }
        initView();

        if (savedInstanceState == null) {
            Log.e("gg", "gg");
            displayFragmentHome();

        }

        if (userModel != null) {

            Log.e("bbbbbbbb", userModel.getUser().getName() + "");
            // EventBus.getDefault().register(this);
            updateToken();

        }


    }


    @SuppressLint("RestrictedApi")
    private void initView() {
        markModelList = new ArrayList<>();
        preferences = Preferences.getInstance();
        add_order_model = preferences.getUserOrder(this);
        if (add_order_model == null) {
            binding.setCartCount(0);
        } else {
            binding.setCartCount(add_order_model.getDetails().size());
        }
        userModel = preferences.getUserData(this);
        toggle = new ActionBarDrawerToggle(this, binding.drawer, binding.toolBar, R.string.open, R.string.close);
        // toggle.setDrawerIndicatorEnabled(false);
        toggle.syncState();

        fragmentManager = getSupportFragmentManager();
        lang = Paper.book().read("lang", Locale.getDefault().getLanguage());
// init marks recyclerview
        markAdapter = new MarkAdapter(this, markModelList);
        binding.recViewModel.setLayoutManager(new LinearLayoutManager(this));
        binding.recViewModel.setAdapter(markAdapter);
        getDrawerMarks();
        setUpBottomNavigation();
        binding.flNotification.setOnClickListener(v -> {
            if (userModel == null) {

                Common.CreateDialogAlert2(this, getString(R.string.please_sign_in_or_sign_up));

            } else {
                Intent intent = new Intent(this, NotificationActivity.class);
                startActivity(intent);
            }

        });

        binding.imageCart.setOnClickListener(v -> {
            Intent intent = new Intent(HomeActivity.this, CartActivity.class);
            startActivity(intent);
        });

        binding.imageChat.setOnClickListener(v -> {
           Intent intent = new Intent(this, ChatRoomActivity.class);
           startActivity(intent);
        });

    }


    // get marks data for navigation drawer
    private void getDrawerMarks() {

        markModelList.clear();
        markAdapter.notifyDataSetChanged();
        binding.progBarModel.setVisibility(View.VISIBLE);

        try {


            Api.getService(Tags.base_url)
                    .getMarks()
                    .enqueue(new Callback<MarksModel>() {
                        @Override
                        public void onResponse(Call<MarksModel> call, Response<MarksModel> response) {
                            binding.progBarModel.setVisibility(View.GONE);
                            if (response.isSuccessful() && response.body() != null && response.body().getMarks() != null) {
                                markModelList.clear();
                                markModelList.addAll(response.body().getMarks());
                                if (response.body().getMarks().size() > 0) {
                                    // rec_sent.setVisibility(View.VISIBLE);
                                    //   Log.e("datasssssss",response.body().getMarks().get(0).getTitle_ar());

                                    // binding.flNotification.setVisibility(View.GONE);
                                    markAdapter.notifyDataSetChanged();
                                    //   total_page = response.body().getMeta().getLast_page();

                                } else {
                                    markAdapter.notifyDataSetChanged();

                                    //     binding.llNoNotification.setVisibility(View.VISIBLE);

                                }
                            } else {
                                markAdapter.notifyDataSetChanged();

                                //   binding.llNoNotification.setVisibility(View.VISIBLE);

                                //Toast.makeText(activity, getString(R.string.failed), Toast.LENGTH_SHORT).show();
                                try {
                                    Log.e("Error_code", response.code() + "_" + response.errorBody().string());
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<MarksModel> call, Throwable t) {
                            try {
                                binding.progBarModel.setVisibility(View.GONE);
                                //binding.llNoNotification.setVisibility(View.VISIBLE);


                            } catch (Exception e) {
                            }
                        }
                    });
        } catch (Exception e) {
            binding.progBarModel.setVisibility(View.GONE);
            // binding.ll.setVisibility(View.VISIBLE);

        }
    }


    private void setUpBottomNavigation() {

        AHBottomNavigationItem item1 = new AHBottomNavigationItem(getString(R.string.home), R.drawable.ic_nav_home);
        AHBottomNavigationItem item2 = new AHBottomNavigationItem(getString(R.string.auction), R.drawable.ic_auction);
        AHBottomNavigationItem item3 = new AHBottomNavigationItem(getString(R.string.search), R.drawable.ic_search_cancel);
        AHBottomNavigationItem item4 = new AHBottomNavigationItem(getString(R.string.profile), R.drawable.ic_nav_user);
        AHBottomNavigationItem item5 = new AHBottomNavigationItem(getString(R.string.more), R.drawable.ic_nav_more);

        binding.ahBottomNav.setTitleState(AHBottomNavigation.TitleState.ALWAYS_SHOW);
        binding.ahBottomNav.setDefaultBackgroundColor(ContextCompat.getColor(this, R.color.colorAccent));
        binding.ahBottomNav.setTitleTextSizeInSp(13, 11);
        binding.ahBottomNav.setForceTint(true);
        binding.ahBottomNav.setAccentColor(ContextCompat.getColor(this, R.color.white));
        binding.ahBottomNav.setInactiveColor(ContextCompat.getColor(this, R.color.gray0));

        binding.ahBottomNav.addItem(item1);
        binding.ahBottomNav.addItem(item2);
        binding.ahBottomNav.addItem(item3);
        binding.ahBottomNav.addItem(item4);
        binding.ahBottomNav.addItem(item5);

        updateBottomNavigationPosition(0);

        binding.ahBottomNav.setOnTabSelectedListener((position, wasSelected) -> {

            switch (position) {
                case 0:
                    displayFragmentHome();

                    break;
                case 1:
                    displayFragmentAuction();
                    break;
                case 2:
                    if (userModel == null) {

                        Common.CreateDialogAlert2(this, getString(R.string.please_sign_in_or_sign_up));

                    } else {
                        displayFragmentSearch();
                    }

                    break;
                case 3:
                    if (userModel != null) {
                        displayFragmentProfile();

                    } else {
                        Common.CreateDialogAlert2(this, getString(R.string.please_sign_in_or_sign_up));
                    }
                    break;

                case 4:
                    displayFragmentMore();
                    break;

            }
            return false;
        });


    }

    private void updateBottomNavigationPosition(int pos) {

        binding.ahBottomNav.setCurrentItem(pos, false);
    }

    private void displayFragmentHome() {
        try {
            if (fragment_home == null) {
                fragment_home = Fragment_Home.newInstance();
            }

            if (fragment_auction != null && fragment_auction.isAdded()) {
                fragmentManager.beginTransaction().hide(fragment_auction).commit();
            }
            if (fragment_reguired != null && fragment_reguired.isAdded()) {
                fragmentManager.beginTransaction().hide(fragment_reguired).commit();
            }

            if (fragment_profile != null && fragment_profile.isAdded()) {
                fragmentManager.beginTransaction().hide(fragment_profile).commit();
            }
            if (fragment_more != null && fragment_more.isAdded()) {
                fragmentManager.beginTransaction().hide(fragment_more).commit();
            }
            if (fragment_home.isAdded()) {
                fragmentManager.beginTransaction().show(fragment_home).commit();

            } else {
                fragmentManager.beginTransaction().add(R.id.fragment_app_container, fragment_home, "fragment_home").addToBackStack("fragment_home").commit();

            }
            updateBottomNavigationPosition(0);
        } catch (Exception e) {
        }
    }

    private void displayFragmentAuction() {
        try {
            if (fragment_auction == null) {
                fragment_auction = Fragment_Auction.newInstance();
            }

            if (fragment_home != null && fragment_home.isAdded()) {
                fragmentManager.beginTransaction().hide(fragment_home).commit();
            }
            if (fragment_reguired != null && fragment_reguired.isAdded()) {
                fragmentManager.beginTransaction().hide(fragment_reguired).commit();
            }

            if (fragment_profile != null && fragment_profile.isAdded()) {
                fragmentManager.beginTransaction().hide(fragment_profile).commit();
            }
            if (fragment_more != null && fragment_more.isAdded()) {
                fragmentManager.beginTransaction().hide(fragment_more).commit();
            }
            if (fragment_auction.isAdded()) {
                fragmentManager.beginTransaction().show(fragment_auction).commit();

            } else {
                fragmentManager.beginTransaction().add(R.id.fragment_app_container, fragment_auction, "fragment_auction").addToBackStack("fragment_auction").commit();

            }
            updateBottomNavigationPosition(1);
        } catch (Exception e) {
        }
    }

    private void displayFragmentSearch() {
        try {
            if (fragment_reguired == null) {
                fragment_reguired = Fragment_Reguired.newInstance();
            }

            if (fragment_home != null && fragment_home.isAdded()) {
                fragmentManager.beginTransaction().hide(fragment_home).commit();
            }
            if (fragment_auction != null && fragment_auction.isAdded()) {
                fragmentManager.beginTransaction().hide(fragment_auction).commit();
            }

            if (fragment_profile != null && fragment_profile.isAdded()) {
                fragmentManager.beginTransaction().hide(fragment_profile).commit();
            }
            if (fragment_more != null && fragment_more.isAdded()) {
                fragmentManager.beginTransaction().hide(fragment_more).commit();
            }
            if (fragment_reguired.isAdded()) {
                fragmentManager.beginTransaction().show(fragment_reguired).commit();

            } else {
                fragmentManager.beginTransaction().add(R.id.fragment_app_container, fragment_reguired, "fragment_required").addToBackStack("fragment_required").commit();

            }
            updateBottomNavigationPosition(2);
        } catch (Exception e) {
        }

    }

    private void displayFragmentProfile() {

        try {
            if (fragment_profile == null) {
                fragment_profile = Fragment_Profile.newInstance();
            }

            if (fragment_home != null && fragment_home.isAdded()) {
                fragmentManager.beginTransaction().hide(fragment_home).commit();
            }
            if (fragment_auction != null && fragment_auction.isAdded()) {
                fragmentManager.beginTransaction().hide(fragment_auction).commit();
            }

            if (fragment_reguired != null && fragment_reguired.isAdded()) {
                fragmentManager.beginTransaction().hide(fragment_reguired).commit();
            }
            if (fragment_more != null && fragment_more.isAdded()) {
                fragmentManager.beginTransaction().hide(fragment_more).commit();
            }
            if (fragment_profile.isAdded()) {
                fragmentManager.beginTransaction().show(fragment_profile).commit();

            } else {
                fragmentManager.beginTransaction().add(R.id.fragment_app_container, fragment_profile, "fragment_profile").addToBackStack("fragment_profile").commit();

            }
            updateBottomNavigationPosition(3);
        } catch (Exception e) {
        }
    }

    private void displayFragmentMore() {
        try {
            if (fragment_more == null) {
                fragment_more = Fragment_More.newInstance();
            }

            if (fragment_home != null && fragment_home.isAdded()) {
                fragmentManager.beginTransaction().hide(fragment_home).commit();
            }
            if (fragment_auction != null && fragment_auction.isAdded()) {
                fragmentManager.beginTransaction().hide(fragment_auction).commit();
            }

            if (fragment_reguired != null && fragment_reguired.isAdded()) {
                fragmentManager.beginTransaction().hide(fragment_reguired).commit();
            }
            if (fragment_profile != null && fragment_profile.isAdded()) {
                fragmentManager.beginTransaction().hide(fragment_profile).commit();
            }
            if (fragment_more.isAdded()) {
                fragmentManager.beginTransaction().show(fragment_more).commit();

            } else {
                fragmentManager.beginTransaction().add(R.id.fragment_app_container, fragment_more, "fragment_more").addToBackStack("fragment_more").commit();

            }
            updateBottomNavigationPosition(4);
        } catch (Exception e) {
        }
    }


    private void NavigateToSignInActivity() {
        Intent intent = new Intent(HomeActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    private void updateToken() {
       /* FirebaseInstanceId.getInstance()
                .getInstanceId()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        token = task.getResult().getToken();
                        task.getResult().getId();
                        Api.getService(Tags.base_url)
                                .updateFireBaseToken(token, userModel.getData().getId(), 1)
                                .enqueue(new Callback<ResponseBody>() {
                                    @Override
                                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                                        if (response.isSuccessful()) {
                                            try {
                                                userModel.getUser().setFireBaseToken(token);
                                                preferences.create_update_userdata(HomeActivity.this, userModel);
                                                Log.e("Success", "token updated");
                                            } catch (Exception e) {
                                                //  e.printStackTrace();
                                            }
                                        } else {
                                            try {
                                                Log.e("error", response.code() + "_" + response.errorBody().string());
                                            } catch (IOException e) {
                                                e.printStackTrace();
                                            }
                                        }


                                    }

                                    @Override
                                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                                        try {
                                            Log.e("Error", t.getMessage());
                                        } catch (Exception e) {
                                        }
                                    }
                                });
                    }
                });*/
    }

    public void refreshActivity(String lang) {
        Paper.book().write("lang", lang);
        Language.setNewLocale(this, lang);
        Intent intent = getIntent();
        finish();
        startActivity(intent);

    }

    public void logout() {
        if (userModel == null) {

            Common.CreateDialogAlert2(this, getString(R.string.please_sign_in_or_sign_up));

        } else {
            Logout();
        }
    }


    public void Logout() {
        Log.e("mmmmm", userModel.getUser().getId() + userModel.getUser().getName() + userModel.getUser().getFireBaseToken() + "");

        if (userModel != null) {
            final ProgressDialog dialog = Common.createProgressDialog(this, getString(R.string.wait));
            //   userModel = preferences.getUserData(this);
            dialog.show();
            Api.getService(Tags.base_url)
                    .logout(userModel.getUser().getId(), userModel.getUser().getFireBaseToken())
                    .enqueue(new Callback<ResponseBody>() {
                        @Override
                        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                            dialog.dismiss();
                            if (response.isSuccessful()) {
                                new Handler()
                                        .postDelayed(() -> {
                                            NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                                            if (manager != null) {
                                                manager.cancel(Tags.not_tag, Tags.not_id);

                                            }
                                        }, 1);
                                preferences.create_update_userdata(HomeActivity.this, null);
                                preferences.create_update_session(HomeActivity.this, Tags.session_logout);
                                preferences.clear(HomeActivity.this);
                                Intent intent = new Intent(HomeActivity.this, LoginActivity.class);
                                startActivity(intent);
                                finish();

                            } else {
                                try {
                                    Log.e("error", response.code() + "__" + response.errorBody().string());
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<ResponseBody> call, Throwable t) {

                        }
                    });
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        List<Fragment> fragmentList = fragmentManager.getFragments();
        for (Fragment fragment : fragmentList) {
            fragment.onActivityResult(requestCode, resultCode, data);
        }


    }

    @Override
    public void onBackPressed() {
        if (fragment_home != null && fragment_home.isAdded() && fragment_home.isVisible()) {
            if (userModel == null) {
                navigateToSignInActivity();
            } else {
                finish();
            }
        } else {
            displayFragmentHome();
        }
    }

    private void navigateToSignInActivity() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        List<Fragment> fragmentList = fragmentManager.getFragments();
        for (Fragment fragment : fragmentList) {
            fragment.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }

    }


    public void showservicecenter(int position) {
        Intent intent = new Intent(HomeActivity.this, ModelDetailsActivity.class);
        intent.putExtra("search", markModelList.get(position).getId() + "");
        intent.putExtra("data", markModelList.get(position));
        startActivity(intent);
    }

    public void showservicecenter(ProductModel productModel) {
        Intent intent = new Intent(HomeActivity.this, AccessoriesSparePartsDetailsActivity.class);
        intent.putExtra("search", productModel.getId() + "");
        startActivity(intent);

    }

    public void addtocart(ProductModel markDataInModel) {
        Create_Order_Model add_order_model = preferences.getUserOrder(HomeActivity.this);
        if (add_order_model != null) {
            if ((add_order_model.getMarket_id() + "").equals(markDataInModel.getId() + "")) {
                List<Create_Order_Model.ProductDetails> productDetails = add_order_model.getProductDetails();
                List<Create_Order_Model.OrderDetails> order_details = add_order_model.getDetails();

                Create_Order_Model.OrderDetails orderDetails1 = null;
                Create_Order_Model.ProductDetails products1 = null;

                int pos = 0;
                for (int i = 0; i < order_details.size(); i++) {
                    if (markDataInModel.getId() == order_details.get(i).getAdv_id()) {
                        orderDetails1 = order_details.get(i);
                        products1 = productDetails.get(i);
                        pos = i;
                    }
                }
                if (products1 != null) {
                    products1.setAmount(1 + order_details.get(pos).getAmount());
                    // Log.e("to",add_order_model.getTotal_cost()+(Double.parseDouble(single_product_model.getPrice())*amount)+""+((amount+order_details.get(pos).getAmount())*Double.parseDouble(single_product_model.getPrice())));
                    products1.setTotal_cost(products1.getTotal_cost() + (Double.parseDouble(markDataInModel.getPrice()) * 1));
                    products1.setImage(markDataInModel.getImage());
                    productDetails.remove(pos);
                    productDetails.add(pos, products1);
                    products1.setAmount(1 + order_details.get(pos).getAmount());
                    // Log.e("to",add_order_model.getTotal_cost()+(Double.parseDouble(single_product_model.getPrice())*amount)+""+((amount+order_details.get(pos).getAmount())*Double.parseDouble(single_product_model.getPrice())));
                    orderDetails1.setCost((Double.parseDouble(markDataInModel.getPrice())));
                    orderDetails1.setAdv_id(markDataInModel.getId());
                    orderDetails1.setAmount(1 + order_details.get(pos).getAmount());
                    order_details.remove(pos);
                    order_details.add(pos, orderDetails1);

                } else {
                    products1 = new Create_Order_Model.ProductDetails();
                    products1.setAmount(1);
                    products1.setTotal_cost(Double.parseDouble(markDataInModel.getPrice()) * 1);
                    if (lang.equals("ar")) {
                        products1.setName(markDataInModel.getTitle_ar());
                    } else {
                        products1.setName(markDataInModel.getTitle_en());

                    }
                    products1.setImage(markDataInModel.getImage());
                    productDetails.add(products1);
                    orderDetails1 = new Create_Order_Model.OrderDetails();
                    orderDetails1.setAmount(1);
                    orderDetails1.setCost(Double.parseDouble(markDataInModel.getPrice()));
                    orderDetails1.setAdv_id(markDataInModel.getId());
                    order_details.add(orderDetails1);
                }
                add_order_model.setProductDetails(productDetails);
                add_order_model.setDetails(order_details);
                // Common.CreateDialogAlert3(ProductDetialsActivity.this, getResources().getString(R.string.suc));

            } else {
                // Common.CreateDialogAlert(ProductDetialsActivity.this, getResources().getString(R.string.order_pref));
            }
        } else {
            add_order_model = new Create_Order_Model();
            List<Create_Order_Model.OrderDetails> order_details = new ArrayList<>();
            List<Create_Order_Model.ProductDetails> productDetails = new ArrayList<>();

            add_order_model.setMarket_id(markDataInModel.getId() + "");
            Create_Order_Model.ProductDetails products1 = new Create_Order_Model.ProductDetails();
            products1.setAmount(1);
            products1.setTotal_cost(Double.parseDouble(markDataInModel.getPrice()) * 1);
            if (lang.equals("ar")) {
                products1.setName(markDataInModel.getTitle_ar());
            } else {
                products1.setName(markDataInModel.getTitle_en());

            }
            products1.setImage(markDataInModel.getImage());
            productDetails.add(products1);
            Create_Order_Model.OrderDetails orderDetails1 = new Create_Order_Model.OrderDetails();
            orderDetails1.setAmount(1);
            orderDetails1.setCost(Double.parseDouble(markDataInModel.getPrice()));
            orderDetails1.setAdv_id(markDataInModel.getId());
            order_details.add(orderDetails1);
            add_order_model.setProductDetails(productDetails);
            add_order_model.setDetails(order_details);
        }
        preferences.create_update_order(HomeActivity.this, add_order_model);


    }

}