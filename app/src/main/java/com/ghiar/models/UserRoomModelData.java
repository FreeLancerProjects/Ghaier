package com.ghiar.models;

import java.io.Serializable;
import java.util.List;

public class UserRoomModelData implements Serializable {

    private List<UserRoomModel> rooms;
    private int current_page;


    public List<UserRoomModel> getRooms() {
        return rooms;
    }

    public int getCurrent_page() {
        return current_page;
    }

    public class UserRoomModel implements Serializable {
        private int id;
        private String first_user_id;
        private String second_user_id;

        private String image;
        private String name;
        private String message;
        private String read;
        private String date;
        private String message_type;

        public int getId() {
            return id;
        }

        public String getFirst_user_id() {
            return first_user_id;
        }

        public String getSecond_user_id() {
            return second_user_id;
        }

        public String getImage() {
            return image;
        }

        public String getName() {
            return name;
        }

        public String getMessage() {
            return message;
        }

        public String getRead() {
            return read;
        }

        public String getDate() {
            return date;
        }

        public String getMessage_type() {
            return message_type;
        }
    }
}
