package com.example.farakhni.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class IngredientListResponse {
    @SerializedName("meals")
    private List<Ingredient> ingredients;

    public List<Ingredient>  getALlIngredients() {
        return ingredients;
    }
}
