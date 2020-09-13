package com.ghiar.models;

import java.io.Serializable;
import java.util.List;

public class CityDataModel implements Serializable {

    public City city;

    public City getCity() {
        return city;
    }

    public static class City implements Serializable{
        private List<String> city;

        public List<String> getCity() {
            return city;
        }
    }
}
