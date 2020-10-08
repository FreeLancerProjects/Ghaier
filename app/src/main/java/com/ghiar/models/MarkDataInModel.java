package com.ghiar.models;

import java.io.Serializable;
import java.util.List;

public class MarkDataInModel implements Serializable {

    private int id;
    private String title_ar;
    private String title_en;
    private String country_id;
    private String price;
    private String mark_id;
    private String type;
    private String details_ar;
    private String details_en;
    private String bail;
    private String image;
    private String status;
    private String rate;
    private String for_home;
    private String user_id;
    private String model_id;
    private String created_at;
    private String updated_at;
    private List<Images> images;
    private City city;

    private ServiceCenterModel market;

    public int getId() {
        return id;
    }

    public String getTitle_ar() {
        return title_ar;
    }

    public String getTitle_en() {
        return title_en;
    }

    public String getCountry_id() {
        return country_id;
    }

    public String getPrice() {
        return price;
    }

    public String getMark_id() {
        return mark_id;
    }

    public String getType() {
        return type;
    }

    public String getDetails_ar() {
        return details_ar;
    }

    public String getDetails_en() {
        return details_en;
    }

    public String getBail() {
        return bail;
    }

    public String getImage() {
        return image;
    }

    public String getStatus() {
        return status;
    }

    public String getRate() {
        return rate;
    }

    public String getFor_home() {
        return for_home;
    }

    public String getUser_id() {
        return user_id;
    }

    public String getModel_id() {
        return model_id;
    }

    public String getCreated_at() {
        return created_at;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public City getCity() {
        return city;
    }

    public List<Images> getImages() {
        return images;
    }




    public ServiceCenterModel getMarket() {
        return market;
    }

    public class Images implements Serializable {
        private int id;
        private String image;

        public int getId() {
            return id;
        }


        public String getImage() {
            return image;
        }
    }

    public class City implements Serializable {
        private String ar_city_title;
        private String en_city_title;

        public String getAr_city_title() {
            return ar_city_title;
        }

        public String getEn_city_title() {
            return en_city_title;
        }
    }


}
