package com.ghiar.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.databinding.DataBindingUtil;

import com.ghiar.R;
import com.ghiar.databinding.AuctionSliderBinding;
import com.ghiar.databinding.ProductSliderBinding;
import com.ghiar.models.MarkDataInModel;
import com.ghiar.models.SingleAuctionModel;
import com.smarteist.autoimageslider.SliderViewAdapter;

import java.util.List;

public class ProductSlideAdapter extends SliderViewAdapter<ProductSlideAdapter.SliderAdapter> {

    Context context;
  private   List<MarkDataInModel.Images> sliderModelList ;


    public ProductSlideAdapter(Context context, List<MarkDataInModel.Images>sliderModelList) {
        this.context = context;
        this.sliderModelList=sliderModelList;
    }

    @Override
    public SliderAdapter onCreateViewHolder(ViewGroup parent) {
        ProductSliderBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.product_slider, parent, false);
        return new SliderAdapter(binding);
    }

    @Override
    public void onBindViewHolder(SliderAdapter holder, int position) {
        holder.binding.setSliderData(sliderModelList.get(position));
    }

    @Override
    public int getCount() {
        return sliderModelList.size();
    }




    class SliderAdapter extends SliderViewAdapter.ViewHolder {

        ProductSliderBinding binding;

        public SliderAdapter(ProductSliderBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

}
