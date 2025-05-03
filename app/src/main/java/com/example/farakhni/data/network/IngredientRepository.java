package com.example.farakhni.data.network;

import com.example.farakhni.data.network.NetworkCallBack;
import com.example.farakhni.model.Ingredient;
import java.util.List;

public interface IngredientRepository {
    void getAllIngredients(NetworkCallBack<List<Ingredient>> callback);
}
