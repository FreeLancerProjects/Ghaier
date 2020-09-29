package com.ghiar.models;

import java.util.List;

public class ProductsModel {
  private   List<ProductModel> part;
   private List<ProductModel> accessory;

    public List<ProductModel> getPart() {
        return part;
    }

    public List<ProductModel> getAccessory() {
        return accessory;
    }
}
