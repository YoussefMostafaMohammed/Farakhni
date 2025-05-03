package com.example.farakhni.data.network.ingredient;

import com.example.farakhni.model.Ingredient;
import com.example.farakhni.data.network.NetworkCallBack;

import java.util.List;

public interface IngredientsRemoteDataSoruce {
    void makeNetworkCall(NetworkCallBack<List<Ingredient>> networkCallBack);
}
