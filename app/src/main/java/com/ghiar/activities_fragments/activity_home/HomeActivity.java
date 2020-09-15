package com.ghiar.activities_fragments.activity_home;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;

import com.aurelhubert.ahbottomnavigation.AHBottomNavigation;
import com.aurelhubert.ahbottomnavigation.AHBottomNavigationItem;
import com.ghiar.R;
import com.ghiar.activities_fragments.activity_cart.CartActivity;
import com.ghiar.activities_fragments.activity_home.fragments.Fragment_Auction;
import com.ghiar.activities_fragments.activity_home.fragments.Fragment_Home;
import com.ghiar.activities_fragments.activity_home.fragments.Fragment_More;
import com.ghiar.activities_fragments.activity_home.fragments.Fragment_Profile;
import com.ghiar.activities_fragments.activity_home.fragments.Fragment_Reguired;
import com.ghiar.activities_fragments.activity_login.LoginActivity;
import com.ghiar.activities_fragments.activity_model_details.ModelDetailsActivity;
import com.ghiar.activities_fragments.activity_notification.NotificationActivity;
import com.ghiar.adapters.MarkAdapter;
import com.ghiar.databinding.ActivityHomeBinding;
import com.ghiar.language.Language;
import com.ghiar.models.MarkModel;
import com.ghiar.models.UserModel;
import com.ghiar.preferences.Preferences;
import com.ghiar.remote.Api;
import com.ghiar.share.Common;
import com.ghiar.tags.Tags;
import com.google.firebase.iid.FirebaseInstanceId;

import org.greenrobot.eventbus.EventBus;

import java.io.IOException;
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
    @Override
    protected void attachBaseContext(Context newBase) {
        Paper.init(newBase);
        super.attachBaseContext(Language.updateResources(newBase, Paper.book().read("lang", Locale.getDefault().getLanguage())));

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_home);
        NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);

        if (manager != null) {
            manager.cancel(Tags.not_tag, Tags.not_id);

        }
        initView();

        if (savedInstanceState == null) {
            Log.e("gg","gg");
            displayFragmentHome();

        }

        if (userModel != null) {

            Log.e("bbbbbbbb",userModel.getUser().getName()+"");
           // EventBus.getDefault().register(this);
            updateToken();

        }

        binding.flNotification.setOnClickListener(v -> {
            Intent intent = new Intent(this, NotificationActivity.class);
            startActivity(intent);
        });

        binding.imageCart.setOnClickListener(v -> {
            Intent intent = new Intent(this, CartActivity.class);
            startActivity(intent);
        });

        binding.imageChat.setOnClickListener(v -> {
            Intent intent = new Intent(this, ModelDetailsActivity.class);
            startActivity(intent);
        });


    }


    @SuppressLint("RestrictedApi")
    private void initView() {
        preferences = Preferences.getInstance();
        userModel = preferences.getUserData(this);
        toggle = new ActionBarDrawerToggle(this,binding.drawer,binding.toolBar,R.string.open,R.string.close);
        toggle.syncState();
        fragmentManager = getSupportFragmentManager();

        // init marks recyclerview
        markAdapter = new MarkAdapter(this);
        binding.recViewModel.setLayoutManager(new LinearLayoutManager(this));
        getDrawerMarks();
        setUpBottomNavigation();
    }


    // get marks data for navigation drawer
    private void getDrawerMarks(){

        Api.getService(Tags.base_url).getMarks().enqueue(new Callback<MarkModel>() {
            @Override
            public void onResponse(Call<MarkModel> call, Response<MarkModel> response) {
                binding.progBarModel.setVisibility(View.GONE);
                Log.d(TAG,"Mark Size:"+response.body().getMarks());
                markAdapter.setList(response.body().getMarks());
                binding.recViewModel.setAdapter(markAdapter);
            }

            @Override
            public void onFailure(Call<MarkModel> call, Throwable t) {
                binding.progBarModel.setVisibility(View.GONE);
                Log.d(TAG,t.getMessage());

            }
        });


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
                    displayFragmentSearch();



                    break;
                case 3:
                    if (userModel!=null){
                        displayFragmentProfile();

                    }else {
                        Common.CreateDialogAlert(this,getString(R.string.please_sign_in_or_sign_up));
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

            Common.CreateDialogAlert(this, getString(R.string.please_sign_in_or_sign_up));

        } else {
            Logout();
        }
    }


    @SuppressLint("RestrictedApi")
    @Override
    public void onBackPressed() {

        if (binding.drawer.isDrawerOpen(GravityCompat.START)){
            binding.drawer.closeDrawer(GravityCompat.START);
        }else {
            if (userModel != null) {
                if (EventBus.getDefault().isRegistered(this)) {
                    EventBus.getDefault().unregister(this);
                }

            /*if (fragment_home != null && fragment_home.isAdded() && fragment_home.isVisible()) {
                finish();
            } else {
                displayFragmentMain();


            }*/

            } else {

            /*if (fragment_home != null && fragment_home.isAdded() && fragment_home.isVisible()) {
                NavigateToSignInActivity();
            } else {
                displayFragmentMain();


            }*/
            }
        }

    }


    public void Logout() {
        if (userModel != null) {
            final ProgressDialog dialog = Common.createProgressDialog(this, getString(R.string.wait));
            userModel = preferences.getUserData(this);
            dialog.show();
            Log.e("mmmmm", userModel.getUser().getId() + userModel.getUser().getFireBaseToken() + "");
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
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        List<Fragment> fragmentList = fragmentManager.getFragments();
        for (Fragment fragment : fragmentList) {
            fragment.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }

    }


}