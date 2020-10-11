package com.ghiar.models;

import java.io.Serializable;
import java.util.List;

public class MyAuctionModel implements Serializable {

    private int current_page;
    private List<SingleAuctionModel> data;

    public int getCurrent_page() {
        return current_page;
    }

    public List<SingleAuctionModel> getData() {
        return data;
    }
}
