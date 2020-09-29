package com.ghiar.models;

import java.io.Serializable;
import java.util.List;

public class MainServiceModel  implements Serializable {
  private   List<ServiceModel> main_services;

    public List<ServiceModel> getMain_services() {
        return main_services;
    }
}
