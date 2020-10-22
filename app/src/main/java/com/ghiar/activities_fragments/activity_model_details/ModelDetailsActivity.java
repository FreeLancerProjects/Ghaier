package com.ghiar.activities_fragments.activity_model_details;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentStatePagerAdapter;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.widget.Toast;

import com.ghiar.R;
import com.ghiar.activities_fragments.activity_accessories_spare_details.AccessoriesSparePartsDetailsActivity;
import com.ghiar.activities_fragments.activity_model_details.fragments.Fragment_Accessories;
import com.ghiar.activities_fragments.activity_model_details.fragments.Fragment_Service;
import com.ghiar.activities_fragments.activity_model_details.fragments.Fragment_Spare_Parts;
import com.ghiar.activities_fragments.activity_service_center.ServiceCenterActivity;
import com.ghiar.activities_fragments.activity_service_center_detials.ServiceCenterDetialsActivity;
import com.ghiar.adapters.ViewPagerAdapter;
import com.ghiar.databinding.ActivityAboutAppBinding;
import com.ghiar.databinding.ActivityModelDetailsBinding;
import com.ghiar.interfaces.Listeners;
import com.ghiar.language.Language;
import com.ghiar.models.Create_Order_Model;
import com.ghiar.models.MarkDataInModel;
import com.ghiar.models.MarkModel;
import com.ghiar.preferences.Preferences;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import io.paperdb.Paper;

public class ModelDetailsActivity extends AppCompatActivity implements Listeners.BackListener {
    private ActivityModelDetailsBinding binding;
    private String lang;
    private ViewPagerAdapter adapter;
    private List<Fragment> fragmentList;
    private List<String> titles;
    public String markid;
    private MarkModel markModel;
    private Intent intent;
    private static final int REQUEST_PHONE_CALL = 1;
    private Preferences preferences;

    protected void attachBaseContext(Context newBase) {
        Paper.init(newBase);
        super.attachBaseContext(Language.updateResources(newBase, Paper.book().read("lang", Locale.getDefault().getLanguage())));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_model_details);
        getDataFromIntent();
        initView();

    }

    private void getDataFromIntent() {
        Intent intent = getIntent();
        if (intent.getStringExtra("search") != null) {
            markid = intent.getStringExtra("search");
            markModel = (MarkModel) intent.getSerializableExtra("data");
        }
    }


    private void initView() {
        fragmentList = new ArrayList<>();
        titles = new ArrayList<>();
        preferences = Preferences.getInstance();
        Paper.init(this);
        lang = Paper.book().read("lang", Locale.getDefault().getLanguage());
        binding.setBackListener(this);
        binding.setLang(lang);
        binding.setModel(markModel);

        fragmentList.add(Fragment_Spare_Parts.newInstance());
        fragmentList.add(Fragment_Accessories.newInstance());
        fragmentList.add(Fragment_Service.newInstance());

        titles.add(getString(R.string.spare_parts));
        titles.add(getString(R.string.accessories));
        titles.add(getString(R.string.services));

        binding.tab.setupWithViewPager(binding.pager);
        adapter = new ViewPagerAdapter(getSupportFragmentManager(), FragmentStatePagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        adapter.addFragments_Titles(fragmentList, titles);
        binding.pager.setAdapter(adapter);
        binding.pager.setOffscreenPageLimit(3);
//        binding.image.setOnClickListener(v -> {
//            Intent intent = new Intent(this, AccessoriesSparePartsDetailsActivity.class);
//            startActivity(intent);
//        });

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
        }
    }

    @Override
    public void back() {
        finish();
    }

    public void showservicecenter(MarkDataInModel a) {
        Intent intent = new Intent(ModelDetailsActivity.this, AccessoriesSparePartsDetailsActivity.class);
        intent.putExtra("search", a.getId() + "");
        startActivity(intent);
    }

    public void addtocart(MarkDataInModel markDataInModel) {
        Create_Order_Model add_order_model = preferences.getUserOrder(ModelDetailsActivity.this);
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
        preferences.create_update_order(ModelDetailsActivity.this, add_order_model);
        Toast.makeText(ModelDetailsActivity.this, getResources().getString(R.string.suc), Toast.LENGTH_LONG).show();


    }

    public void show(int id) {
        Intent intent = new Intent(ModelDetailsActivity.this, ServiceCenterDetialsActivity.class);
        intent.putExtra("search", id + "");
        startActivity(intent);
    }
}
