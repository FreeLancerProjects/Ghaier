package com.ghiar.models;

import java.io.Serializable;
import java.util.List;

public class SingleAuctionModel implements Serializable {

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
    private User user;
    private int count;

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

    public User getUser() {
        return user;
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

    public static class User implements Serializable {
        private int id;
        private String name;
        private String email;
        private String phone_code;
        private String phone;
        private String image;
        private String logo;
        private String latitude;
        private String longitude;
        private String address;
        private String user_type;
        private String details;

        private String fireBaseToken;

        public User() {
        }

        public User(int id, String name, String phone_code, String phone, String image, String token) {
            this.id = id;
            this.name = name;
            this.phone_code = phone_code;
            this.phone = phone;
            this.image = image;
        }

        public int getId() {
            return id;
        }

        public String getName() {
            return name;
        }

        public String getEmail() {
            return email;
        }


        public String getPhone_code() {
            return phone_code;
        }

        public String getPhone() {
            return phone;
        }

        public String getImage() {
            return image;
        }

        public String getLogo() {
            return logo;
        }


        public String getLatitude() {
            return latitude;
        }

        public String getLongitude() {
            return longitude;
        }

        public String getAddress() {
            return address;
        }

        public String getType() {
            return user_type;
        }

        public String getFireBaseToken() {
            return fireBaseToken;
        }

        public void setFireBaseToken(String fireBaseToken) {
            this.fireBaseToken = fireBaseToken;
        }


        public String getDetails() {
            return details;
        }


    }

    public int getCount() {
        return count;
    }
}
