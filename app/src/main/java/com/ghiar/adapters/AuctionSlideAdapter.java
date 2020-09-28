package com.ghiar.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.databinding.DataBindingUtil;

import com.ghiar.R;
import com.ghiar.databinding.AuctionSliderBinding;
import com.ghiar.databinding.ItemSliderBinding;
import com.ghiar.models.SingleAuctionModel;
import com.ghiar.models.SliderModel;
import com.smarteist.autoimageslider.SliderViewAdapter;

import java.util.List;

public class AuctionSlideAdapter extends SliderViewAdapter<AuctionSlideAdapter.SliderAdapter> {

    Context context;
  private   List<SingleAuctionModel.Images> sliderModelList ;


    public AuctionSlideAdapter(Context context, List<SingleAuctionModel.Images>sliderModelList) {
        this.context = context;
        this.sliderModelList=sliderModelList;
    }

    @Override
    public SliderAdapter onCreateViewHolder(ViewGroup parent) {
        AuctionSliderBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.auction_slider, parent, false);
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

        AuctionSliderBinding binding;

        public SliderAdapter(AuctionSliderBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

}
