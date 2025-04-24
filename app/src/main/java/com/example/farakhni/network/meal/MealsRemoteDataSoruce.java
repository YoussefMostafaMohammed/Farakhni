package com.example.farakhni.network.meal;


import com.example.farakhni.model.Meal;
import com.example.farakhni.network.NetworkCallBack;

import java.util.List;

public interface MealsRemoteDataSoruce {
    void makeNetworkCallgetMealByID(String mealId,NetworkCallBack<List<Meal>> networkCallBack);
    void makeNetworkCallgetMealByName(String mealName, NetworkCallBack<List<Meal>> networkCallBack);
    void makeNetworkCallgetRandomMeal(NetworkCallBack<List<Meal>> networkCallBack);

}
