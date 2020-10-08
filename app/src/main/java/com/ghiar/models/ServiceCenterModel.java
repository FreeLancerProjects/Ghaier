package com.ghiar.models;

import java.io.Serializable;
import java.util.List;

public class ServiceCenterModel {


    private int id;
    private String name;
    private String is_login;
    private String email;
    private String user_type;
    private String phone;
    private String phone_code;
    private String city_id;
    private String image;
    private String details;
    private String panner;
    private String address;
    private String google_lat;
    private String google_long;
    private String confirmed;
    private String jop;
    private String rate;
    private String email_verified_at;
    private String created_at;
    private String updated_at;
    private CityDataModel.CityModel city;
    private List<ServiceModel> services;
    private double miles;

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getIs_login() {
        return is_login;
    }

    public String getEmail() {
        return email;
    }

    public String getUser_type() {
        return user_type;
    }

    public String getPhone() {
        return phone;
    }

    public String getPhone_code() {
        return phone_code;
    }

    public String getCity_id() {
        return city_id;
    }

    public String getImage() {
        return image;
    }

    public String getDetails() {
        return details;
    }

    public String getPanner() {
        return panner;
    }

    public String getAddress() {
        return address;
    }

    public String getGoogle_lat() {
        return google_lat;
    }

    public String getGoogle_long() {
        return google_long;
    }

    public String getConfirmed() {
        return confirmed;
    }

    public String getJop() {
        return jop;
    }

    public String getRate() {
        return rate;
    }

    public String getEmail_verified_at() {
        return email_verified_at;
    }

    public String getCreated_at() {
        return created_at;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public CityDataModel.CityModel getCity() {
        return city;
    }

    public List<ServiceModel> getServices() {
        return services;
    }


    public double getMiles() {
        return miles;
    }


}
