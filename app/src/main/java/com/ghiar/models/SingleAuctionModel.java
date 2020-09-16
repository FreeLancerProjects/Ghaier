package com.ghiar.models;

import java.io.Serializable;
import java.util.List;

public class SingleAuctionModel implements Serializable{

    private int id;
    private String main_image;
    private String title_ar;
    private String title_en;
    private String start_price;
    private String step_price;
    private String amount;
    private String end_date;
    private String details_ar;
    private String details_en;
    private String status;
    private String user_id;
    private String current;
    private List<Images> images;

    public int getId() {
        return id;
    }

    public String getMain_image() {
        return main_image;
    }

    public String getTitle_ar() {
        return title_ar;
    }

    public String getTitle_en() {
        return title_en;
    }

    public String getStart_price() {
        return start_price;
    }

    public String getStep_price() {
        return step_price;
    }

    public String getAmount() {
        return amount;
    }

    public String getEnd_date() {
        return end_date;
    }

    public String getDetails_ar() {
        return details_ar;
    }

    public String getDetails_en() {
        return details_en;
    }

    public String getStatus() {
        return status;
    }

    public String getUser_id() {
        return user_id;
    }

    public String getCurrent() {
        return current;
    }

    public List<Images> getImages() {
        return images;
    }

    public class Images implements Serializable {
        private int id;
        private String auction_id;
        private String image;

        public int getId() {
            return id;
        }

        public String getAuction_id() {
            return auction_id;
        }

        public String getImage() {
            return image;
        }
    }
}
