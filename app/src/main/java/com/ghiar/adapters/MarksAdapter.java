package com.ghiar.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import androidx.databinding.DataBindingUtil;

import com.ghiar.R;
import com.ghiar.databinding.SpinnerCityRowBinding;
import com.ghiar.models.CityDataModel;
import com.ghiar.models.MarkModel;

import java.util.List;

import io.paperdb.Paper;

public class MarksAdapter extends BaseAdapter {
    private List<MarkModel> data;
    private Context context;
    private String lang;

    public MarksAdapter(List<MarkModel> data, Context context) {
        this.data = data;
        this.context = context;
        lang = Paper.book().read("lang","ar");

    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int i) {
        return i;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        @SuppressLint("ViewHolder") SpinnerCityRowBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.spinner_city_row,viewGroup,false);

        if (lang.equals("ar")){
            binding.setData(data.get(i).getTitle_ar());

        }else {
            binding.setData(data.get(i).getTitle_en());

        }

        return binding.getRoot();
    }
}
