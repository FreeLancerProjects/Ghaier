package com.ghiar.models;

import android.content.Context;
import android.text.TextUtils;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;
import androidx.databinding.ObservableField;

import com.ghiar.BR;
import com.ghiar.R;

import java.io.Serializable;

public class AddAuctionModel extends BaseObservable implements Serializable {

    private String english_title;
    private String arabic_title;
    private String price;
    private String min;
    private String quantity;
    private String date;
    private String english_detials;
    private String arabic_detials;
    private String time;


    public ObservableField<String> error_english_title = new ObservableField<>();
    public ObservableField<String> error_arabic_title = new ObservableField<>();

    public ObservableField<String> error_price = new ObservableField<>();

    public ObservableField<String> error_time = new ObservableField<>();
    public ObservableField<String> error_english_detials = new ObservableField<>();
    public ObservableField<String> error_arabic_detilas = new ObservableField<>();

    public ObservableField<String> error_min = new ObservableField<>();

    public ObservableField<String> error_date = new ObservableField<>();
    public ObservableField<String> error_quantity = new ObservableField<>();

    public AddAuctionModel() {
        this.english_title = "";
        this.arabic_title = "";
        this.price = "";
        this.time = "";
        this.arabic_detials = "";
        this.english_detials = "";
        this.min = "";
        this.quantity = "";
        this.date = "";

    }

    @Bindable
    public String getTime() {
        return time;
    }

    @Bindable
    public String getEnglish_title() {
        return english_title;
    }

    public void setEnglish_title(String english_title) {
        this.english_title = english_title;
        notifyPropertyChanged(BR.english_title);

    }

    @Bindable
    public String getArabic_title() {
        return arabic_title;
    }

    public void setArabic_title(String arabic_title) {
        this.arabic_title = arabic_title;
        notifyPropertyChanged(BR.arabic_title);

    }

    @Bindable
    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
        notifyPropertyChanged(BR.price);

    }


    public void setTime(String time) {
        this.time = time;
        notifyPropertyChanged(BR.phone_code);

    }

    public String getMin() {
        return min;
    }

    public void setMin(String min) {
        this.min = min;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getEnglish_detials() {
        return english_detials;
    }

    public void setEnglish_detials(String english_detials) {
        this.english_detials = english_detials;
    }

    public String getArabic_detials() {
        return arabic_detials;
    }

    public void setArabic_detials(String arabic_detials) {
        this.arabic_detials = arabic_detials;
    }

    public boolean isDataValid(Context context) {
        if (!TextUtils.isEmpty(time) &&

                !TextUtils.isEmpty(english_title) &&
                !TextUtils.isEmpty(arabic_title) &&
                !TextUtils.isEmpty(price) &&
                !TextUtils.isEmpty(english_detials) &&
                !TextUtils.isEmpty(arabic_detials) &&
                !TextUtils.isEmpty(min) && !TextUtils.isEmpty(quantity) &&
                !TextUtils.isEmpty(date)


        ) {
            error_arabic_title.set(null);
            error_english_title.set(null);
            error_price.set(null);
            error_time.set(null);
            error_arabic_detilas.set(null);
            error_english_detials.set(null);
            error_date.set(null);
            error_min.set(null);
            error_quantity.set(null);


            return true;
        } else {
            if (english_title.isEmpty()) {
                error_english_title.set(context.getString(R.string.field_req));
            } else {
                error_english_title.set(null);
            }
            if (arabic_title.isEmpty()) {
                error_arabic_title.set(context.getString(R.string.field_req));
            } else {
                error_arabic_title.set(null);
            }
            if (price.isEmpty()) {
                error_price.set(context.getString(R.string.field_req));
            } else {
                error_price.set(null);
            }


            if (time.isEmpty()) {
                error_time.set(context.getString(R.string.field_req));
            } else {
                error_time.set(null);
            }
            if (english_detials.isEmpty()) {
                error_english_detials.set(context.getString(R.string.field_req));
            } else {
                error_english_detials.set(null);
            }
            if (arabic_detials.isEmpty()) {
                error_arabic_detilas.set(context.getString(R.string.field_req));
            } else {
                error_arabic_detilas.set(null);
            }
            if (date.isEmpty()) {
                error_date.set(context.getString(R.string.field_req));
            } else {
                error_date.set(null);
            }
            if (min.isEmpty()) {
                error_min.set(context.getString(R.string.field_req));
            } else {
                error_min.set(null);
            }
            if (quantity.isEmpty()) {
                error_quantity.set(context.getString(R.string.field_req));
            } else {
                error_quantity.set(null);
            }
            return false;
        }
    }
}
