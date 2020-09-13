package com.ghiar.models;

import java.util.List;

public class ServiceModel {

    int id;
    String title_ar,title_en,image,updated_at,created_at;
    List<ServiceModel> main_services;

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

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public List<ServiceModel> getMain_services() {
        return main_services;
    }

    public void setMain_services(List<ServiceModel> main_services) {
        this.main_services = main_services;
    }
}
