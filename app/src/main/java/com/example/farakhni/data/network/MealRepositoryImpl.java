package com.example.farakhni.data.network;
import androidx.lifecycle.LiveData;
import com.example.farakhni.data.DB.FavoriteMealsLocalDataSource;
import com.example.farakhni.data.network.meal.MealsRemoteDataSoruce;
import com.example.farakhni.data.network.NetworkCallBack;
import com.example.farakhni.model.Meal;
import java.util.List;

public class MealRepositoryImpl implements MealRepository {
    private final MealsRemoteDataSoruce remoteDataSource;
    private final FavoriteMealsLocalDataSource localDataSource;
    private static MealRepositoryImpl instance;

    private MealRepositoryImpl(MealsRemoteDataSoruce remoteDataSource,
                               FavoriteMealsLocalDataSource localDataSource) {
        this.remoteDataSource = remoteDataSource;
        this.localDataSource = localDataSource;
    }

    public static synchronized MealRepositoryImpl getInstance(
            MealsRemoteDataSoruce remoteDataSource,
            FavoriteMealsLocalDataSource localDataSource) {
        if (instance == null) {
            instance = new MealRepositoryImpl(remoteDataSource, localDataSource);
        }
        return instance;
    }

    @Override
    public LiveData<List<Meal>> getFavoriteMeals() {
        return localDataSource.getFavoriteMeals();
    }

    @Override
    public void getMealById(String mealId, NetworkCallBack<List<Meal>> callback) {
        remoteDataSource.makeNetworkCallgetMealByID(mealId, callback);
    }

    @Override
    public void searchMealsByName(String mealName, NetworkCallBack<List<Meal>> callback) {
        remoteDataSource.makeNetworkCallgetMealByName(mealName, callback);
    }

    @Override
    public void getRandomMeal(NetworkCallBack<List<Meal>> callback) {
        remoteDataSource.makeNetworkCallgetRandomMeal(callback);
    }

    @Override
    public void getMealsByFirstLetter(String firstLetter, NetworkCallBack<List<Meal>> callback) {
        remoteDataSource.makeNetworkCallgetMealByFirstLetter(firstLetter, callback);
    }

    @Override
    public void filterByIngredient(String ingredient, NetworkCallBack<List<Meal>> callback) {
        remoteDataSource.makeNetworkCallFilterByIngredient(ingredient, callback);
    }

    @Override
    public void filterByCategory(String category, NetworkCallBack<List<Meal>> callback) {
        remoteDataSource.makeNetworkCallFilterByCategory(category, callback);
    }

    @Override
    public void filterByArea(String area, NetworkCallBack<List<Meal>> callback) {
        remoteDataSource.makeNetworkCallFilterByArea(area, callback);
    }

    @Override
    public void insertFavoriteMeal(Meal meal) {
        localDataSource.insertMeal(meal);
    }

    @Override
    public void deleteFavoriteMeal(Meal meal) {
        localDataSource.deleteMeal(meal);
    }
}