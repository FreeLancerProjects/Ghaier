package com.ghiar.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.databinding.DataBindingUtil;

import com.ghiar.R;
import com.ghiar.databinding.ItemSliderBinding;
import com.ghiar.models.SliderModel;
import com.smarteist.autoimageslider.SliderViewAdapter;

import java.util.ArrayList;
import java.util.List;

public class SliderAdapter extends SliderViewAdapter<SliderAdapter.SliderAdapterVH> {

    private static final String TAG = "SliderAdapter";
    Context context;
    List<SliderModel> sliderModelList = new ArrayList<>();


    public SliderAdapter(Context context) {
        this.context = context;
    }

    @Override
    public SliderAdapterVH onCreateViewHolder(ViewGroup parent) {
        ItemSliderBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.item_slider, parent, false);
        return new SliderAdapterVH(binding);
    }

    @Override
    public void onBindViewHolder(SliderAdapterVH holder, int position) {
        holder.binding.setSliderData(sliderModelList.get(position));
    }

    @Override
    public int getCount() {
        return sliderModelList.size();
    }

    public void setSliderModelList(List<SliderModel> sliderModelList) {
        this.sliderModelList = sliderModelList;
        notifyDataSetChanged();
    }

    ;

    class SliderAdapterVH extends SliderViewAdapter.ViewHolder {

        ItemSliderBinding binding;

        public SliderAdapterVH(ItemSliderBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

}
