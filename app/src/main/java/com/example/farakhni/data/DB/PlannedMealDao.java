package com.example.farakhni.data.DB;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.farakhni.model.PlannedMeal;

import java.util.List;

@Dao
public interface PlannedMealDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insertPlannedMeal(PlannedMeal plannedMeal);

    @Delete
    void deletePlannedMeal(PlannedMeal plannedMeal);

    @Query("SELECT * FROM planned_meals_table")
    LiveData<List<PlannedMeal>> getAllPlannedMeals();

    @Query("SELECT * FROM planned_meals_table WHERE scheduledDate = :date")
    LiveData<List<PlannedMeal>> getPlannedMealsForDate(String date);

    @Query("SELECT EXISTS(SELECT 1 FROM planned_meals_table WHERE id = :mealId AND scheduledDate = :date)")
    boolean isMealPlanned(String mealId, String date);

    @Query("DELETE FROM planned_meals_table")
    void deleteAllPlannedMeals();
}