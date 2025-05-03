package com.example.farakhni.data.DB;


import androidx.lifecycle.LiveData;
import com.example.farakhni.model.Meal;
import java.util.List;

public interface FavoriteMealsLocalDataSource {
    LiveData<List<Meal>> getFavoriteMeals();
    void insertMeal(Meal meal);
    void deleteMeal(Meal meal);
}
