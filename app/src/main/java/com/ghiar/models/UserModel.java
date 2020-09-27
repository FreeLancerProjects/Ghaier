package com.ghiar.models;

import java.io.Serializable;
import java.util.List;

public class UserModel implements Serializable {

    private User user;


    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
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
}
