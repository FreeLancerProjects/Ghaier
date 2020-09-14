package com.ghiar.models;

import java.io.Serializable;
import java.util.List;

public class CityDataModel implements Serializable {
    private List<CityModel> city;

    public List<CityModel> getCity() {
        return city;
    }

    public static class CityModel implements Serializable{
        private String id_city;
        private String ar_city_title;
        private String en_city_title;

        public void setId_city(String id_city) {
            this.id_city = id_city;
        }

        public void setAr_city_title(String ar_city_title) {
            this.ar_city_title = ar_city_title;
        }

        public void setEn_city_title(String en_city_title) {
            this.en_city_title = en_city_title;
        }

        public String getId_city() {
            return id_city;
        }

        public String getAr_city_title() {
            return ar_city_title;
        }

        public String getEn_city_title() {
            return en_city_title;
        }
    }


}
