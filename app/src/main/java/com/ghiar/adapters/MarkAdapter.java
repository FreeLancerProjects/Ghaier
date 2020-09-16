package com.ghiar.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.ghiar.R;
import com.ghiar.databinding.ItemDrawerMarkBinding;
import com.ghiar.models.MarkModel;

import java.util.ArrayList;
import java.util.List;

public class MarkAdapter extends RecyclerView.Adapter<MarkAdapter.MarkViewholder> {

   private List<MarkModel> list ;
    private Context context;




    public MarkAdapter(Context context, List<MarkModel> list) {
        this.list = list;
        this.context = context;

    }

    @NonNull
    @Override
    public MarkViewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemDrawerMarkBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context),R.layout.item_drawer_mark, parent, false);
        return new MarkViewholder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull MarkViewholder holder, int position) {

    holder.binding.setMark(list.get(position));
    holder.binding.setLang("ar");


    }

    @Override
    public int getItemCount() {
        return list.size();
    }



    public class MarkViewholder extends RecyclerView.ViewHolder {
        ItemDrawerMarkBinding binding;
        public MarkViewholder(@NonNull ItemDrawerMarkBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
