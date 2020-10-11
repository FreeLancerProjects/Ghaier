package com.ghiar.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.ghiar.R;
import com.ghiar.activities_fragments.activity_accessories_spare_details.AccessoriesSparePartsDetailsActivity;
import com.ghiar.activities_fragments.activity_model_details.ModelDetailsActivity;
import com.ghiar.activities_fragments.activity_service_center.ServiceCenterActivity;
import com.ghiar.activities_fragments.activity_service_center_detials.ServiceCenterDetialsActivity;
import com.ghiar.databinding.SamecenterRowBinding;
import com.ghiar.databinding.SameproductRowBinding;
import com.ghiar.models.MarksDataModel;
import com.ghiar.models.ServiceCentersModel;

import java.util.List;

public class SameServiceCenterAdapter extends RecyclerView.Adapter<SameServiceCenterAdapter.MarkViewholder> {

    private List<ServiceCentersModel.Like> list;
    private Context context;


    public SameServiceCenterAdapter(Context context, List<ServiceCentersModel.Like> list) {
        this.list = list;
        this.context = context;

    }

    @NonNull
    @Override
    public MarkViewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        SamecenterRowBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.samecenter_row, parent, false);
        return new MarkViewholder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull MarkViewholder holder, int position) {

        holder.binding.setModel(list.get(position));
        holder.binding.setLang("ar");
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (context instanceof ServiceCenterDetialsActivity) {
                    ServiceCenterDetialsActivity serviceCenterActivity = (ServiceCenterDetialsActivity) context;
                    serviceCenterActivity.show(list.get(position).getId());
                }
            }
        });
        holder.binding.linearCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (context instanceof ServiceCenterDetialsActivity) {
                    ServiceCenterDetialsActivity serviceCenterActivity = (ServiceCenterDetialsActivity) context;
                    serviceCenterActivity.call(list.get(position).getPhone_code() + list.get(position).getPhone());
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    public class MarkViewholder extends RecyclerView.ViewHolder {
        SamecenterRowBinding binding;

        public MarkViewholder(@NonNull SamecenterRowBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
