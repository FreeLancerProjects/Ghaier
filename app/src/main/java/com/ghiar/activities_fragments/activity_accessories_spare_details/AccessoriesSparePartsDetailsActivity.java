package com.ghiar.activities_fragments.activity_accessories_spare_details;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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

import com.ghiar.R;
import com.ghiar.activities_fragments.activity_cart.CartActivity;
import com.ghiar.activities_fragments.activity_home.HomeActivity;
import com.ghiar.activities_fragments.activity_map_show_location.MapShowLocationActivity;
import com.ghiar.activities_fragments.activity_model_details.ModelDetailsActivity;
import com.ghiar.activities_fragments.activtity_auction_detials.AuctionDetialsActivity;
import com.ghiar.adapters.AllAdversimentAdapter;
import com.ghiar.adapters.ProductSlideAdapter;
import com.ghiar.adapters.SameAdversimentAdapter;
import com.ghiar.databinding.ActivityAboutAppBinding;
import com.ghiar.databinding.ActivityAccessoriesSparePartsDetailsBinding;
import com.ghiar.interfaces.Listeners;
import com.ghiar.language.Language;
import com.ghiar.models.Create_Order_Model;
import com.ghiar.models.MarkDataInModel;
import com.ghiar.models.MarkModel;
import com.ghiar.models.MarksDataModel;
import com.ghiar.models.SingleAuctionModel;
import com.ghiar.preferences.Preferences;
import com.ghiar.remote.Api;
import com.ghiar.share.Common;
import com.ghiar.tags.Tags;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import io.paperdb.Paper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AccessoriesSparePartsDetailsActivity extends AppCompatActivity implements Listeners.BackListener {
    private ActivityAccessoriesSparePartsDetailsBinding binding;
    private String lang;
    private String search_id;
    private List<MarksDataModel.All> allList;
    private List<MarksDataModel.Like> likeList;
    private AllAdversimentAdapter allAdversimentAdapter;
    private SameAdversimentAdapter sameAdversimentAdapter;
    private ProductSlideAdapter productSlideAdapter;
    private MarksDataModel singleadversimet;
    private Intent intent;
    private static final int REQUEST_PHONE_CALL = 1;
    private Preferences preferences;
    private int amount = 1;

    protected void attachBaseContext(Context newBase) {
        Paper.init(newBase);
        super.attachBaseContext(Language.updateResources(newBase, Paper.book().read("lang", Locale.getDefault().getLanguage())));}

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_accessories_spare_parts_details);
        getDataFromIntent();
        initView();
        getsingleauction();

    }

    private void getDataFromIntent() {
        Intent intent = getIntent();
        if (intent.getStringExtra("search") != null) {
            search_id = intent.getStringExtra("search");
        }
    }

    private void initView() {
        Paper.init(this);
        lang = Paper.book().read("lang", Locale.getDefault().getLanguage());
binding.setBackListener(this);
        binding.setLang(lang);
        allList = new ArrayList<>();
        likeList = new ArrayList<>();
        preferences = Preferences.getInstance();
        sameAdversimentAdapter = new SameAdversimentAdapter(this, likeList);
        allAdversimentAdapter = new AllAdversimentAdapter(this, allList);
        binding.recViewMostSeller.setLayoutManager(new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false));
        binding.recViewall.setLayoutManager(new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false));
        binding.recViewMostSeller.setAdapter(sameAdversimentAdapter);
        binding.recViewall.setAdapter(allAdversimentAdapter);
        binding.imageCart.setOnClickListener(v -> {
            Intent intent = new Intent(AccessoriesSparePartsDetailsActivity.this, CartActivity.class);
            startActivity(intent);
        });
        binding.flCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (singleadversimet != null) {
                    call(singleadversimet.getAdvertsment().getMarket().getPhone_code() + singleadversimet.getAdvertsment().getMarket().getPhone());
                }
            }
        });
        binding.flLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigateToSMapActivity();
            }
        });
        binding.imincrease.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                amount += 1;
                binding.tvamount.setText(amount + "");
            }
        });
        binding.imdecrease.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (amount > 1) {
                    amount -= 1;
                }
                binding.tvamount.setText(amount + "");

            }
        });
        binding.btnAddToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Create_Order_Model add_order_model = preferences.getUserOrder(AccessoriesSparePartsDetailsActivity.this);
                if (add_order_model != null) {
                    if ((add_order_model.getMarket_id() + "").equals(singleadversimet.getAdvertsment().getMarket().getId() + "")) {
                        List<Create_Order_Model.ProductDetails> productDetails = add_order_model.getProductDetails();
                        List<Create_Order_Model.OrderDetails> order_details = add_order_model.getDetails();

                        Create_Order_Model.OrderDetails orderDetails1 = null;
                        Create_Order_Model.ProductDetails products1 = null;

                        int pos = 0;
                        for (int i = 0; i < order_details.size(); i++) {
                            if (singleadversimet.getAdvertsment().getId() == order_details.get(i).getAdv_id()) {
                                orderDetails1 = order_details.get(i);
                                products1 = productDetails.get(i);
                                pos = i;
                            }
                        }
                        if (products1 != null) {
                            products1.setAmount(amount + order_details.get(pos).getAmount());
                            // Log.e("to",add_order_model.getTotal_cost()+(Double.parseDouble(single_product_model.getPrice())*amount)+""+((amount+order_details.get(pos).getAmount())*Double.parseDouble(single_product_model.getPrice())));
                            products1.setTotal_cost(products1.getTotal_cost() + (Double.parseDouble(singleadversimet.getAdvertsment().getPrice()) * amount));
                            products1.setImage(singleadversimet.getAdvertsment().getImage());
                            productDetails.remove(pos);
                            productDetails.add(pos, products1);
                            products1.setAmount(amount + order_details.get(pos).getAmount());
                            // Log.e("to",add_order_model.getTotal_cost()+(Double.parseDouble(single_product_model.getPrice())*amount)+""+((amount+order_details.get(pos).getAmount())*Double.parseDouble(single_product_model.getPrice())));
                            orderDetails1.setCost((Double.parseDouble(singleadversimet.getAdvertsment().getPrice())));
                            orderDetails1.setAdv_id(singleadversimet.getAdvertsment().getId());
                            orderDetails1.setAmount(amount + order_details.get(pos).getAmount());
                            order_details.remove(pos);
                            order_details.add(pos, orderDetails1);

                        } else {
                            products1 = new Create_Order_Model.ProductDetails();
                            products1.setAmount(amount);
                            products1.setTotal_cost(Double.parseDouble(singleadversimet.getAdvertsment().getPrice()) * amount);
                            if (lang.equals("ar")) {
                                products1.setName(singleadversimet.getAdvertsment().getTitle_ar());
                            } else {
                                products1.setName(singleadversimet.getAdvertsment().getTitle_en());

                            }
                            products1.setImage(singleadversimet.getAdvertsment().getImage());
                            productDetails.add(products1);
                            orderDetails1 = new Create_Order_Model.OrderDetails();
                            orderDetails1.setAmount(amount);
                            orderDetails1.setCost(Double.parseDouble(singleadversimet.getAdvertsment().getPrice()));
                            orderDetails1.setAdv_id(singleadversimet.getAdvertsment().getId());
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

                    add_order_model.setMarket_id(singleadversimet.getAdvertsment().getMarket().getId() + "");
                    Create_Order_Model.ProductDetails products1 = new Create_Order_Model.ProductDetails();
                    products1.setAmount(amount);
                    products1.setTotal_cost(Double.parseDouble(singleadversimet.getAdvertsment().getPrice()) * amount);
                    if (lang.equals("ar")) {
                        products1.setName(singleadversimet.getAdvertsment().getTitle_ar());
                    } else {
                        products1.setName(singleadversimet.getAdvertsment().getTitle_en());

                    }
                    products1.setImage(singleadversimet.getAdvertsment().getImage());
                    productDetails.add(products1);
                    Create_Order_Model.OrderDetails orderDetails1 = new Create_Order_Model.OrderDetails();
                    orderDetails1.setAmount(amount);
                    orderDetails1.setCost(Double.parseDouble(singleadversimet.getAdvertsment().getPrice()));
                    orderDetails1.setAdv_id(singleadversimet.getAdvertsment().getId());
                    order_details.add(orderDetails1);
                    add_order_model.setProductDetails(productDetails);
                    add_order_model.setDetails(order_details);
                }
                preferences.create_update_order(AccessoriesSparePartsDetailsActivity.this, add_order_model);


            }


        });
    }

    public void getsingleauction() {
        //   Common.CloseKeyBoard(homeActivity, edt_name);

        ProgressDialog dialog = Common.createProgressDialog(AccessoriesSparePartsDetailsActivity.this, getString(R.string.wait));
        dialog.setCancelable(false);
        dialog.show();
        // rec_sent.setVisibility(View.GONE);
        try {


            Api.getService(Tags.base_url)
                    .get_singleadversiment(search_id)
                    .enqueue(new Callback<MarksDataModel>() {
                        @Override
                        public void onResponse(Call<MarksDataModel> call, Response<MarksDataModel> response) {
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
                        public void onFailure(Call<MarksDataModel> call, Throwable t) {
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

    private void setdata(MarksDataModel body) {
        this.singleadversimet = body;
//        Log.e("lslslls",body.getAdvertsment().getLike().size()+"");
        binding.setModel(body.getAdvertsment());
        allList.clear();
        if (body.getAll() != null) {
            allList.addAll(body.getAll());
        }
        else {
            binding.tv2.setVisibility(View.GONE);
        }
        likeList.clear();
        if (body.getLike() != null) {
            likeList.addAll(body.getLike());
        }
        else {
            binding.tv1.setVisibility(View.GONE);
        }
        allAdversimentAdapter.notifyDataSetChanged();
        sameAdversimentAdapter.notifyDataSetChanged();
        productSlideAdapter = new ProductSlideAdapter(this, body.getAdvertsment().getImages());
        binding.tab.setupWithViewPager(binding.pager);
        binding.pager.setAdapter(productSlideAdapter);

    }

    private void navigateToSMapActivity() {
        Intent intent = new Intent(this, MapShowLocationActivity.class);

        intent.putExtra("lat", Double.parseDouble(singleadversimet.getAdvertsment().getMarket().getGoogle_lat()));
        intent.putExtra("lng", Double.parseDouble(singleadversimet.getAdvertsment().getMarket().getGoogle_long()));

        startActivity(intent);
        //  finish();
    }

    private void call(String s) {
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

    @Override
    public void back() {
        finish();
    }

    public void addtocart(MarksDataModel.All markDataInModel) {
        Create_Order_Model add_order_model = preferences.getUserOrder(AccessoriesSparePartsDetailsActivity.this);
        if (add_order_model != null) {
            if ((add_order_model.getMarket_id() + "").equals(markDataInModel.getMarket().getId() + "")) {
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

            add_order_model.setMarket_id(markDataInModel.getMarket().getId() + "");
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
        preferences.create_update_order(AccessoriesSparePartsDetailsActivity.this, add_order_model);


    }

    public void addtocart(MarksDataModel.Like markDataInModel) {
        Create_Order_Model add_order_model = preferences.getUserOrder(AccessoriesSparePartsDetailsActivity.this);
        if (add_order_model != null) {
            if ((add_order_model.getMarket_id() + "").equals(markDataInModel.getMarket().getId() + "")) {
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

            add_order_model.setMarket_id(markDataInModel.getMarket().getId() + "");
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
        preferences.create_update_order(AccessoriesSparePartsDetailsActivity.this, add_order_model);


    }

}
