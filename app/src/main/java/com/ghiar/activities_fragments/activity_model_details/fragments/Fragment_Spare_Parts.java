package com.ghiar.activities_fragments.activity_model_details.fragments;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.ghiar.R;
import com.ghiar.activities_fragments.activity_home.HomeActivity;
import com.ghiar.activities_fragments.activity_home.fragments.Fragment_Home;
import com.ghiar.activities_fragments.activity_model_details.ModelDetailsActivity;
import com.ghiar.activities_fragments.activity_signup.SignUpActivity;
import com.ghiar.adapters.CityAdapter;
import com.ghiar.adapters.MarkDataInAdapter;
import com.ghiar.adapters.ModelsAdapter;
import com.ghiar.databinding.FragmentHomeBinding;
import com.ghiar.databinding.FragmentSpareAccessoriesBinding;
import com.ghiar.models.CityDataModel;
import com.ghiar.models.MarkDataInModel;
import com.ghiar.models.MarksDataModel;
import com.ghiar.models.MarksModel;
import com.ghiar.models.ModelModel;
import com.ghiar.models.ModelsData;
import com.ghiar.models.UserModel;
import com.ghiar.preferences.Preferences;
import com.ghiar.remote.Api;
import com.ghiar.share.Common;
import com.ghiar.tags.Tags;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import io.paperdb.Paper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Fragment_Spare_Parts extends Fragment {

    private ModelDetailsActivity activity;
    private FragmentSpareAccessoriesBinding binding;
    private Preferences preferences;
    private UserModel userModel;
    private String lang;
    private List<ModelModel> modelModelList;
    private ModelsAdapter modelsAdapter;
    private ModelModel modelModel;
    private List<CityDataModel.CityModel> cityList;
    private CityAdapter cityAdapter;
    private CityDataModel.CityModel cityModel;
    private List<MarkDataInModel> markDataInModelList;
    private ArrayAdapter<String> adapter;
    private List<String> list;
    private List<String> list2;
    private MarkDataInAdapter markDataInAdapter;
    private String markid, model_id, status, title, country_id;

    public static Fragment_Spare_Parts newInstance() {

        return new Fragment_Spare_Parts();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_spare_accessories, container, false);
        initView();
        getFilterData();
        return binding.getRoot();
    }


    private void initView() {

        list = new ArrayList<>();
        list2 = new ArrayList<>();
        markDataInModelList = new ArrayList<>();
        modelModelList = new ArrayList<>();
        cityList = new ArrayList<>();
        activity = (ModelDetailsActivity) getActivity();
        markid = activity.markid;
        preferences = Preferences.getInstance();
        Paper.init(activity);
        lang = Paper.book().read("lang", "ar");
        userModel = preferences.getUserData(activity);
        binding.setLang(lang);
        modelsAdapter = new ModelsAdapter(modelModelList, activity);
        binding.spinnerModel.setAdapter(modelsAdapter);
        modelModel = new ModelModel();
        modelModel.setId(0);
        modelModel.setTitle_ar(getString(R.string.choose));
        modelModelList.add(modelModel);
        cityAdapter = new CityAdapter(cityList, activity);
        binding.spinnerCity.setAdapter(cityAdapter);
        list.add(getResources().getString(R.string.choose));
        list.add(getResources().getString(R.string.news));
        list.add(getResources().getString(R.string.used));
        markDataInAdapter = new MarkDataInAdapter(activity, markDataInModelList);
        binding.recView.setLayoutManager(new LinearLayoutManager(activity));
        binding.recView.setAdapter(markDataInAdapter);

        list2.add("new");
        list2.add("used");
        adapter = new ArrayAdapter<String>(activity,
                android.R.layout.simple_spinner_item, list);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        binding.spinnerState.setAdapter(adapter);
        binding.spinnerModel.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    modelModel.setId(0);
                } else {
                    model_id = modelModelList.get(position).getId() + "";
                    modelModel.setId(modelModelList.get(position).getId());
                    getFilterData();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        cityModel = new CityDataModel.CityModel();
        cityModel.setId_city("0");
        cityModel.setAr_city_title(getString(R.string.choose));
        cityList.add(cityModel);
        binding.editQuery.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                String query = binding.editQuery.getText().toString();
                if (!TextUtils.isEmpty(query)) {
                    Common.CloseKeyBoard(activity, binding.editQuery);
                    title = query;
                    getFilterData();
                    return false;
                }
            }
            return false;
        });
        binding.imsearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String query = binding.editQuery.getText().toString();
                if (!TextUtils.isEmpty(query)) {
                    Common.CloseKeyBoard(activity, binding.editQuery);
                    title = query;
                    getFilterData();
                }
            }
        });
        binding.spinnerCity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                } else {
                    country_id = cityList.get(position).getId_city() + "";
                    getFilterData();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        binding.spinnerState.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                } else {
                    status = list2.get(position - 1);
                    getFilterData();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        getModels();
        getCities();
    }

    private void getCities() {
        ProgressDialog dialog = Common.createProgressDialog(activity, getString(R.string.wait));
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
                                activity.runOnUiThread(() -> {
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
                                Toast.makeText(activity, "Server Error", Toast.LENGTH_SHORT).show();


                            } else {
                                Toast.makeText(activity, getString(R.string.failed), Toast.LENGTH_SHORT).show();


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
                                    Toast.makeText(activity, R.string.something, Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(activity, t.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }

                        } catch (Exception e) {
                        }
                    }
                });
    }


    private void getModels() {
        ProgressDialog dialog = Common.createProgressDialog(activity, getString(R.string.wait));
        dialog.show();

        Api.getService(Tags.base_url)
                .getModels()
                .enqueue(new Callback<ModelsData>() {
                    @Override
                    public void onResponse(Call<ModelsData> call, Response<ModelsData> response) {
                        dialog.dismiss();
                        if (response.isSuccessful() && response.body() != null) {
                            if (response.body().getModels().size() > 0) {
                                modelModelList.clear();
                                modelModelList.add(modelModel);
                                modelModelList.addAll(response.body().getModels());
                                //  Log.e("data", markModelList.size() + "__");
                                activity.runOnUiThread(() -> {
                                    modelsAdapter.notifyDataSetChanged();
                                });
                            }
                        } else {
                            try {

                                Log.e("error", response.code() + "_" + response.errorBody().string());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }

                            if (response.code() == 500) {
                                Toast.makeText(activity, "Server Error", Toast.LENGTH_SHORT).show();


                            } else {
                                Toast.makeText(activity, getString(R.string.failed), Toast.LENGTH_SHORT).show();


                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<ModelsData> call, Throwable t) {
                        try {
                            dialog.dismiss();
                            if (t.getMessage() != null) {
                                Log.e("error", t.getMessage());
                                if (t.getMessage().toLowerCase().contains("failed to connect") || t.getMessage().toLowerCase().contains("unable to resolve host")) {
                                    Toast.makeText(activity, R.string.something, Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(activity, t.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }

                        } catch (Exception e) {
                        }
                    }
                });
    }


    private void getFilterData() {

        markDataInModelList.clear();
        markDataInAdapter.notifyDataSetChanged();
        binding.progBar.setVisibility(View.VISIBLE);

        try {


            Api.getService(Tags.base_url)
                    .get_MarkDataIn(country_id, model_id, status, title, "part", markid)
                    .enqueue(new Callback<MarksDataModel>() {
                        @Override
                        public void onResponse(Call<MarksDataModel> call, Response<MarksDataModel> response) {
                            binding.progBar.setVisibility(View.GONE);
                            if (response.isSuccessful() && response.body() != null && response.body().getData() != null) {
                                markDataInModelList.clear();
                                markDataInModelList.addAll(response.body().getData());
                                if (response.body().getData().size() > 0) {
                                    // rec_sent.setVisibility(View.VISIBLE);
                                    //   Log.e("datasssssss",response.body().getMarks().get(0).getTitle_ar());

                                    // binding.flNotification.setVisibility(View.GONE);
                                    markDataInAdapter.notifyDataSetChanged();
                                    //   total_page = response.body().getMeta().getLast_page();

                                } else {
                                    markDataInAdapter.notifyDataSetChanged();

                                    //     binding.llNoNotification.setVisibility(View.VISIBLE);

                                }
                            } else {
                                markDataInAdapter.notifyDataSetChanged();

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
                        public void onFailure(Call<MarksDataModel> call, Throwable t) {
                            try {
                                binding.progBar.setVisibility(View.GONE);
                                //binding.llNoNotification.setVisibility(View.VISIBLE);


                            } catch (Exception e) {
                            }
                        }
                    });
        } catch (Exception e) {
            binding.progBar.setVisibility(View.GONE);
            // binding.ll.setVisibility(View.VISIBLE);

        }
    }


}

