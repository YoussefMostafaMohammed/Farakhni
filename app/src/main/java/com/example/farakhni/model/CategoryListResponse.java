package com.example.farakhni.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class CategoryListResponse {
    @SerializedName("categories")
    private List<Category> categories;
    public List<Category>  getAllCategories() {
        return categories;
    }
}
