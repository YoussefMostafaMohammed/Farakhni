package com.example.farakhni.network.meal;


import com.example.farakhni.model.Meal;
import com.example.farakhni.network.NetworkCallBack;

import java.util.List;

public interface MealsRemoteDataSoruce {
    public void makeNetworkCallgetMealByID(String mealId,NetworkCallBack<List<Meal>> networkCallBack);
    public void makeNetworkCallgetMealByName(String mealName, NetworkCallBack<List<Meal>> networkCallBack);
    public void makeNetworkCallgetRandomMeal(NetworkCallBack<List<Meal>> networkCallBack);

    public void makeNetworkCallgetMealByFirstLetter(String charFirstLetter,NetworkCallBack<List<Meal>> networkCallBack);
    public void makeNetworkCallFilterByIngredient(String ingredient, NetworkCallBack<List<Meal>> networkCallBack);
}
