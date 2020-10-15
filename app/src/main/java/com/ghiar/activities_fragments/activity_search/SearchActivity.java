package com.ghiar.activities_fragments.activity_search;

import android.content.Context;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;


import com.ghiar.R;
import com.ghiar.activities_fragments.activity_model_details.ModelDetailsActivity;
import com.ghiar.adapters.MarkDataInAdapter;
import com.ghiar.databinding.ActivitySearchBinding;
import com.ghiar.interfaces.Listeners;
import com.ghiar.language.Language;
import com.ghiar.models.Create_Order_Model;
import com.ghiar.models.MarkDataInModel;
import com.ghiar.models.MarksDataModel;
import com.ghiar.models.UserModel;
import com.ghiar.preferences.Preferences;
import com.ghiar.remote.Api;
import com.ghiar.tags.Tags;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import io.paperdb.Paper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchActivity extends AppCompatActivity implements Listeners.BackListener {
    private ActivitySearchBinding binding;
    private String lang;
    private Preferences preferences;
    private UserModel userModel;
    // private Animation animation;
    private LinearLayoutManager manager;
    private List<MarkDataInModel> productModelList;
    private MarkDataInAdapter adapter;
    private String search;

    @Override
    protected void attachBaseContext(Context newBase) {
        Paper.init(newBase);
        super.attachBaseContext(Language.updateResources(newBase, Paper.book().read("lang", Locale.getDefault().getLanguage())));
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_search);
        getDataFromIntent();
        initView();
        if (search != null) {
            search(search);
        }

    }

    private void getDataFromIntent() {

        Intent intent = getIntent();
        if (intent != null && intent.getStringExtra("search") != null) {
            search = intent.getStringExtra("search");
        }
    }

    private void initView() {
        preferences = Preferences.getInstance();
        userModel = preferences.getUserData(this);
        productModelList = new ArrayList<>();
        Paper.init(this);
        lang = Paper.book().read("lang", "ar");
        binding.setLang(lang);
        binding.setBackListener(this);
//        animation = AnimationUtils.loadAnimation(this,R.anim.search_anim);
//        binding.ll.startAnimation(animation);
//        animation.setAnimationListener(new Animation.AnimationListener() {
//            @Override
//            public void onAnimationStart(Animation animation) {
//                binding.ll.setVisibility(View.VISIBLE);
//            }
//
//            @Override
//            public void onAnimationEnd(Animation animation) {
//
//            }
//
//            @Override
//            public void onAnimationRepeat(Animation animation) {
//
//            }
//        });
        binding.progBar.getIndeterminateDrawable().setColorFilter(ContextCompat.getColor(this, R.color.colorPrimary), PorterDuff.Mode.SRC_IN);
        manager = new LinearLayoutManager(this);
        binding.recView.setLayoutManager(manager);
        adapter = new MarkDataInAdapter(this, productModelList);
        binding.recView.setAdapter(adapter);

        binding.edtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

                String query = editable.toString().trim();
                if (!query.isEmpty()) {
                    search(query);
                } else {
                    productModelList.clear();
                    adapter.notifyDataSetChanged();
                    binding.tvNoSearchResult.setVisibility(View.VISIBLE);
                }
            }
        });

    }


    private void search(String query) {
        binding.tvNoSearchResult.setVisibility(View.GONE);
        binding.progBar.setVisibility(View.VISIBLE);
        productModelList.clear();
        adapter.notifyDataSetChanged();
        try {

            Api.getService(Tags.base_url).searchByName(query, "off")
                    .enqueue(new Callback<MarksDataModel>() {
                        @Override
                        public void onResponse(Call<MarksDataModel> call, Response<MarksDataModel> response) {
                            binding.progBar.setVisibility(View.GONE);
                            if (response.isSuccessful() && response.body() != null && response.body().getData() != null) {

                                productModelList.clear();

                                if (response.body().getData().size() > 0) {
                                    productModelList.addAll(response.body().getData());

                                    binding.tvNoSearchResult.setVisibility(View.GONE);
                                    adapter.notifyDataSetChanged();
                                } else {
                                    binding.tvNoSearchResult.setVisibility(View.VISIBLE);
                                }


                            } else {

                                try {

                                    Log.e("error", response.code() + "_" + response.errorBody().string());
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }

                                if (response.code() == 500) {
                                    //Toast.makeText(com.technology.circles.apps.testahil.activities_fragments.activity_search.SearchActivity.this, "Server Error", Toast.LENGTH_SHORT).show();


                                } else {
                                    //Toast.makeText(com.technology.circles.apps.testahil.activities_fragments.activity_search.SearchActivity.this, getString(R.string.failed), Toast.LENGTH_SHORT).show();


                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<MarksDataModel> call, Throwable t) {
                            try {
                                binding.progBar.setVisibility(View.GONE);
                                if (t.getMessage() != null) {
                                    Log.e("error", t.getMessage());
                                    if (t.getMessage().toLowerCase().contains("failed to connect") || t.getMessage().toLowerCase().contains("unable to resolve host")) {
                                        //    Toast.makeText(com.technology.circles.apps.testahil.activities_fragments.activity_search.SearchActivity.this, R.string.something, Toast.LENGTH_SHORT).show();
                                    } else {
                                        //      Toast.makeText(com.technology.circles.apps.testahil.activities_fragments.activity_search.SearchActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                }

                            } catch (Exception e) {
                            }
                        }
                    });
        } catch (Exception e) {

        }
    }


    @Override
    public void back() {
        finish();
    }

    public void addtocart(MarkDataInModel markDataInModel) {
        Create_Order_Model add_order_model = preferences.getUserOrder(SearchActivity.this);
        if (add_order_model != null) {
            if ((add_order_model.getMarket_id() + "").equals(markDataInModel.getMarket().getId()+"")) {
                List<Create_Order_Model.ProductDetails> productDetails = add_order_model.getProductDetails();
                List<Create_Order_Model.OrderDetails> order_details = add_order_model.getDetails();

                Create_Order_Model.OrderDetails orderDetails1 = null;
                Create_Order_Model.ProductDetails products1 = null;

                int pos = 0;
                for (int i = 0; i < order_details.size(); i++) {
                    if (markDataInModel.getId() == order_details.get(i).getAdv_id()) {
                        orderDetails1 = order_details.get(i);
                        products1=productDetails.get(i);
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
                    orderDetails1.setCost( (Double.parseDouble(markDataInModel.getPrice()) ));
                    orderDetails1.setAdv_id(markDataInModel.getId());
                    orderDetails1.setAmount(1 + order_details.get(pos).getAmount());
                    order_details.remove(pos);
                    order_details.add(pos, orderDetails1);

                } else {
                    products1 = new Create_Order_Model.ProductDetails();
                    products1.setAmount(1);
                    products1.setTotal_cost(Double.parseDouble(markDataInModel.getPrice()) * 1);
                    if(lang.equals("ar")) {
                        products1.setName(markDataInModel.getTitle_ar());
                    }else {
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

            }
            else {
                // Common.CreateDialogAlert(ProductDetialsActivity.this, getResources().getString(R.string.order_pref));
            }
        }
        else {
            add_order_model = new Create_Order_Model();
            List<Create_Order_Model.OrderDetails> order_details = new ArrayList<>();
            List<Create_Order_Model.ProductDetails> productDetails = new ArrayList<>();

            add_order_model.setMarket_id(markDataInModel.getMarket().getId()+"");
            Create_Order_Model.ProductDetails products1 = new Create_Order_Model.ProductDetails();
            products1.setAmount(1);
            products1.setTotal_cost(Double.parseDouble(markDataInModel.getPrice()) * 1);
            if(lang.equals("ar")) {
                products1.setName(markDataInModel.getTitle_ar());
            }else {
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
        preferences.create_update_order(SearchActivity.this,add_order_model );


    }

}
