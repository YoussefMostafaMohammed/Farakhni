package com.example.farakhni.model;

import com.google.gson.annotations.SerializedName;

public class Ingredient {
    @SerializedName("idIngredient")
    private String idIngredient;

    @SerializedName("strIngredient")
    private String ingredient;

    @SerializedName("strDescription")
    private String description;

    public void setDescription(String description) {
        this.description = description;
    }

    public void setIngredient(String ingredient) {
        this.ingredient = ingredient;
    }

    public void setIdIngredient(String idIngredient) {
        this.idIngredient = idIngredient;
    }

    public String getIdIngredient() {
        return idIngredient;
    }

    public String getIngredient() {
        return ingredient;
    }

    public String getDescription() {
        return description;
    }
}
