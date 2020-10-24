package com.ghiar.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.ghiar.R;
import com.ghiar.activities_fragments.activity_order_details.OrderDetailsActivity;
import com.ghiar.databinding.ProductDetailsRowBinding;
import com.ghiar.models.OrderModel;
import com.iarcuschin.simpleratingbar.SimpleRatingBar;

import java.util.List;
import java.util.Locale;

import io.paperdb.Paper;

public class ProductDetailsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private String lang;
    private List<OrderModel.Item> list;
    private Context context;
    private LayoutInflater inflater;

    public ProductDetailsAdapter(List<OrderModel.Item> list, Context context) {
        this.list = list;
        this.context = context;
        inflater = LayoutInflater.from(context);
        Paper.init(context);
        lang = Paper.book().read("lang", Locale.getDefault().getLanguage());

    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {


        ProductDetailsRowBinding binding = DataBindingUtil.inflate(inflater, R.layout.product_details_row, parent, false);
        return new MyHolder(binding);


    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        MyHolder myHolder = (MyHolder) holder;
        OrderModel.Item model = list.get(position);
        myHolder.binding.setLang(lang);
        myHolder.binding.setModel(model);

        myHolder.binding.simplarate.setOnRatingBarChangeListener(new SimpleRatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(SimpleRatingBar simpleRatingBar, float rating, boolean fromUser) {
                if (context instanceof OrderDetailsActivity) {
                    OrderDetailsActivity productDetailsActivity = (OrderDetailsActivity) context;
                    productDetailsActivity.makerate(list.get(myHolder.getLayoutPosition()).getId(), rating);
                }
            }
        });


    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyHolder extends RecyclerView.ViewHolder {
        public ProductDetailsRowBinding binding;

        public MyHolder(@NonNull ProductDetailsRowBinding binding) {
            super(binding.getRoot());
            this.binding = binding;

        }
    }


}
