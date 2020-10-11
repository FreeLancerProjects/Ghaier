package com.ghiar.models;

import java.io.Serializable;

public class MessageModel  implements Serializable {
    
 private int id;
         private String to_user_id;
    private String from_user_id;
    private String type;
    private String message;
    private String image;
    private String voice;
    private String room_id;
    private String is_read;
        private long date;

    public int getId() {
        return id;
    }

    public String getTo_user_id() {
        return to_user_id;
    }

    public String getFrom_user_id() {
        return from_user_id;
    }

    public String getType() {
        return type;
    }

    public String getMessage() {
        return message;
    }

    public String getImage() {
        return image;
    }

    public String getVoice() {
        return voice;
    }

    public String getRoom_id() {
        return room_id;
    }

    public String getIs_read() {
        return is_read;
    }

    public long getDate() {
        return date;
    }
}