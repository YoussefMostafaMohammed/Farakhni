package com.example.farakhni.data.repositories;

import com.example.farakhni.data.network.NetworkCallBack;
import com.example.farakhni.data.network.ingredient.IngredientsRemoteDataSourceImpl;
import com.example.farakhni.model.Ingredient;
import java.util.List;

public class IngredientRepositoryImpl implements IngredientRepository {
    private final IngredientsRemoteDataSourceImpl remoteDataSource;
    private static IngredientRepositoryImpl instance;

    private IngredientRepositoryImpl(IngredientsRemoteDataSourceImpl remoteDataSource) {
        this.remoteDataSource = remoteDataSource;
    }

    public static synchronized IngredientRepositoryImpl getInstance() {
        if (instance == null) {
            instance = new IngredientRepositoryImpl(IngredientsRemoteDataSourceImpl.getInstance());
        }
        return instance;
    }

    @Override
    public void getAllIngredients(NetworkCallBack<List<Ingredient>> callback) {
        remoteDataSource.makeNetworkCall(callback);
    }
}