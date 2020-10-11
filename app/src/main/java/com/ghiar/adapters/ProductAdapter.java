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
import com.ghiar.activities_fragments.activity_home.HomeActivity;
import com.ghiar.activities_fragments.activity_model_details.ModelDetailsActivity;
import com.ghiar.databinding.ItemProductBinding;
import com.ghiar.databinding.ItemServiceBinding;
import com.ghiar.models.ProductModel;
import com.ghiar.models.ServiceModel;

import java.util.ArrayList;
import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductAdapterVH> {

   private List<ProductModel> list ;
   private Context context;
    private Fragment fragment;


    public ProductAdapter(Context context, Fragment fragment,List<ProductModel> list) {
        this.context = context;
        this.fragment = fragment;
        this.list=list;

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
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (context instanceof HomeActivity) {
                    HomeActivity activity = (HomeActivity) context;
                    activity.showservicecenter(list.get(position));
                }
            }
        });
        holder.binding.cardImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (context instanceof ModelDetailsActivity) {
                    HomeActivity activity = (HomeActivity) context;
                    activity.addtocart(list.get(position));
                }
            }
        });


    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    public class ProductAdapterVH extends RecyclerView.ViewHolder {
        ItemProductBinding binding;

        public ProductAdapterVH(@NonNull ItemProductBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
