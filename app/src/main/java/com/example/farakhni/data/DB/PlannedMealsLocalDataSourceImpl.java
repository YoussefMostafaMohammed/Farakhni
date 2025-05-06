package com.example.farakhni.data.DB;

import android.content.Context;

import androidx.lifecycle.LiveData;

import com.example.farakhni.model.PlannedMeal;

import java.util.List;

public class PlannedMealsLocalDataSourceImpl implements PlannedMealsLocalDataSource {
    private final PlannedMealDao plannedMealDao;
    private static PlannedMealsLocalDataSourceImpl instance;

    private PlannedMealsLocalDataSourceImpl(Context context) {
        AppDataBase db = AppDataBase.getInstance(context);
        this.plannedMealDao = db.getPlannedMealDAO();
    }

    public static synchronized PlannedMealsLocalDataSourceImpl getInstance(Context context) {
        if (instance == null) {
            instance = new PlannedMealsLocalDataSourceImpl(context.getApplicationContext());
        }
        return instance;
    }

    @Override
    public LiveData<List<PlannedMeal>> getAllPlannedMeals() {
        return plannedMealDao.getAllPlannedMeals();
    }

    @Override
    public LiveData<List<PlannedMeal>> getPlannedMealsForDate(String date) {
        return plannedMealDao.getPlannedMealsForDate(date);
    }

    @Override
    public void insertPlannedMeal(PlannedMeal plannedMeal) {
        plannedMealDao.insertPlannedMeal(plannedMeal);
    }

    @Override
    public void deletePlannedMeal(PlannedMeal plannedMeal) {
        plannedMealDao.deletePlannedMeal(plannedMeal);
    }

    @Override
    public boolean isMealPlanned(String mealId, String date) {
        return plannedMealDao.isMealPlanned(mealId, date);
    }
}