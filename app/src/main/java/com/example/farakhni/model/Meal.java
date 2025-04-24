package com.example.farakhni.model;

import com.google.gson.annotations.SerializedName;

public class Meal {
    @SerializedName("idMeal")
    private String id;

    @SerializedName("strMeal")
    private String name;

    @SerializedName("strCategory")
    private String category;

    @SerializedName("strArea")
    private String area;

    @SerializedName("strInstructions")
    private String instructions;

    @SerializedName("strMealThumb")
    private String mealThumb;

    @SerializedName("strTags")
    private String tags;

    @SerializedName("strYoutube")
    private String youtube;

    public void setCategory(String category) {
        this.category = category;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public void setInstructions(String instructions) {
        this.instructions = instructions;
    }

    public void setMealThumb(String mealThumb) {
        this.mealThumb = mealThumb;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public void setYoutube(String youtube) {
        this.youtube = youtube;
    }

    public String getCategory() {
        return category;
    }

    public String getArea() {
        return area;
    }

    public String getInstructions() {
        return instructions;
    }

    public String getMealThumb() {
        return mealThumb;
    }

    public String getTags() {
        return tags;
    }

    public String getYoutube() {
        return youtube;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public String getId() {
        return id;
    }

}