package com.example.farakhni.network.ingredient;

import com.example.farakhni.model.Ingredient;
import com.example.farakhni.network.NetworkCallBack;

import java.util.List;

public interface IngredientsRemoteDataSoruce {
    void makeNetworkCall(String strIngredient, NetworkCallBack<List<Ingredient>> networkCallBack);
}
