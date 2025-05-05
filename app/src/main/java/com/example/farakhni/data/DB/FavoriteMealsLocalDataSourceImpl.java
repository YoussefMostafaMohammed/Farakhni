package com.example.farakhni.data.DB;

import android.content.Context;

import androidx.lifecycle.LiveData;

import com.example.farakhni.model.FavoriteMeal;

import java.util.List;

public class FavoriteMealsLocalDataSourceImpl implements FavoriteMealsLocalDataSource {
    private final FavoriteMealDAO dao;
    private static FavoriteMealsLocalDataSourceImpl instance;
    private final LiveData<List<FavoriteMeal>> storedMeals;

    private FavoriteMealsLocalDataSourceImpl(Context context) {
        AppDataBase db = AppDataBase.getInstance(context.getApplicationContext());
        dao = db.getFavoriteMealDAO();
        storedMeals = dao.getFavoriteMealsForUser();
    }

    public static synchronized FavoriteMealsLocalDataSourceImpl getInstance(Context context) {
        if (instance == null) {
            instance = new FavoriteMealsLocalDataSourceImpl(context);
        }
        return instance;
    }

    @Override
    public LiveData<List<FavoriteMeal>> getFavoriteMeals() {
        return storedMeals;
    }

    @Override
    public void insertMeal(FavoriteMeal meal) {
        new Thread(() -> dao.insertMeal(meal)).start();
    }

    @Override
    public void deleteMeal(FavoriteMeal meal) {
        new Thread(() -> dao.deleteMeal(meal)).start();
    }

    @Override
    public boolean isFavorite(String mealId) {
        return dao.isFavorite(mealId);
    }
}