package com.ghiar.models;

import java.io.Serializable;
import java.util.List;

public class ServiceCentersModel implements Serializable {
    private List<ServiceCenterModel> markets;
    private ServiceCenterModel market;

    public List<ServiceCenterModel> getMarkets() {
        return markets;
    }

    public ServiceCenterModel getMarket() {
        return market;
    }
}
