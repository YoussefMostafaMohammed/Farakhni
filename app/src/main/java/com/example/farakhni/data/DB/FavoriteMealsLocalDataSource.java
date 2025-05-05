package com.example.farakhni.data.DB;

import androidx.lifecycle.LiveData;

import com.example.farakhni.model.FavoriteMeal;

import java.util.List;

public interface FavoriteMealsLocalDataSource {
    LiveData<List<FavoriteMeal>> getFavoriteMeals();
    void insertMeal(FavoriteMeal meal);
    void deleteMeal(FavoriteMeal meal);
    boolean isFavorite(String mealId);
}