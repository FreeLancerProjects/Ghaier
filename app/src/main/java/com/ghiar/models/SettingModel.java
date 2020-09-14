package com.ghiar.models;

import java.io.Serializable;

public class SettingModel implements Serializable {

    private Setting setting;

    public Setting getSetting() {
        return setting;
    }

    public static class Setting implements Serializable {
        private String permission;
        private String about;

        public String getPermission() {
            return permission;
        }

        public String getAbout() {
            return about;
        }
    }
}
