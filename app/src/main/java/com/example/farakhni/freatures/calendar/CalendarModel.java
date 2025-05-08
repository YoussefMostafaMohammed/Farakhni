package com.example.farakhni.freatures.calendar;

import androidx.lifecycle.LiveData;
import com.example.farakhni.data.repositories.MealRepositoryImpl;
import com.example.farakhni.model.Meal;
import com.example.farakhni.model.PlannedMeal;
import java.util.List;

public class CalendarModel {
    private final MealRepositoryImpl repo;

    public CalendarModel(MealRepositoryImpl repo) {
        this.repo = repo;
    }

    public LiveData<List<PlannedMeal>> getPlannedMealsForDate(String date) {
        LiveData<List<PlannedMeal>> meals = repo.getPlannedMealsForDate(date);
        meals.observeForever(plannedMeals -> {
            android.util.Log.d("CalendarModel", "Fetched " + (plannedMeals != null ? plannedMeals.size() : 0) + " planned meals for " + date);
        });
        return meals;
    }

    public void addPlannedMeal(Meal meal, String date) {
        PlannedMeal plannedMeal = new PlannedMeal(meal, date);
        repo.insertPlannedMeal(plannedMeal);
        android.util.Log.d("CalendarModel", "Inserted meal ID " + meal.getId() + " for " + date);
    }

    public void deletePlannedMeal(Meal meal, String date) {
        repo.deletePlannedMeal(new PlannedMeal(meal, date));
    }
}