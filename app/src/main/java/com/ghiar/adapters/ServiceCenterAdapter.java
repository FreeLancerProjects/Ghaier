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
import com.ghiar.databinding.ItemServiceCenterBinding;
import com.ghiar.models.ProductModel;
import com.ghiar.models.ServiceCenterModel;
import com.ghiar.preferences.Preferences;

import java.util.ArrayList;
import java.util.List;

public class ServiceCenterAdapter extends RecyclerView.Adapter<ServiceCenterAdapter.ServiceCenterAdapterVH> {

    private List<ServiceCenterModel> list;
    private Context context;


    public ServiceCenterAdapter(Context context, List<ServiceCenterModel> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public ServiceCenterAdapterVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemServiceCenterBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.item_service_center, parent, false);
        return new ServiceCenterAdapterVH(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ServiceCenterAdapterVH holder, int position) {

        holder.binding.setServiceCenter(list.get(position));
        holder.binding.setLang("ar");
        holder.binding.ratingBar.setRating(Float.parseFloat(list.get(position).getRate()));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }



    public class ServiceCenterAdapterVH extends RecyclerView.ViewHolder {
        ItemServiceCenterBinding binding;

        public ServiceCenterAdapterVH(@NonNull ItemServiceCenterBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
