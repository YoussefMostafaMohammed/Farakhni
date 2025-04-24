package com.example.farakhni.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class MealListResponse {
    @SerializedName("meals")
    private List<Meal> meals;
    public List<Meal> getMeals() {
        return meals != null ? meals : new ArrayList<>();
    }
}