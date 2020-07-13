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

import com.ghiar.R;
import com.ghiar.activities_fragments.activity_cart.CartActivity;
import com.ghiar.databinding.FragmentCartPurchasesBinding;
import com.ghiar.interfaces.Listeners;
import com.ghiar.models.AddOrderModel;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class Fragment_Cart_Purchases extends Fragment{
    private static final String TAG = "data";
    private CartActivity activity;
    private FragmentCartPurchasesBinding binding;
    private AddOrderModel addOrderModel;


    public static Fragment_Cart_Purchases newInstance(AddOrderModel addOrderModel)
    {
        Bundle bundle = new Bundle();
        bundle.putSerializable(TAG,addOrderModel);
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
        activity = (CartActivity) getActivity();





    }


    public void setModel(AddOrderModel addOrderModel) {
        this.addOrderModel = addOrderModel;
    }
}
