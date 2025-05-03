package com.example.farakhni.data.DB;
import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import com.example.farakhni.model.Meal;
import java.util.List;

@Dao
public interface FavoriteMealDAO {
    @Query("SELECT * FROM fav_meals_table")
    public LiveData<List<Meal>> getFavoriteMealsForUser();
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public void insertMeal(Meal meal);
    @Delete
    public void deleteMeal(Meal meal);
}
