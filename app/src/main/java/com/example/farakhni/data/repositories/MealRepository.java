package com.example.farakhni.data.repositories;

import androidx.lifecycle.LiveData;
import com.example.farakhni.data.DB.FavoriteMealsLocalDataSource;
import com.example.farakhni.data.DB.PlannedMealsLocalDataSource;
import com.example.farakhni.data.network.NetworkCallBack;
import com.example.farakhni.model.FavoriteMeal;
import com.example.farakhni.model.Meal;
import com.example.farakhni.model.PlannedMeal;
import java.util.List;

public interface MealRepository {
    LiveData<List<FavoriteMeal>> getFavoriteMeals();
    void getMealById(String mealId, NetworkCallBack<List<Meal>> callback);
    void searchMealsByName(String mealName, NetworkCallBack<List<Meal>> callback);
    void getRandomMeal(NetworkCallBack<List<Meal>> callback);
    void getMealsByFirstLetter(String firstLetter, NetworkCallBack<List<Meal>> callback);
    void filterByIngredient(String ingredient, NetworkCallBack<List<Meal>> callback);
    void filterByCategory(String category, NetworkCallBack<List<Meal>> callback);
    void filterByArea(String area, NetworkCallBack<List<Meal>> callback);
    void insertFavoriteMeal(FavoriteMeal meal);
    void deleteFavoriteMeal(FavoriteMeal meal);
    boolean isFavorite(String mealId);
    LiveData<List<PlannedMeal>> getAllPlannedMeals();
    LiveData<List<PlannedMeal>> getPlannedMealsForDate(String date);
    void insertPlannedMeal(PlannedMeal plannedMeal);
    void deletePlannedMeal(PlannedMeal plannedMeal);
    boolean isMealPlanned(String mealId, String date);
    void syncFavoriteMealsWithFirestore(FavoriteMealsLocalDataSource.OnSyncCompleteListener listener);
    void syncPlannedMealsWithFirestore(PlannedMealsLocalDataSource.OnSyncCompleteListener listener);
}