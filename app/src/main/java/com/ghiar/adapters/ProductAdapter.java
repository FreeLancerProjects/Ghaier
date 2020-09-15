package com.ghiar.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.ghiar.R;
import com.ghiar.databinding.ItemProductBinding;
import com.ghiar.databinding.ItemServiceBinding;
import com.ghiar.models.ProductModel;
import com.ghiar.models.ServiceModel;

import java.util.ArrayList;
import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductAdapterVH> {

    private static final String TAG = "ProductAdapter";
    List<ProductModel> list = new ArrayList<>();
    Context context;
    Fragment fragment;
    AppCompatActivity activity;


    public ProductAdapter(Context context, Fragment fragment) {
        this.context = context;
        this.fragment = fragment;

    }

    @NonNull
    @Override
    public ProductAdapterVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemProductBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.item_product, parent, false);
        return new ProductAdapterVH(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductAdapterVH holder, int position) {

        holder.binding.setProduct(list.get(position));
        holder.binding.setLang("ar");
        holder.binding.ratingBar.setRating((Float.parseFloat(list.get(position).getRate())));


        Log.e(TAG, list.get(position).getTitle_ar());

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void setList(List<ProductModel> list) {
        this.list.clear();
        this.list = list;
        notifyDataSetChanged();
    }

    public class ProductAdapterVH extends RecyclerView.ViewHolder {
        ItemProductBinding binding;

        public ProductAdapterVH(@NonNull ItemProductBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
