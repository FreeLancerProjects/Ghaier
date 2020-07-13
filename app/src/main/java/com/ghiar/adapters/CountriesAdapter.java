package com.ghiar.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.ghiar.R;
import com.ghiar.activities_fragments.activity_cart.CartActivity;
import com.ghiar.activities_fragments.activity_cart.fragments.Fragment_Address;
import com.ghiar.activities_fragments.activity_login.LoginActivity;
import com.ghiar.databinding.CountriesRowBinding;
import com.ghiar.models.CountryModel;

import java.util.List;

public class CountriesAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<CountryModel> list;
    private Context context;
    private LayoutInflater inflater;
    private AppCompatActivity activity;
    private Fragment fragment;
    public CountriesAdapter(List<CountryModel> list, Context context, Fragment fragment) {
        this.list = list;
        this.context = context;
        inflater = LayoutInflater.from(context);
        activity = (AppCompatActivity) context;
        this.fragment = fragment;


    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {


        CountriesRowBinding binding = DataBindingUtil.inflate(inflater, R.layout.countries_row, parent, false);
        return new MyHolder(binding);


    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        MyHolder myHolder = (MyHolder) holder;
        myHolder.binding.setModel(list.get(position));

        myHolder.itemView.setOnClickListener(view -> {
            if (activity instanceof LoginActivity){
                LoginActivity loginActivity = (LoginActivity) activity;
                loginActivity.setItemData(list.get(myHolder.getAdapterPosition()));
            }else if (activity instanceof CartActivity&&fragment!=null){
                if (fragment instanceof Fragment_Address){
                    Fragment_Address fragment_address = (Fragment_Address) fragment;
                    fragment_address.setItemData(list.get(myHolder.getAdapterPosition()));
                }
            }

        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyHolder extends RecyclerView.ViewHolder {
        public CountriesRowBinding binding;

        public MyHolder(@NonNull CountriesRowBinding binding) {
            super(binding.getRoot());
            this.binding = binding;

        }
    }




}
