package com.ghiar.activities_fragments.activity_cart.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;


import com.ghiar.R;
import com.ghiar.activities_fragments.activity_cart.CartActivity;
import com.ghiar.databinding.FragmentPaymentTypeBinding;
import com.ghiar.models.AddOrderModel;
import com.ghiar.models.Create_Order_Model;
import com.ghiar.preferences.Preferences;

public class Fragment_Payment_Type extends Fragment {
    private static final String TAG = "data";
    private CartActivity activity;
    private FragmentPaymentTypeBinding binding;
    private AddOrderModel addOrderModel;
    private Preferences preferences;

    public static Fragment_Payment_Type newInstance(AddOrderModel addOrderModel) {
        Bundle bundle = new Bundle();
        bundle.putSerializable(TAG, addOrderModel);
        Fragment_Payment_Type fragment_payment_type = new Fragment_Payment_Type();
        fragment_payment_type.setArguments(bundle);
        return fragment_payment_type;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_payment_type, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView();


    }


    private void initView() {
        activity = (CartActivity) getActivity();

binding.tv.setText(activity.create_order_model.getTotal_cost());
        binding.cardCash.setOnClickListener(v -> {
            binding.cardCash.setCardBackgroundColor(ContextCompat.getColor(activity, R.color.colorPrimary));
            binding.tvCash.setTextColor(ContextCompat.getColor(activity, R.color.white));
            binding.imageCash.setColorFilter(ContextCompat.getColor(activity, R.color.white));

            binding.cardPaypal.setCardBackgroundColor(ContextCompat.getColor(activity, R.color.white));
            binding.cardVisa.setCardBackgroundColor(ContextCompat.getColor(activity, R.color.white));

            addOrderModel.setPayment_type("cash");
            activity.updateAddOrderModel(addOrderModel);

        });

        binding.cardPaypal.setOnClickListener(v -> {
            binding.cardCash.setCardBackgroundColor(ContextCompat.getColor(activity, R.color.white));
            binding.tvCash.setTextColor(ContextCompat.getColor(activity, R.color.colorPrimary));
            binding.imageCash.setColorFilter(ContextCompat.getColor(activity, R.color.colorPrimary));

            binding.cardPaypal.setCardBackgroundColor(ContextCompat.getColor(activity, R.color.colorPrimary));
            binding.cardVisa.setCardBackgroundColor(ContextCompat.getColor(activity, R.color.white));

            addOrderModel.setPayment_type("paypal");
            activity.updateAddOrderModel(addOrderModel);

        });

        binding.cardVisa.setOnClickListener(v -> {
            binding.cardCash.setCardBackgroundColor(ContextCompat.getColor(activity, R.color.white));
            binding.tvCash.setTextColor(ContextCompat.getColor(activity, R.color.colorPrimary));
            binding.imageCash.setColorFilter(ContextCompat.getColor(activity, R.color.colorPrimary));

            binding.cardPaypal.setCardBackgroundColor(ContextCompat.getColor(activity, R.color.white));
            binding.cardVisa.setCardBackgroundColor(ContextCompat.getColor(activity, R.color.colorPrimary));

            addOrderModel.setPayment_type("visa");
            activity.updateAddOrderModel(addOrderModel);
        });

        Bundle bundle = getArguments();
        if (bundle != null) {
            addOrderModel = (AddOrderModel) bundle.getSerializable(TAG);
        }

        if (addOrderModel != null) {
            addOrderModel.setPayment_type("cash");
        }
    }


    public void setModel(AddOrderModel model) {
        this.addOrderModel = model;
    }


}
