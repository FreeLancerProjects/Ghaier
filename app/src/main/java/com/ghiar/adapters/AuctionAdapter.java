package com.ghiar.adapters;

import android.content.Context;
import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.ghiar.R;
import com.ghiar.databinding.AuctionRowBinding;
import com.ghiar.databinding.ItemDrawerMarkBinding;
import com.ghiar.models.MarkModel;
import com.ghiar.models.SingleAuctionModel;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Timer;
import java.util.concurrent.TimeUnit;

public class AuctionAdapter extends RecyclerView.Adapter<AuctionAdapter.MarkViewholder> {

    private List<SingleAuctionModel> list;
    private Context context;


    public AuctionAdapter(Context context, List<SingleAuctionModel> list) {
        this.list = list;
        this.context = context;

    }

    @NonNull
    @Override
    public MarkViewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        AuctionRowBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.auction_row, parent, false);
        return new MarkViewholder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull MarkViewholder holder, int position) {

        holder.binding.setAuctionmodel(list.get(position));
        long enddate = Long.parseLong(list.get(position).getEnd_date()) * 1000;
        long date = System.currentTimeMillis();
        long diffrent = (enddate - date);
        CountDownTimer cdt = new CountDownTimer(diffrent, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                long days = TimeUnit.MILLISECONDS.toDays(millisUntilFinished);
                millisUntilFinished -= TimeUnit.DAYS.toMillis(days);

                long hours = TimeUnit.MILLISECONDS.toHours(millisUntilFinished);
                millisUntilFinished -= TimeUnit.HOURS.toMillis(hours);

                long minutes = TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished);
                millisUntilFinished -= TimeUnit.MINUTES.toMillis(minutes);

                long seconds = TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished);
                holder.binding.tvseconed.setText(seconds + "");
                holder.binding.tvminute.setText(minutes + "");
                holder.binding.tvhour.setText(hours + "");
                holder.binding.tvday.setText(days + "");


            }

            @Override
            public void onFinish() {
            }
        };
        cdt.start();

    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    public class MarkViewholder extends RecyclerView.ViewHolder {
        AuctionRowBinding binding;

        public MarkViewholder(@NonNull AuctionRowBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
