package com.example.farakhni.model;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class MealListResponse {

    @SerializedName("meals")
    private List<Meal> meals;

    public List<Meal> getAllMeals() {
        return meals;
    }

    public void setMeals(List<Meal> meals) {
        this.meals = meals;
    }
}