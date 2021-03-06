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
import com.ghiar.activities_fragments.activity_home.HomeActivity;
import com.ghiar.databinding.AllproductRowBinding;
import com.ghiar.databinding.SameproductRowBinding;
import com.ghiar.models.MarkDataInModel;
import com.ghiar.models.MarksDataModel;
import com.ghiar.tags.Tags;

import java.util.List;

public class AllAdversimentAdapter extends RecyclerView.Adapter<AllAdversimentAdapter.MarkViewholder> {

    private List<MarksDataModel.All> list;
    private Context context;


    public AllAdversimentAdapter(Context context, List<MarksDataModel.All> list) {
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

        holder.binding.cardImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (context instanceof AccessoriesSparePartsDetailsActivity) {
                    AccessoriesSparePartsDetailsActivity accessoriesSparePartsDetailsActivity = (AccessoriesSparePartsDetailsActivity) context;
                    accessoriesSparePartsDetailsActivity.addtocart(list.get(position));
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void share(int id) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TEXT, Tags.base_url+"api/RedirectLink/product/"+id);
        context.startActivity(intent);
    }
    public class MarkViewholder extends RecyclerView.ViewHolder {
        AllproductRowBinding binding;

        public MarkViewholder(@NonNull AllproductRowBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
