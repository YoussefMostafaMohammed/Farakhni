package com.example.farakhni.model;

import com.google.gson.annotations.SerializedName;

public class Area {
    @SerializedName("strArea")
    private String area;

    public void setArea(String area) {
        this.area = area;
    }

    public String getArea() {
        return area;
    }
}
