package com.ghiar.models;

import java.util.List;

public class ServiceModel {

    private int id;
    private String title_ar;
    private String title_en;
    private String image;
    private String updated_at;
    private String created_at;

    public int getId() {
        return id;
    }

    public String getTitle_ar() {
        return title_ar;
    }

    public String getTitle_en() {
        return title_en;
    }

    public String getImage() {
        return image;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public String getCreated_at() {
        return created_at;
    }
}
