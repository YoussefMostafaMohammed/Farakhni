package com.example.farakhni.freatures.Search;

import com.example.farakhni.data.network.NetworkCallBack;
import com.example.farakhni.data.repositories.AreaRepositoryImpl;
import com.example.farakhni.data.repositories.CategoryRepositoryImpl;
import com.example.farakhni.data.repositories.IngredientRepositoryImpl;
import com.example.farakhni.data.repositories.MealRepositoryImpl;
import com.example.farakhni.model.Area;
import com.example.farakhni.model.Category;
import com.example.farakhni.model.FavoriteMeal;
import com.example.farakhni.model.Ingredient;
import com.example.farakhni.model.Meal;

import java.util.ArrayList;
import java.util.List;

public class SearchModel implements SearchContract.Model {
    private final MealRepositoryImpl mealRepository;
    private final AreaRepositoryImpl areaRepository;
    private final CategoryRepositoryImpl categoryRepository;
    private final IngredientRepositoryImpl ingredientRepository;

    public SearchModel(MealRepositoryImpl mealRepository,
                       AreaRepositoryImpl areaRepository,
                       CategoryRepositoryImpl categoryRepository,
                       IngredientRepositoryImpl ingredientRepository) {
        this.mealRepository = mealRepository;
        this.areaRepository = areaRepository;
        this.categoryRepository = categoryRepository;
        this.ingredientRepository = ingredientRepository;
    }

    @Override
    public void getMealsByName(String name, NetworkCallBack<List<Meal>> callback) {
        mealRepository.searchMealsByName(name, callback);
    }

    @Override
    public void getMealsByFirstLetter(String letter, NetworkCallBack<List<Meal>> callback) {
        mealRepository.getMealsByFirstLetter(letter, callback);
    }

    @Override
    public void getMealById(String id, NetworkCallBack<List<Meal>> callback) {
        mealRepository.getMealById(id, callback);
    }

    @Override
    public void getAllAreas(NetworkCallBack<List<Area>> callback) {
        areaRepository.getAllAreas(callback);
    }

    @Override
    public void getAllCategories(NetworkCallBack<List<Category>> callback) {
        categoryRepository.getAllCategories(callback);
    }

    @Override
    public void getAllIngredients(NetworkCallBack<List<Ingredient>> callback) {
        ingredientRepository.getAllIngredients(callback);
    }

    @Override
    public void insertFavoriteMeal(FavoriteMeal meal) {
        mealRepository.insertFavoriteMeal(meal);
    }

    @Override
    public void deleteFavoriteMeal(FavoriteMeal meal) {
        mealRepository.deleteFavoriteMeal(meal);
    }

    @Override
    public List<Area> filterAreas(List<Area> areas, String query) {
        List<Area> filteredList = new ArrayList<>();
        String lowerCaseQuery = query.toLowerCase();
        for (Area area : areas) {
            if (area.getArea() != null &&
                    area.getArea().toLowerCase().contains(lowerCaseQuery)) {
                filteredList.add(area);
            }
        }
        return filteredList;
    }

    @Override
    public List<Category> filterCategories(List<Category> categories, String query) {
        List<Category> filteredList = new ArrayList<>();
        String lowerCaseQuery = query.toLowerCase();
        for (Category category : categories) {
            if (category.getCategory() != null &&
                    category.getCategory().toLowerCase().contains(lowerCaseQuery)) {
                filteredList.add(category);
            }
        }
        return filteredList;
    }

    @Override
    public List<Ingredient> filterIngredients(List<Ingredient> ingredients, String query) {
        List<Ingredient> filteredList = new ArrayList<>();
        String lowerCaseQuery = query.toLowerCase();
        for (Ingredient ingredient : ingredients) {
            if (ingredient.getIngredient() != null &&
                    ingredient.getIngredient().toLowerCase().contains(lowerCaseQuery)) {
                filteredList.add(ingredient);
            }
        }
        return filteredList;
    }

    @Override
    public androidx.lifecycle.LiveData<List<FavoriteMeal>> getFavoriteMeals() {
        return mealRepository.getFavoriteMeals();
    }
}