package com.ghiar.models;

import java.io.Serializable;
import java.util.List;

public class ServiceCentersModel implements Serializable {
    List<ServiceCenterModel> markets;

    public List<ServiceCenterModel> getMarkets() {
        return markets;
    }
}
