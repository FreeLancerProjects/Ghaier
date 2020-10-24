package com.ghiar.adapters;

import android.content.Context;
import android.graphics.PorterDuff;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;


import com.ghiar.R;
import com.ghiar.activities_fragments.activity_home.fragments.fragment_profile.fragments.FragmentMyOrder;
import com.ghiar.databinding.LoadMoreBinding;
import com.ghiar.databinding.OrderRowBinding;
import com.ghiar.models.OrderModel;
import com.ghiar.models.Order_Model;

import java.util.List;
import java.util.Locale;

import io.paperdb.Paper;

public class OrderAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<Order_Model.Data> orderlist;
    private Context context;
    private LayoutInflater inflater;
    private String lang;
    String title;
    private final int ITEM_DATA = 1;
    private final int LOAD = 2;
    private Fragment fragment;

    public OrderAdapter(List<Order_Model.Data> orderlist, Context context, Fragment fragment) {
        this.orderlist = orderlist;
        this.context = context;
        inflater = LayoutInflater.from(context);
        Paper.init(context);
        lang = Paper.book().read("lang", Locale.getDefault().getLanguage());
        this.fragment = fragment;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        if (viewType == ITEM_DATA) {
            OrderRowBinding binding = DataBindingUtil.inflate(inflater, R.layout.order_row, parent, false);
            return new EventHolder(binding);
        } else {
            LoadMoreBinding binding = DataBindingUtil.inflate(inflater, R.layout.load_more, parent, false);
            return new LoadHolder(binding);
        }

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof EventHolder) {
            EventHolder eventHolder = (EventHolder) holder;
            eventHolder.binding.setOrdermodel(orderlist.get(position));
            holder.itemView.setOnClickListener(view ->
            {
                Order_Model.Data model2 = orderlist.get(holder.getAdapterPosition());

                if (fragment instanceof FragmentMyOrder) {
                    FragmentMyOrder fragmentCurrentOrder = (FragmentMyOrder) fragment;
                    fragmentCurrentOrder.setItemData(model2);
                } else if (fragment instanceof FragmentMyOrder) {
                    FragmentMyOrder fragmentPreviousOrder = (FragmentMyOrder) fragment;
                    fragmentPreviousOrder.setItemData(model2);
                }
            });
/*
if(i==position){
    if(i!=0) {
        if (((EventHolder) holder).binding.expandLayout.isExpanded()) {
            ((EventHolder) holder).binding.tvTitle.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
            ((EventHolder) holder).binding.recView.setLayoutParams(new FrameLayout.LayoutParams(0, 0));
            ((EventHolder) holder).binding.expandLayout.collapse(true);
            ((EventHolder) holder).binding.expandLayout.setVisibility(View.GONE);



        }
        else {

          //  ((EventHolder) holder).binding.tvTitle.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
            ((EventHolder) holder).binding.recView.setLayoutParams(new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT));
            ((EventHolder) holder).binding.expandLayout.setVisibility(View.VISIBLE);

           ((EventHolder) holder).binding.expandLayout.expand(true);
        }
    }
    else {
        eventHolder.binding.tvTitle.setBackground(activity.getResources().getDrawable(R.drawable.linear_bg_green));

        ((EventHolder) holder).binding.tvTitle.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        ((EventHolder) holder).binding.recView.setLayoutParams(new FrameLayout.LayoutParams(0, 0));

    }
}
if(i!=position) {
    eventHolder.binding.tvTitle.setBackground(activity.getResources().getDrawable(R.drawable.linear_bg_white));
    ((EventHolder) holder).binding.tvTitle.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));

    ((EventHolder) holder).binding.recView.setLayoutParams(new FrameLayout.LayoutParams(0, 0));
    ((EventHolder) holder).binding.expandLayout.collapse(true);


}*/
        } else {
            LoadHolder loadHolder = (LoadHolder) holder;
            loadHolder.binding.progBar.setIndeterminate(true);
        }
    }

    @Override
    public int getItemCount() {
        return orderlist.size();
    }

    public class EventHolder extends RecyclerView.ViewHolder {
        public OrderRowBinding binding;

        public EventHolder(@NonNull OrderRowBinding binding) {
            super(binding.getRoot());
            this.binding = binding;

        }
    }

    public class LoadHolder extends RecyclerView.ViewHolder {
        private LoadMoreBinding binding;

        public LoadHolder(@NonNull LoadMoreBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
            binding.progBar.getIndeterminateDrawable().setColorFilter(ContextCompat.getColor(context, R.color.colorAccent), PorterDuff.Mode.SRC_IN);
        }

    }

    @Override
    public int getItemViewType(int position) {
        Order_Model.Data order_Model = orderlist.get(position);
        if (order_Model != null) {
            return ITEM_DATA;
        } else {
            return LOAD;
        }

    }

}
