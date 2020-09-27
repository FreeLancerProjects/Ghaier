package com.ghiar.activities_fragments.activity_home.fragments;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.ghiar.R;
import com.ghiar.activities_fragments.activity_home.HomeActivity;
import com.ghiar.adapters.MarkAdapter;
import com.ghiar.adapters.ProductAdapter;
import com.ghiar.adapters.ServicesAdapter;
import com.ghiar.adapters.SliderAdapter;
import com.ghiar.databinding.FragmentHomeBinding;
import com.ghiar.databinding.FragmentMoreBinding;
import com.ghiar.interfaces.Listeners;
import com.ghiar.models.MarkModel;
import com.ghiar.models.ProductModel;
import com.ghiar.models.ServiceModel;
import com.ghiar.models.SliderModel;
import com.ghiar.models.UserModel;
import com.ghiar.preferences.Preferences;
import com.ghiar.remote.Api;
import com.ghiar.services.Service;
import com.ghiar.tags.Tags;
import com.smarteist.autoimageslider.IndicatorView.animation.type.IndicatorAnimationType;
import com.smarteist.autoimageslider.SliderAnimations;
import com.smarteist.autoimageslider.SliderView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import io.paperdb.Paper;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.ghiar.tags.Tags.base_url;

public class Fragment_Home extends Fragment {

    private static final String TAG = "Fragment_Home";
    private HomeActivity activity;
    private FragmentHomeBinding binding;
    private Preferences preferences;
    private UserModel.User userModel;
    private String lang;
    private ServicesAdapter servicesAdapter;
    private SliderAdapter sliderAdapter;
    private ProductAdapter accessoriesAdapter;
    private ProductAdapter partsAdapter;
    private List<ProductModel> productModelList, productModelsspare;
    private List<ServiceModel> serviceModelList;
    private List<SliderModel> sliderModelList, sliderModels;


    public static Fragment_Home newInstance() {

        return new Fragment_Home();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false);
        initView();
        getServices();
        getSliderData();
        getParts();
        getAccessories();
        return binding.getRoot();
    }

    private void initView() {
        productModelList = new ArrayList<>();
        serviceModelList = new ArrayList<>();
        productModelsspare = new ArrayList<>();
        sliderModelList = new ArrayList<>();
        sliderModels = new ArrayList<>();
        activity = (HomeActivity) getActivity();
        preferences = Preferences.getInstance();

        //services recyclerview
        servicesAdapter = new ServicesAdapter(activity, serviceModelList);
        binding.recViewService.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        binding.recViewService.setAdapter(servicesAdapter);

        //services recyclerview
        partsAdapter = new ProductAdapter(activity, this, productModelsspare);
        binding.recViewSpareParts.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        binding.recViewSpareParts.setAdapter(partsAdapter);
        //services recyclerview
        accessoriesAdapter = new ProductAdapter(activity, this, productModelList);
        binding.recViewAccessories.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        binding.recViewAccessories.setAdapter(accessoriesAdapter);

        sliderInit();

        Paper.init(activity);
        lang = Paper.book().read("lang", "ar");
        userModel = preferences.getUserData(activity);
        binding.setLang(lang);

    }

    //initiate slider ui
    private void sliderInit() {
        //top slider
        sliderAdapter = new SliderAdapter(activity, sliderModelList);
        binding.imageSliderTop.setSliderAdapter(sliderAdapter);
        binding.imageSliderTop.setIndicatorAnimation(IndicatorAnimationType.WORM); //set indicator animation by using SliderLayout.IndicatorAnimations. :WORM or THIN_WORM or COLOR or DROP or FILL or NONE or SCALE or SCALE_DOWN or SLIDE and SWAP!!
        binding.imageSliderTop.setSliderTransformAnimation(SliderAnimations.SIMPLETRANSFORMATION);
        binding.imageSliderTop.setAutoCycleDirection(SliderView.AUTO_CYCLE_DIRECTION_BACK_AND_FORTH);
        binding.imageSliderTop.setIndicatorSelectedColor(Color.WHITE);
        binding.imageSliderTop.setIndicatorUnselectedColor(Color.GRAY);
        binding.imageSliderTop.setScrollTimeInSec(3);
        binding.imageSliderTop.setAutoCycle(true);
        binding.imageSliderTop.startAutoCycle();

        //bottom slider
        sliderAdapter = new SliderAdapter(activity, sliderModels);
        binding.imageSliderBottom.setSliderAdapter(sliderAdapter);
        binding.imageSliderBottom.setIndicatorAnimation(IndicatorAnimationType.WORM); //set indicator animation by using SliderLayout.IndicatorAnimations. :WORM or THIN_WORM or COLOR or DROP or FILL or NONE or SCALE or SCALE_DOWN or SLIDE and SWAP!!
        binding.imageSliderBottom.setSliderTransformAnimation(SliderAnimations.SIMPLETRANSFORMATION);
        binding.imageSliderBottom.setAutoCycleDirection(SliderView.AUTO_CYCLE_DIRECTION_BACK_AND_FORTH);
        binding.imageSliderBottom.setIndicatorSelectedColor(Color.WHITE);
        binding.imageSliderBottom.setIndicatorUnselectedColor(Color.GRAY);
        binding.imageSliderBottom.setScrollTimeInSec(3);
        binding.imageSliderBottom.setAutoCycle(true);
        binding.imageSliderBottom.startAutoCycle();

    }


    // get services
    private void getServices() {

        Api.getService(base_url).getHomeServices().enqueue(new Callback<ServiceModel>() {
            @Override
            public void onResponse(Call<ServiceModel> call, Response<ServiceModel> response) {
                binding.progBarService.setVisibility(View.GONE);
                if (response.isSuccessful() && response.body() != null && response.body().getMain_services() != null) {
                    serviceModelList.clear();
                    serviceModelList.addAll(response.body().getMain_services());
                    if (response.body().getMain_services().size() > 0) {
                        // rec_sent.setVisibility(View.VISIBLE);
                        //  Log.e("data",response.body().getData().get(0).getAr_title());

                        // binding.ll.setVisibility(View.GONE);
                        servicesAdapter.notifyDataSetChanged();
                        //   total_page = response.body().getMeta().getLast_page();

                    } else {
                        servicesAdapter.notifyDataSetChanged();

                        // binding.llNoStore.setVisibility(View.VISIBLE);

                    }
                } else {
                    servicesAdapter.notifyDataSetChanged();

                    //  binding.llNoStore.setVisibility(View.VISIBLE);

                    //Toast.makeText(activity, getString(R.string.failed), Toast.LENGTH_SHORT).show();
                    try {
                        Log.e("Error_code", response.code() + "_" + response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<ServiceModel> call, Throwable t) {
                Log.d(TAG, t.getMessage());
                binding.progBarService.setVisibility(View.GONE);

            }
        });

    }


    // get slider data from server
    private void getSliderData() {
        Api.getService(base_url).getHomeSliderData().enqueue(new Callback<SliderModel>() {
            @Override
            public void onResponse(Call<SliderModel> call, Response<SliderModel> response) {

                if (response.code() == 200 && response.body() != null) {
                    sliderModelList.addAll(response.body().getTop());
                    sliderAdapter.notifyDataSetChanged();
                    binding.imageSliderTop.setSliderAdapter(sliderAdapter);


                    sliderModels.addAll(response.body().getBottom());
                    sliderAdapter.notifyDataSetChanged();
                    binding.imageSliderBottom.setSliderAdapter(sliderAdapter);
                }

            }

            @Override
            public void onFailure(Call<SliderModel> call, Throwable t) {
                Log.d(TAG, t.getMessage());
            }
        });

    }


    private void getParts() {
        Api.getService(base_url).getParts("off").enqueue(new Callback<ProductModel>() {
            @Override
            public void onResponse(Call<ProductModel> call, Response<ProductModel> response) {
                binding.progBarSpareParts.setVisibility(View.GONE);
                if (response.isSuccessful() && response.body() != null && response.body().getPart() != null) {
                    productModelsspare.clear();
                    productModelsspare.addAll(response.body().getPart());
                    if (response.body().getPart().size() > 0) {
                        // rec_sent.setVisibility(View.VISIBLE);
                        //  Log.e("data",response.body().getData().get(0).getAr_title());

                        // binding.llNoStore.setVisibility(View.GONE);
                        partsAdapter.notifyDataSetChanged();
                        //   total_page = response.body().getMeta().getLast_page();

                    } else {
                        partsAdapter.notifyDataSetChanged();

                        //  binding.llNoStore.setVisibility(View.VISIBLE);

                    }
                } else {
                    partsAdapter.notifyDataSetChanged();

                    // binding.llNoStore.setVisibility(View.VISIBLE);

                    //Toast.makeText(activity, getString(R.string.failed), Toast.LENGTH_SHORT).show();
                    try {
                        Log.e("Error_code", response.code() + "_" + response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

            }

            @Override
            public void onFailure(Call<ProductModel> call, Throwable t) {
                Log.d(TAG, t.getMessage());
                binding.progBarSpareParts.setVisibility(View.GONE);
            }
        });

    }

    private void getAccessories() {
        Api.getService(base_url).getAccessories("off").enqueue(new Callback<ProductModel>() {
            @Override
            public void onResponse(Call<ProductModel> call, Response<ProductModel> response) {
                binding.progBarAccessories.setVisibility(View.GONE);
                if (response.isSuccessful() && response.body() != null && response.body().getAccessory() != null) {
                    productModelList.clear();
                    productModelList.addAll(response.body().getAccessory());
                    if (response.body().getAccessory().size() > 0) {
                        // rec_sent.setVisibility(View.VISIBLE);
                        //  Log.e("data",response.body().getData().get(0).getAr_title());

                        // binding.llNoStore.setVisibility(View.GONE);
                        accessoriesAdapter.notifyDataSetChanged();
                        //   total_page = response.body().getMeta().getLast_page();

                    } else {
                        accessoriesAdapter.notifyDataSetChanged();

                        //  binding.llNoStore.setVisibility(View.VISIBLE);

                    }
                } else {
                    accessoriesAdapter.notifyDataSetChanged();

                    // binding.llNoStore.setVisibility(View.VISIBLE);

                    //Toast.makeText(activity, getString(R.string.failed), Toast.LENGTH_SHORT).show();
                    try {
                        Log.e("Error_code", response.code() + "_" + response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

            }


            @Override
            public void onFailure(Call<ProductModel> call, Throwable t) {
                Log.d(TAG, t.getMessage());
                binding.progBarAccessories.setVisibility(View.GONE);
            }
        });
    }
}
