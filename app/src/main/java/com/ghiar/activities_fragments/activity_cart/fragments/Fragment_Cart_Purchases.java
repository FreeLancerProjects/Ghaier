package com.ghiar.activities_fragments.activity_cart.fragments;

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

        cartAdapter = new CartAdapter(dataList, activity);

        binding.recView.setLayoutManager(new LinearLayoutManager(activity));
        binding.recView.setAdapter(cartAdapter);


        getorders();
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
}
