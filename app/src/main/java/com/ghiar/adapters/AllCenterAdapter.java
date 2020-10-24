package com.ghiar.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.ghiar.R;
import com.ghiar.activities_fragments.activity_accessories_spare_details.AccessoriesSparePartsDetailsActivity;
import com.ghiar.activities_fragments.activity_service_center_detials.ServiceCenterDetialsActivity;
import com.ghiar.databinding.AllcenterRowBinding;
import com.ghiar.databinding.AllproductRowBinding;
import com.ghiar.models.MarksDataModel;
import com.ghiar.models.ServiceCentersModel;
import com.ghiar.tags.Tags;

import java.util.List;

public class AllCenterAdapter extends RecyclerView.Adapter<AllCenterAdapter.MarkViewholder> {

    private List<ServiceCentersModel.All> list;
    private Context context;


    public AllCenterAdapter(Context context, List<ServiceCentersModel.All> list) {
        this.list = list;
        this.context = context;

    }

    @NonNull
    @Override
    public MarkViewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        AllcenterRowBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.allcenter_row, parent, false);
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
        holder.binding.imshare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                share(list.get(position).getId());
            }
        });

    }
    public void share(int id) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TEXT, Tags.base_url+"api/RedirectLink/market/"+id);
        context.startActivity(intent);
    }
    @Override
    public int getItemCount() {
        return list.size();
    }


    public class MarkViewholder extends RecyclerView.ViewHolder {
        AllcenterRowBinding binding;

        public MarkViewholder(@NonNull AllcenterRowBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
