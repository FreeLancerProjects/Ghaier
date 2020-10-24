package com.ghiar.models;

import java.io.Serializable;
import java.util.List;

public class Order_Model implements Serializable {

    private int current_page;
    private List<Data> orders;

    public int getCurrent_page() {
        return current_page;
    }

    public List<Data> getData() {
        return orders;
    }

    public class Data implements Serializable {

        private int id;

        private long order_date;
        private String user_id;
        private String market_id;
        private String address;

        private String full_name;
        private String phone;
        private String time;
        private String order_status;
        private String total_cost;

        public int getId() {
            return id;
        }

        public long getOrder_date() {
            return order_date;
        }

        public String getUser_id() {
            return user_id;
        }

        public String getMarket_id() {
            return market_id;
        }

        public String getAddress() {
            return address;
        }

        public String getFull_name() {
            return full_name;
        }

        public String getPhone() {
            return phone;
        }

        public String getTime() {
            return time;
        }

        public String getOrder_status() {
            return order_status;
        }

        public String getTotal_cost() {
            return total_cost;
        }
    }
}
