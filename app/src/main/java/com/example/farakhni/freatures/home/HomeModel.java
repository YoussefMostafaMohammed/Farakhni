package com.example.farakhni.freatures.home;

import com.example.farakhni.data.network.NetworkCallBack;
import com.example.farakhni.data.repositories.AreaRepository;
import com.example.farakhni.data.repositories.CategoryRepository;
import com.example.farakhni.data.repositories.IngredientRepository;
import com.example.farakhni.data.repositories.MealRepository;
import com.example.farakhni.model.Area;
import com.example.farakhni.model.Category;
import com.example.farakhni.model.Ingredient;
import com.example.farakhni.model.Meal;

import java.util.List;

public class HomeModel implements HomeContract.Model {
    private final MealRepository mealRepo;
    private final IngredientRepository ingredientRepo;
    private final CategoryRepository categoryRepo;
    private final AreaRepository areaRepository;


    public HomeModel(MealRepository mealRepo,
                     IngredientRepository ingredientRepo,
                     CategoryRepository categoryRepo, AreaRepository areaRepository) {
        this.mealRepo = mealRepo;
        this.ingredientRepo = ingredientRepo;
        this.categoryRepo = categoryRepo;
        this.areaRepository = areaRepository;
    }

    @Override
    public void getRandomMeal(NetworkCallBack<List<Meal>> callback) {
        mealRepo.getRandomMeal(callback);
    }

    @Override
    public void getAllIngredients(NetworkCallBack<List<Ingredient>> callback) {
        ingredientRepo.getAllIngredients(callback);
    }

    @Override
    public void getAllCategories(NetworkCallBack<List<Category>> callback) {
        categoryRepo.getAllCategories(callback);
    }

    @Override
    public void getAllAreas(NetworkCallBack<List<Area>> callback) {
        areaRepository.getAllAreas(callback);
    }
}

