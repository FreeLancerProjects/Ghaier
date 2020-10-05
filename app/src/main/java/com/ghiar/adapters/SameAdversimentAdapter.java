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
import com.ghiar.databinding.MarkdatainRowBinding;
import com.ghiar.databinding.SameproductRowBinding;
import com.ghiar.models.MarkDataInModel;

import java.util.List;

public class SameAdversimentAdapter extends RecyclerView.Adapter<SameAdversimentAdapter.MarkViewholder> {

   private List<MarkDataInModel.Like> list ;
    private Context context;




    public SameAdversimentAdapter(Context context, List<MarkDataInModel.Like> list) {
        this.list = list;
        this.context = context;

    }

    @NonNull
    @Override
    public MarkViewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
SameproductRowBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context),R.layout.sameproduct_row, parent, false);
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
        SameproductRowBinding binding;
        public MarkViewholder(@NonNull SameproductRowBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
