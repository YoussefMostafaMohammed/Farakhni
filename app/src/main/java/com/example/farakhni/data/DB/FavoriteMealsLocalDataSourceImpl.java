package com.example.farakhni.data.DB;

import androidx.lifecycle.LiveData;
import com.example.farakhni.model.Meal;
import java.util.List;

import android.content.Context;
import androidx.lifecycle.LiveData;
import com.example.farakhni.data.DB.AppDataBase;
import com.example.farakhni.data.DB.FavoriteMealDAO;
import com.example.farakhni.model.Meal;
import java.util.List;

public class FavoriteMealsLocalDataSourceImpl implements FavoriteMealsLocalDataSource {
    private FavoriteMealDAO dao;
    private static FavoriteMealsLocalDataSourceImpl instance;
    private LiveData<List<Meal>> storedMeals;

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
    public LiveData<List<Meal>> getFavoriteMeals() {
        return storedMeals;
    }

    @Override
    public void insertMeal(Meal meal) {
        new Thread(() -> dao.insertMeal(meal)).start();
    }

    @Override
    public void deleteMeal(Meal meal) {
        new Thread(() -> dao.deleteMeal(meal)).start();
    }
}