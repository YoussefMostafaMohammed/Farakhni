package com.example.farakhni.data.network.meal;
import com.example.farakhni.model.Meal;
import com.example.farakhni.data.network.NetworkCallBack;

import java.util.List;

public interface MealsRemoteDataSource {
    void makeNetworkCallgetMealByID(String mealId, NetworkCallBack<List<Meal>> networkCallBack);
    void makeNetworkCallgetMealByName(String mealName, NetworkCallBack<List<Meal>> networkCallBack);
    void makeNetworkCallgetRandomMeal(NetworkCallBack<List<Meal>> networkCallBack);
    void makeNetworkCallgetMealByFirstLetter(String charFirstLetter, NetworkCallBack<List<Meal>> networkCallBack);
    void makeNetworkCallFilterByIngredient(String ingredient, NetworkCallBack<List<Meal>> networkCallBack);
    void makeNetworkCallFilterByCategory(String category, NetworkCallBack<List<Meal>> networkCallBack);
    void makeNetworkCallFilterByArea(String area, NetworkCallBack<List<Meal>> networkCallBack);
}