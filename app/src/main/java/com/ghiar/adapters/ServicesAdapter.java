package com.ghiar.adapters;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.ghiar.R;
import com.ghiar.activities_fragments.activity_service_center.ServiceCenterActivity;
import com.ghiar.databinding.ItemServiceBinding;
import com.ghiar.models.ServiceModel;
import com.ghiar.preferences.Preferences;

import java.util.ArrayList;
import java.util.List;

public class ServicesAdapter extends RecyclerView.Adapter<ServicesAdapter.ServiceViewholder> {

    private static final String TAG = "ServicesAdapter";
    List<ServiceModel> list = new ArrayList<>();
    Context context;
    Fragment fragment;
    AppCompatActivity activity;



    public ServicesAdapter(Context context, Fragment fragment) {
        this.list = list;
        this.context = context;
        this.fragment = fragment;

    }

    @NonNull
    @Override
    public ServiceViewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemServiceBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context),R.layout.item_service, parent, false);
        return new ServiceViewholder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ServiceViewholder holder, int position) {

    holder.binding.setService(list.get(position));
    holder.binding.setLang("ar");
    holder.binding.image.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(context, ServiceCenterActivity.class);
            intent.putExtra("serviceId",list.get(position).getId());
            context.startActivity(intent);
        }
    });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void setList(List<ServiceModel> list) {
        this.list.clear();
        this.list = list;
        notifyDataSetChanged();
    }

    public class ServiceViewholder extends RecyclerView.ViewHolder {
        ItemServiceBinding binding;
        public ServiceViewholder(@NonNull ItemServiceBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
