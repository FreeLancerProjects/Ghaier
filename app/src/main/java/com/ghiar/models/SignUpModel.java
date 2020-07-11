package com.ghiar.models;

import android.content.Context;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;
import androidx.databinding.ObservableField;

import com.ghiar.BR;
import com.ghiar.R;


public class SignUpModel extends BaseObservable {
    private String name;

    public ObservableField<String> error_name = new ObservableField<>();



    public boolean isDataValid(Context context)
    {
        if (!name.trim().isEmpty()
        ){
            error_name.set(null);


            return true;
        }else
            {
                if (name.trim().isEmpty())
                {
                    error_name.set(context.getString(R.string.field_required));

                }else
                    {
                        error_name.set(null);

                    }
return false;

    }

    }
    public SignUpModel() {
        setName("");

    }



    @Bindable
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
        notifyPropertyChanged(BR.name);

    }


}
