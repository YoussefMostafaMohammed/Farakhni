package com.example.farakhni.data.DB;

import androidx.lifecycle.LiveData;
import com.example.farakhni.model.PlannedMeal;
import java.util.List;

public interface PlannedMealsLocalDataSource {
    LiveData<List<PlannedMeal>> getAllPlannedMeals();
    LiveData<List<PlannedMeal>> getPlannedMealsForDate(String date);
    void insertPlannedMeal(PlannedMeal plannedMeal);
    void deletePlannedMeal(PlannedMeal plannedMeal);
    boolean isMealPlanned(String mealId, String date);
    void syncPlannedMealsWithFirestore(OnSyncCompleteListener listener);

    interface OnSyncCompleteListener {
        void onSyncSuccess();
        void onSyncFailure(Exception e);
    }
}