package com.example.farakhni.model;

import com.google.gson.annotations.SerializedName;

public class Category {
    @SerializedName("strCategory")
    private String category;
    @SerializedName("strCategoryThumb")
    private String CategoryThumb;

    @SerializedName("strCategoryDescription")
    private String categoryDescription;

    public void setCategoryThumb(String categoryThumb) {
        CategoryThumb = categoryThumb;
    }

    public void setCategoryDescription(String categoryDescription) {
        this.categoryDescription = categoryDescription;
    }

    public String getCategoryDescription() {
        return categoryDescription;
    }

    public String getCategoryThumb() {
        return CategoryThumb;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}
