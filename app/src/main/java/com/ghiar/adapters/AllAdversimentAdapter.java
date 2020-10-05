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
import com.ghiar.activities_fragments.activity_home.HomeActivity;
import com.ghiar.databinding.AllproductRowBinding;
import com.ghiar.databinding.SameproductRowBinding;
import com.ghiar.models.MarkDataInModel;

import java.util.List;

public class AllAdversimentAdapter extends RecyclerView.Adapter<AllAdversimentAdapter.MarkViewholder> {

    private List<MarkDataInModel.All> list;
    private Context context;


    public AllAdversimentAdapter(Context context, List<MarkDataInModel.All> list) {
        this.list = list;
        this.context = context;

    }

    @NonNull
    @Override
    public MarkViewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        AllproductRowBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.allproduct_row, parent, false);
        return new MarkViewholder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull MarkViewholder holder, int position) {

        holder.binding.setModel(list.get(position));
        holder.binding.setLang("ar");

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
if(context instanceof AccessoriesSparePartsDetailsActivity){
    AccessoriesSparePartsDetailsActivity accessoriesSparePartsDetailsActivity=(AccessoriesSparePartsDetailsActivity)context;
    accessoriesSparePartsDetailsActivity.addtocart(list.get(position));
}
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    public class MarkViewholder extends RecyclerView.ViewHolder {
        AllproductRowBinding binding;

        public MarkViewholder(@NonNull AllproductRowBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
