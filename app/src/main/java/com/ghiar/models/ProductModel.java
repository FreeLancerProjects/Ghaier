package com.ghiar.models;

import java.util.List;

public class ProductModel {

    int id;
    String title_ar,title_en,country_id,price,mark_id,type,rate,details_ar,details_en,bail,image,status,for_home,user_id
            ,model_id,created_at,updated_at;

    List<ProductModel> part;
    List<ProductModel> accessory;

    public List<ProductModel> getPart() {
        return part;
    }

    public void setPart(List<ProductModel> part) {
        this.part = part;
    }

    public List<ProductModel> getAccessory() {
        return accessory;
    }

    public void setAccessory(List<ProductModel> accessory) {
        this.accessory = accessory;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public String getRate() {
        return rate;
    }

    public void setRate(String rate) {
        this.rate = rate;
    }

    public String getCountry_id() {
        return country_id;
    }

    public void setCountry_id(String country_id) {
        this.country_id = country_id;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getMark_id() {
        return mark_id;
    }

    public void setMark_id(String mark_id) {
        this.mark_id = mark_id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
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

    public String getBail() {
        return bail;
    }

    public void setBail(String bail) {
        this.bail = bail;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }


    public String getFor_home() {
        return for_home;
    }

    public void setFor_home(String for_home) {
        this.for_home = for_home;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getModel_id() {
        return model_id;
    }

    public void setModel_id(String model_id) {
        this.model_id = model_id;
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
}
