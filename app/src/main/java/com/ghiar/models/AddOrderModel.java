package com.ghiar.models;

import android.content.Context;
import android.widget.Toast;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;
import androidx.databinding.ObservableField;


import com.ghiar.BR;
import com.ghiar.R;

import java.io.Serializable;

public class AddOrderModel extends BaseObservable implements Serializable {

    private String address;
    private double lat;
    private double lng;
    private String name;
    private String phone_code;
    private String phone;
    private String street;
    private String payment_type;
    public ObservableField<String> error_address = new ObservableField<>();
    public ObservableField<String> error_name = new ObservableField<>();
    public ObservableField<String> error_phone = new ObservableField<>();



    public AddOrderModel() {
        setAddress("");
        setLat(0.0);
        setLat(0.0);
        setStreet("");
        setPayment_type("cash");
    }


    public boolean isStep1Valid(Context context)
    {
        if (!name.trim().isEmpty()&&
                !phone.trim().isEmpty()&&
                !address.trim().isEmpty()&&
                lat!=0.0&&lng!=0.0
        ){
            error_name.set(null);
            error_phone.set(null);
            error_address.set(null);
            return true;
        }else {

            if (name.trim().isEmpty()){
                error_name.set(context.getString(R.string.field_req));
            }else {
                error_name.set(null);

            }

            if (phone.trim().isEmpty()){
                error_phone.set(context.getString(R.string.field_req));
            }else {
                error_phone.set(null);

            }

            if (address.trim().isEmpty()){
                error_address.set(context.getString(R.string.field_req));
            }else {
                error_address.set(null);

            }
            return false;

        }
    }


    @Bindable
    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
        notifyPropertyChanged(BR.address);
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }


    public String getPayment_type() {
        return payment_type;
    }

    public void setPayment_type(String payment_type) {
        this.payment_type = payment_type;
    }

    @Bindable
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
        notifyPropertyChanged(BR.name);
    }

    @Bindable
    public String getPhone_code() {
        return phone_code;
    }

    public void setPhone_code(String phone_code) {
        this.phone_code = phone_code;
        notifyPropertyChanged(BR.phone_code);

    }

    @Bindable
    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
        notifyPropertyChanged(BR.phone);

    }

    @Bindable
    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
        notifyPropertyChanged(BR.street);

    }
}
