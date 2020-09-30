package com.ghiar.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.ghiar.R;
import com.ghiar.activities_fragments.activity_home.HomeActivity;
import com.ghiar.databinding.ItemDrawerMarkBinding;
import com.ghiar.databinding.MarkdatainRowBinding;
import com.ghiar.models.MarkDataInModel;
import com.ghiar.models.MarkModel;

import java.util.List;

public class MarkDataInAdapter extends RecyclerView.Adapter<MarkDataInAdapter.MarkViewholder> {

   private List<MarkDataInModel> list ;
    private Context context;




    public MarkDataInAdapter(Context context, List<MarkDataInModel> list) {
        this.list = list;
        this.context = context;

    }

    @NonNull
    @Override
    public MarkViewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
MarkdatainRowBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context),R.layout.markdatain_row, parent, false);
        return new MarkViewholder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull MarkViewholder holder, int position) {

    holder.binding.setModel(list.get(position));
    holder.binding.setLang("ar");

holder.itemView.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        if(context instanceof HomeActivity){
            HomeActivity activity=(HomeActivity)context;
            activity.showservicecenter(holder.getLayoutPosition());
        }
    }
});
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
