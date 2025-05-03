package com.example.farakhni.common;

import com.example.farakhni.model.Meal;

import java.util.List;

public interface OnIngredientClickListener {
    void onIngredientSelected(List<Meal> meals);
}