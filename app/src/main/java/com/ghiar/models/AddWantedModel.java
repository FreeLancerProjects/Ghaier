package com.ghiar.models;

import android.content.Context;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Patterns;
import android.widget.Toast;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;
import androidx.databinding.ObservableField;
import androidx.databinding.library.baseAdapters.BR;

import com.ghiar.R;

import java.io.Serializable;

public class AddWantedModel extends BaseObservable implements Serializable {

    private Uri image;
    private String title_ar;
    private String title_en;
    private String details_ar;
    private String details_en;
    private String model_id;
    private String mark_id;
    private String amount;
    private String status;
    private String type;
    public ObservableField<String> error_title_ar = new ObservableField<>();
    public ObservableField<String> error_title_en = new ObservableField<>();
    public ObservableField<String> error_details_ar = new ObservableField<>();
    public ObservableField<String> error_details_en = new ObservableField<>();


    public boolean isDataValid(Context context) {


        if (!title_ar.isEmpty() &&
                !title_en.isEmpty() &&
                !details_ar.isEmpty() &&
                !details_en.isEmpty()

        ) {
            error_title_ar.set(null);
            error_title_en.set(null);
            error_details_ar.set(null);
            error_details_en.set(null);

            return true;
        } else {

            if (title_ar.isEmpty()) {
                error_title_ar.set(context.getString(R.string.field_required));
            }else {
                error_title_ar.set(null);
            }

            if (title_en.isEmpty()) {
                error_title_en.set(context.getString(R.string.field_required));
            }else {
                error_title_en.set(null);
            }

            if (details_ar.isEmpty()) {
                error_details_ar.set(context.getString(R.string.field_required));
            }else {
                error_details_ar.set(null);
            }
            if (details_en.isEmpty()) {
                error_details_en.set(context.getString(R.string.field_required));
            }else {
                error_details_en.set(null);
            }


            return false;
        }
    }

    public AddWantedModel(){
        setTitle_ar("");
        setTitle_en("");
        setDetails_ar("");
        setDetails_en("");
        setAmount("");
        setImage(null);
        setStatus("");
        setType("");
        setMark_id("");
        setModel_id("");
    }


    public Uri getImage() {
        return image;
    }

    public void setImage(Uri image) {
        this.image = image;
    }

    public String getTitle_ar() {
        return title_ar;
    }

    public void setTitle_ar(String title_ar) {
        this.title_ar = title_ar;
    }

    public String getTitle_en() {
        return title_en;
    }

    public void setTitle_en(String title_en) {
        this.title_en = title_en;
    }

    public String getDetails_ar() {
        return details_ar;
    }

    public void setDetails_ar(String details_ar) {
        this.details_ar = details_ar;
    }

    public String getDetails_en() {
        return details_en;
    }

    public void setDetails_en(String details_en) {
        this.details_en = details_en;
    }

    public String getModel_id() {
        return model_id;
    }

    public void setModel_id(String model_id) {
        this.model_id = model_id;
    }

    public String getMark_id() {
        return mark_id;
    }

    public void setMark_id(String mark_id) {
        this.mark_id = mark_id;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
