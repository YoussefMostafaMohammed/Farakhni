package com.example.farakhni.data.network;
import androidx.lifecycle.LiveData;
import com.example.farakhni.data.network.NetworkCallBack;
import com.example.farakhni.model.Meal;
import java.util.List;

public interface MealRepository {
    LiveData<List<Meal>> getFavoriteMeals();
    void getMealById(String mealId, NetworkCallBack<List<Meal>> callback);
    void searchMealsByName(String mealName, NetworkCallBack<List<Meal>> callback);
    void getRandomMeal(NetworkCallBack<List<Meal>> callback);
    void getMealsByFirstLetter(String firstLetter, NetworkCallBack<List<Meal>> callback);
    void filterByIngredient(String ingredient, NetworkCallBack<List<Meal>> callback);
    void filterByCategory(String category, NetworkCallBack<List<Meal>> callback);
    void filterByArea(String area, NetworkCallBack<List<Meal>> callback);
    void insertFavoriteMeal(Meal meal);
    void deleteFavoriteMeal(Meal meal);
}

