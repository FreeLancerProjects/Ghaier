package com.ghiar.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.ghiar.R;
import com.ghiar.activities_fragments.activity_model_details.ModelDetailsActivity;
import com.ghiar.activities_fragments.activity_service_center.ServiceCenterActivity;
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
        holder.binding.linearCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(context instanceof ServiceCenterActivity){
                    ServiceCenterActivity serviceCenterActivity=(ServiceCenterActivity)context;
                    serviceCenterActivity.call(list.get(position).getPhone_code()+list.get(position).getPhone());
                }
                else {
                    ModelDetailsActivity modelDetailsActivity=(ModelDetailsActivity)context;
                    modelDetailsActivity.call(list.get(position).getPhone_code()+list.get(position).getPhone());

                }
            }
        });
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
