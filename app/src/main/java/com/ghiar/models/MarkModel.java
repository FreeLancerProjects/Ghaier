package com.ghiar.models;

import java.util.List;

public class MarkModel {

   private int id;
   private String title_ar;
   private String title_en;
    private String image;
    private String updated_at;
   private String created_at;

    public void setId(int id) {
        this.id = id;
    }

    public void setTitle_ar(String title_ar) {
        this.title_ar = title_ar;
    }

    public void setTitle_en(String title_en) {
        this.title_en = title_en;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

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
