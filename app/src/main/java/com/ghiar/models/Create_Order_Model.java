package com.ghiar.models;

import java.io.Serializable;
import java.util.List;

public class Create_Order_Model implements Serializable {

  private String user_id;
    private String market_id;
    private String address;
    private String google_lat;
    private String google_long;
    private String payment_method;
    private String full_name;
    private String phone;
    private String total_cost;
    private List<OrderDetails> details;
    private List<ProductDetails> productDetails;

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getMarket_id() {
        return market_id;
    }

    public void setMarket_id(String market_id) {
        this.market_id = market_id;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getGoogle_lat() {
        return google_lat;
    }

    public void setGoogle_lat(String google_lat) {
        this.google_lat = google_lat;
    }

    public String getGoogle_long() {
        return google_long;
    }

    public void setGoogle_long(String google_long) {
        this.google_long = google_long;
    }

    public String getPayment_method() {
        return payment_method;
    }

    public void setPayment_method(String payment_method) {
        this.payment_method = payment_method;
    }

    public String getFull_name() {
        return full_name;
    }

    public void setFull_name(String full_name) {
        this.full_name = full_name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getTotal_cost() {
        return total_cost;
    }

    public void setTotal_cost(String total_cost) {
        this.total_cost = total_cost;
    }

    public List<OrderDetails> getDetails() {
        return details;
    }

    public void setDetails(List<OrderDetails> details) {
        this.details = details;
    }

    public static class OrderDetails implements Serializable {
        private int adv_id;
        private int amount;
        private double cost;

        public int getAdv_id() {
            return adv_id;
        }

        public void setAdv_id(int adv_id) {
            this.adv_id = adv_id;
        }

        public int getAmount() {
            return amount;
        }

        public void setAmount(int amount) {
            this.amount = amount;
        }

        public double getCost() {
            return cost;
        }

        public void setCost(double cost) {
            this.cost = cost;
        }
    }

    public List<ProductDetails> getProductDetails() {
        return productDetails;
    }

    public void setProductDetails(List<ProductDetails> productDetails) {
        this.productDetails = productDetails;
    }

    public static class ProductDetails implements Serializable {
        private int amount;
        private double total_cost;
        private String image;
        private String name;


        public int getAmount() {
            return amount;
        }

        public void setAmount(int amount) {
            this.amount = amount;
        }

        public double getTotal_cost() {
            return total_cost;
        }

        public void setTotal_cost(double total_cost) {
            this.total_cost = total_cost;
        }

        public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }


}
