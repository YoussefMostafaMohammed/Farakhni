package com.example.farakhni.model;

import com.google.gson.annotations.SerializedName;

public class Category {
    @SerializedName("strCategory")
    private String category;

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}
