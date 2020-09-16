package com.ghiar.models;

import android.content.Context;
import android.text.TextUtils;
import android.widget.Toast;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;
import androidx.databinding.ObservableField;

import com.ghiar.BR;
import com.ghiar.R;

import java.io.Serializable;

public class ReguiredModel extends BaseObservable implements Serializable {

    private String english_title;
    private String arabic_title;

    private String model;

    private String amount;

    private String brand_id;
    private String required_type;


    public ObservableField<String> error_english_title = new ObservableField<>();
    public ObservableField<String> error_arabic_title = new ObservableField<>();

    public ObservableField<String> error_model = new ObservableField<>();

    public ObservableField<String> error_amount = new ObservableField<>();


    public ReguiredModel() {
        this.english_title = "";
        this.arabic_title = "";

        this.model = "";
        this.amount = "";

        this.brand_id = "";
        this.required_type = "";

    }

    @Bindable
    public String getAmount() {
        return amount;
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
    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
        notifyPropertyChanged(BR.model);

    }



    @Bindable

    public String getRequired_type() {
        return required_type;
    }

    public void setRequired_type(String required_type) {
        this.required_type = required_type;
    }


    public void setAmount(String amount) {
        this.amount = amount;
        notifyPropertyChanged(BR.phone_code);

    }







    @Bindable
    public String getBrand_id() {
        return brand_id;
    }



    public void setBrand_id(String brand_id) {
        this.brand_id = brand_id;
    }



    public boolean isDataValid(Context context)
    {
        if (!TextUtils.isEmpty(amount)&&

                !TextUtils.isEmpty(english_title)&&
                !TextUtils.isEmpty(arabic_title)&&
                !TextUtils.isEmpty(model)&&
!TextUtils.isEmpty(required_type)&&
                !TextUtils.isEmpty(brand_id)

        )
        {
            error_arabic_title.set(null);
            error_english_title.set(null);
            error_model.set(null);
            error_amount.set(null);

            return true;
        }else
        {
            if (english_title.isEmpty())
            {
                error_english_title.set(context.getString(R.string.field_req));
            }else
            {
                error_english_title.set(null);
            }
            if (arabic_title.isEmpty())
            {
                error_arabic_title.set(context.getString(R.string.field_req));
            }else
            {
                error_arabic_title.set(null);
            }

            if (model.isEmpty())
            {
                error_model.set(context.getString(R.string.field_req));
            }else
            {
                error_model.set(null);
            }


            if (amount.isEmpty())
            {
                error_amount.set(context.getString(R.string.field_req));
            }else
            {
                error_amount.set(null);
            }



//            if (brand_id.isEmpty())
//            {
//                Toast.makeText(context, context.getString(R.string.ch_brand), Toast.LENGTH_SHORT).show();
//            }
//            if (required_type.isEmpty())
//            {
//                Toast.makeText(context, context.getString(R.string.ch_type), Toast.LENGTH_SHORT).show();
//            }



            return false;
        }
    }
}
