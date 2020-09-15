package com.ghiar.models;

import java.util.List;

public class SliderModel {

    int id;
    String image,type,created_at,updated_at;
    List<SliderModel>  top;
    List<SliderModel>  bottom;

    public List<SliderModel> getTop() {
        return top;
    }

    public void setTop(List<SliderModel> top) {
        this.top = top;
    }

    public List<SliderModel> getBottom() {
        return bottom;
    }

    public void setBottom(List<SliderModel> bottom) {
        this.bottom = bottom;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
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
