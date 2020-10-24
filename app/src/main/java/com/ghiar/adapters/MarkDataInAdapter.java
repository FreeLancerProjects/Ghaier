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
import com.ghiar.activities_fragments.activity_home.HomeActivity;
import com.ghiar.activities_fragments.activity_model_details.ModelDetailsActivity;
import com.ghiar.activities_fragments.activity_search.SearchActivity;
import com.ghiar.databinding.ItemDrawerMarkBinding;
import com.ghiar.databinding.MarkdatainRowBinding;
import com.ghiar.models.MarkDataInModel;
import com.ghiar.models.MarkModel;
import com.ghiar.tags.Tags;

import java.util.List;

public class MarkDataInAdapter extends RecyclerView.Adapter<MarkDataInAdapter.MarkViewholder> {

    private List<MarkDataInModel> list;
    private Context context;


    public MarkDataInAdapter(Context context, List<MarkDataInModel> list) {
        this.list = list;
        this.context = context;

    }

    @NonNull
    @Override
    public MarkViewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        MarkdatainRowBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.markdatain_row, parent, false);
        return new MarkViewholder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull MarkViewholder holder, int position) {

        holder.binding.setModel(list.get(position));
        holder.binding.setLang("ar");

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (context instanceof ModelDetailsActivity) {
                    ModelDetailsActivity activity = (ModelDetailsActivity) context;
                    activity.showservicecenter(list.get(position));
                }
            }
        });
        holder.binding.flAddToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (context instanceof ModelDetailsActivity) {
                    ModelDetailsActivity activity = (ModelDetailsActivity) context;
                    activity.addtocart(list.get(position));
                }
                else   if (context instanceof SearchActivity) {
                    SearchActivity activity = (SearchActivity) context;
                    activity.addtocart(list.get(position));
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
        intent.putExtra(Intent.EXTRA_TEXT, Tags.base_url+"api/RedirectLink/product/"+id);
        context.startActivity(intent);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    public class MarkViewholder extends RecyclerView.ViewHolder {
        MarkdatainRowBinding binding;

        public MarkViewholder(@NonNull MarkdatainRowBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
