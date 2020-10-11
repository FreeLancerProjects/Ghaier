package com.ghiar.models;

import java.io.Serializable;
import java.util.List;

public class MyRequiredModel implements Serializable {

    private int current_page;
    private List<Data> data;

    public int getCurrent_page() {
        return current_page;
    }

    public List<Data> getData() {
        return data;
    }

    public class Data implements Serializable {
     private int id;
             private String title_ar;
        private String title_en;
        private String model_id;
        private String user_id;
        private String amount;
        private String image;
        private String type;
        private String status;
        private String details_ar;
        private String details_en;
        private String mark_id;
        private String created_at;

        public int getId() {
            return id;
        }

        public String getTitle_ar() {
            return title_ar;
        }

        public String getTitle_en() {
            return title_en;
        }

        public String getModel_id() {
            return model_id;
        }

        public String getUser_id() {
            return user_id;
        }

        public String getAmount() {
            return amount;
        }

        public String getImage() {
            return image;
        }

        public String getType() {
            return type;
        }

        public String getStatus() {
            return status;
        }

        public String getDetails_ar() {
            return details_ar;
        }

        public String getDetails_en() {
            return details_en;
        }

        public String getMark_id() {
            return mark_id;
        }

        public String getCreated_at() {
            return created_at;
        }
    }
}
