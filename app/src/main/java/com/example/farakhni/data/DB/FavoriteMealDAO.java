package com.example.farakhni.data.DB;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.farakhni.model.FavoriteMeal;

import java.util.List;

@Dao
public interface FavoriteMealDAO {
    @Query("SELECT * FROM favorite_meals_table")
    LiveData<List<FavoriteMeal>> getFavoriteMealsForUser();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertMeal(FavoriteMeal meal);

    @Delete
    void deleteMeal(FavoriteMeal meal);

    @Query("SELECT EXISTS(SELECT 1 FROM favorite_meals_table WHERE id = :mealId)")
    boolean isFavorite(String mealId);

    @Query("DELETE FROM favorite_meals_table")
    void deleteAllFavoriteMeals();
}