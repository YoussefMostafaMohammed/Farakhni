// com/example/farakhni/freatures/FilterBy/FilterByModel.java
package com.example.farakhni.freatures.FilterBy;

import android.content.Context;
import com.example.farakhni.data.repositories.MealRepositoryImpl;
import com.example.farakhni.model.Meal;

public class FilterByModel implements FilterByContract.Model {
    private final MealRepositoryImpl repo;

    public FilterByModel(Context ctx) {
        this.repo = MealRepositoryImpl.getInstance(ctx);
    }

    @Override
    public void saveFavorite(Meal meal) {
        repo.insertFavoriteMeal(meal);
    }

    @Override
    public void deleteFavorite(Meal meal) {
        repo.deleteFavoriteMeal(meal);
    }
}
