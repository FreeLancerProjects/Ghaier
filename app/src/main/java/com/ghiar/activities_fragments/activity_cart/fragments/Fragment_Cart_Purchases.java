package com.ghiar.activities_fragments.activity_cart.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.ghiar.R;
import com.ghiar.activities_fragments.activity_cart.CartActivity;
import com.ghiar.activities_fragments.activity_home.HomeActivity;
import com.ghiar.adapters.CartAdapter;
import com.ghiar.databinding.FragmentCartPurchasesBinding;
import com.ghiar.interfaces.Listeners;
import com.ghiar.models.AddOrderModel;
import com.ghiar.models.Create_Order_Model;
import com.ghiar.preferences.Preferences;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class Fragment_Cart_Purchases extends Fragment {
    private static final String TAG = "data";
    private CartActivity activity;
    private FragmentCartPurchasesBinding binding;
    private AddOrderModel addOrderModel;
    private CartAdapter cartAdapter;
    private List<Create_Order_Model.ProductDetails> dataList;
    private List<Create_Order_Model.OrderDetails> orderDetailsList;
    private Preferences preferences;
    public double total;

    public static Fragment_Cart_Purchases newInstance(AddOrderModel addOrderModel) {
        Bundle bundle = new Bundle();
        bundle.putSerializable(TAG, addOrderModel);
        Fragment_Cart_Purchases fragment_cartPurchases = new Fragment_Cart_Purchases();
        fragment_cartPurchases.setArguments(bundle);
        return fragment_cartPurchases;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_cart_purchases, container, false);

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView();

    }


    private void initView() {
        dataList = new ArrayList<>();
        orderDetailsList = new ArrayList<>();
        preferences = Preferences.getInstance();
        activity = (CartActivity) getActivity();

        cartAdapter = new CartAdapter(dataList, activity, this);

        binding.recView.setLayoutManager(new LinearLayoutManager(activity));
        binding.recView.setAdapter(cartAdapter);

        binding.btnShopNow.setOnClickListener(v -> {
            navigateToHomeActivity();

        });

        getorders();
    }

    private void navigateToHomeActivity() {
        Intent intent = new Intent(getActivity(), HomeActivity.class);
        startActivity(intent);
        getActivity().finish();
    }

    private void getorders() {
        if (preferences.getUserOrder(activity) != null) {
            binding.llEmptyCart.setVisibility(View.GONE);

            dataList.clear();
            orderDetailsList.clear();
            dataList.addAll(preferences.getUserOrder(activity).getProductDetails());
            orderDetailsList.addAll(preferences.getUserOrder(activity).getDetails());

            cartAdapter.notifyDataSetChanged();
            gettotal();

        } else {
            // binding.consTotal.setVisibility(View.GONE);
            binding.llEmptyCart.setVisibility(View.VISIBLE);
        }
    }

    private void gettotal() {

        total = 0;
        for (int i = 0; i < dataList.size(); i++) {
            total += dataList.get(i).getTotal_cost();

        }

        //  binding.tvquatity.setText(total + "");
    }

    public void setModel(AddOrderModel addOrderModel) {
        this.addOrderModel = addOrderModel;
    }

    public void additem(int layoutPosition) {
        Create_Order_Model.OrderDetails products1 = orderDetailsList.get(layoutPosition);
        products1.setCost((products1.getCost() / products1.getAmount()) * (products1.getAmount() + 1));
        products1.setAmount(products1.getAmount() + 1);
        orderDetailsList.remove(layoutPosition);
        orderDetailsList.add(layoutPosition, products1);
        Create_Order_Model add_order_model = preferences.getUserOrder(activity);
        add_order_model.setDetails(orderDetailsList);
        preferences.create_update_order(activity, add_order_model);
        Create_Order_Model.ProductDetails products2 = dataList.get(layoutPosition);
        products2.setTotal_cost((products2.getTotal_cost() / products2.getAmount()) * (products2.getAmount() + 1));
        products2.setAmount(products2.getAmount() + 1);
        dataList.remove(layoutPosition);
        dataList.add(layoutPosition, products2);
        add_order_model.setProductDetails(dataList);
        preferences.create_update_order(activity, add_order_model);
        cartAdapter.notifyDataSetChanged();
        gettotal();
    }

    public void minusitem(int layoutPosition) {

        Create_Order_Model.OrderDetails products1 = orderDetailsList.get(layoutPosition);
        Create_Order_Model.ProductDetails products2 = dataList.get(layoutPosition);

        if (products1.getAmount() > 1) {
            products1.setCost((products1.getCost() / products1.getAmount()) * (products1.getAmount() - 1));
            products1.setAmount(products1.getAmount() - 1);
            orderDetailsList.remove(layoutPosition);
            orderDetailsList.add(layoutPosition, products1);
            Create_Order_Model add_order_model = preferences.getUserOrder(activity);
            add_order_model.setDetails(orderDetailsList);
            products2.setTotal_cost((products2.getTotal_cost() / products2.getAmount()) * (products2.getAmount() - 1));
            products2.setAmount(products2.getAmount() - 1);
            dataList.remove(layoutPosition);
            dataList.add(layoutPosition, products2);
            add_order_model.setProductDetails(dataList);
            preferences.create_update_order(activity, add_order_model);
            cartAdapter.notifyDataSetChanged();
            gettotal();

        }
    }

    public void deleteItem(int adapterPosition) {
        dataList.remove(adapterPosition);
        cartAdapter.notifyDataSetChanged();
        if (dataList.size() == 0) {
            preferences.create_update_order(activity, null);
            getorders();
        }
    }
}
