package com.ghiar.models;

import java.io.Serializable;
import java.util.List;

public class OrderModel implements Serializable {
    private List<Item> item;
    private Order order;

    public List<Item> getItem() {
        return item;
    }

    public Order getOrder() {
        return order;
    }

    public class Item implements Serializable {
        private int id;
        private String title_ar;
        private String title_en;
        private String price;
        private String image;
        private int user_id;
        private String model_id;
        private String amount;

        public int getId() {
            return id;
        }

        public String getTitle_ar() {
            return title_ar;
        }

        public String getTitle_en() {
            return title_en;
        }

        public String getPrice() {
            return price;
        }

        public String getImage() {
            return image;
        }

        public int getUser_id() {
            return user_id;
        }

        public String getModel_id() {
            return model_id;
        }

        public String getAmount() {
            return amount;
        }
    }

    public class Order implements Serializable {
        private int id;

        private String address;

        private String order_status;

        public int getId() {
            return id;
        }

        public String getAddress() {
            return address;
        }

        public String getOrder_status() {
            return order_status;
        }
    }


}
