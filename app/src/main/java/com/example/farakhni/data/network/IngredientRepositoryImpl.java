package com.example.farakhni.data.network;

import com.example.farakhni.data.network.NetworkCallBack;
import com.example.farakhni.data.network.ingredient.IngredientsRemoteDataSoruce;
import com.example.farakhni.model.Ingredient;
import java.util.List;

public class IngredientRepositoryImpl implements IngredientRepository {
    private final IngredientsRemoteDataSoruce remoteDataSource;
    private static IngredientRepositoryImpl instance;

    private IngredientRepositoryImpl(IngredientsRemoteDataSoruce remoteDataSource) {
        this.remoteDataSource = remoteDataSource;
    }

    public static synchronized IngredientRepositoryImpl getInstance(
            IngredientsRemoteDataSoruce remoteDataSource) {
        if (instance == null) {
            instance = new IngredientRepositoryImpl(remoteDataSource);
        }
        return instance;
    }

    @Override
    public void getAllIngredients(NetworkCallBack<List<Ingredient>> callback) {
        remoteDataSource.makeNetworkCall(callback);
    }
}