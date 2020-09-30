package com.ghiar.models;

import java.io.Serializable;
import java.util.List;

public class MarksDataModel {
private List<MarkDataInModel> data;
private MarkDataInModel advertsment;
    public List<MarkDataInModel> getData() {
        return data;
    }

    public MarkDataInModel getAdvertsment() {
        return advertsment;
    }
}
