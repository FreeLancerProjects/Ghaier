package com.ghiar.models;

import android.content.Context;
import android.util.Patterns;
import android.widget.Toast;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;
import androidx.databinding.ObservableField;

import com.ghiar.BR;
import com.ghiar.R;


public class SignUpModel extends BaseObservable {
    private String name;
    private String phone;
    private String phone_code;
    private String email;
    private String city_id;
    private String image;
    private String address;
    private boolean isTermsAccepted;

    public ObservableField<String> error_name = new ObservableField<>();


    public boolean isDataValid(Context context) {
        if (!name.trim().isEmpty()&
                !city_id.trim().isEmpty()&&
                isTermsAccepted
        ) {
            error_name.set(null);


            return true;
        } else {
            if (name.trim().isEmpty()) {
                error_name.set(context.getString(R.string.field_required));

            } else {
                error_name.set(null);

            }
            if (city_id.trim().isEmpty())
            {
                Toast.makeText(context, R.string.ch_city, Toast.LENGTH_SHORT).show();

            }

            if (!isTermsAccepted)
            {
                Toast.makeText(context, R.string.cannot_signup, Toast.LENGTH_SHORT).show();
            }
            return false;

        }

    }

    public SignUpModel() {
        setName("");
        setEmail("");
        setCity_id("");

        isTermsAccepted = false;
    }



    @Bindable
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
        notifyPropertyChanged(BR.name);

    }

    public String getPhone_code() {
        return phone_code;
    }

    public void setPhone_code(String phone_code) {
        this.phone_code = phone_code;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    @Bindable
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
        notifyPropertyChanged(BR.email);
    }


    public String getCity_id() {
        return city_id;
    }

    public void setCity_id(String city_id) {
        this.city_id = city_id;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public boolean isTermsAccepted() {
        return isTermsAccepted;
    }

    public void setTermsAccepted(boolean termsAccepted) {
        isTermsAccepted = termsAccepted;
    }


}
