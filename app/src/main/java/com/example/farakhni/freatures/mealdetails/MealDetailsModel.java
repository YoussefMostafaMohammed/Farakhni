package com.example.farakhni.freatures.mealdetails;

import com.example.farakhni.data.repositories.MealRepositoryImpl;
import com.example.farakhni.model.FavoriteMeal;
import com.example.farakhni.model.PlannedMeal;

public class MealDetailsModel implements MealDetailsContract.Model {
    private final MealRepositoryImpl repository;

    public MealDetailsModel(MealRepositoryImpl repository) {
        this.repository = repository;
    }

    @Override
    public boolean isFavorite(String mealId) {
        return repository.isFavorite(mealId);
    }

    @Override
    public boolean isMealPlanned(String mealId, String date) {
        return repository.isMealPlanned(mealId, date);
    }

    @Override
    public void insertFavoriteMeal(FavoriteMeal meal) {
        repository.insertFavoriteMeal(meal);
    }

    @Override
    public void deleteFavoriteMeal(FavoriteMeal meal) {
        repository.deleteFavoriteMeal(meal);
    }

    @Override
    public void insertPlannedMeal(String mealId, String name, String date) {
        repository.insertPlannedMeal(new PlannedMeal(mealId, name, date));
    }

    @Override
    public void deletePlannedMeal(String mealId, String name, String date) {
        repository.deletePlannedMeal(new PlannedMeal(mealId, name, date));
    }
}