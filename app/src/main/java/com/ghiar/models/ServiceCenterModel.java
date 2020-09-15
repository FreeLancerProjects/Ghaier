package com.ghiar.models;

import java.util.List;

public class ServiceCenterModel {


    int id;
    String name,is_login,email,user_type,phone,phone_code,city_id,image,details,panner,address,google_lat,google_long,confirmed,jop
    ,rate,email_verified_at,created_at,updated_at;
    CityDataModel.CityModel city;
    ServiceModel services;
    List<ServiceCenterModel> markets;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIs_login() {
        return is_login;
    }

    public void setIs_login(String is_login) {
        this.is_login = is_login;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUser_type() {
        return user_type;
    }

    public void setUser_type(String user_type) {
        this.user_type = user_type;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPhone_code() {
        return phone_code;
    }

    public void setPhone_code(String phone_code) {
        this.phone_code = phone_code;
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

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public String getPanner() {
        return panner;
    }

    public void setPanner(String panner) {
        this.panner = panner;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getGoogle_lat() {
        return google_lat;
    }

    public void setGoogle_lat(String google_lat) {
        this.google_lat = google_lat;
    }

    public String getGoogle_long() {
        return google_long;
    }

    public void setGoogle_long(String google_long) {
        this.google_long = google_long;
    }

    public String getConfirmed() {
        return confirmed;
    }

    public void setConfirmed(String confirmed) {
        this.confirmed = confirmed;
    }

    public String getJop() {
        return jop;
    }

    public void setJop(String jop) {
        this.jop = jop;
    }

    public String getRate() {
        return rate;
    }

    public void setRate(String rate) {
        this.rate = rate;
    }

    public String getEmail_verified_at() {
        return email_verified_at;
    }

    public void setEmail_verified_at(String email_verified_at) {
        this.email_verified_at = email_verified_at;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }

    public ServiceModel getServices() {
        return services;
    }

    public void setServices(ServiceModel services) {
        this.services = services;
    }

    public List<ServiceCenterModel> getMarkets() {
        return markets;
    }

    public void setMarkets(List<ServiceCenterModel> markets) {
        this.markets = markets;
    }

    public CityDataModel.CityModel getCity() {
        return city;
    }

    public void setCity(CityDataModel.CityModel city) {
        this.city = city;
    }
}
