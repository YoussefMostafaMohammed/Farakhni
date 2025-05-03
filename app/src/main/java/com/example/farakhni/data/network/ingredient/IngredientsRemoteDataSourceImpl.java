package com.example.farakhni.data.network.ingredient;

import com.example.farakhni.model.IngredientListResponse;
import com.example.farakhni.model.Ingredient;
import com.example.farakhni.data.network.NetworkCallBack;
import com.example.farakhni.data.network.RetrofitClient;

import java.util.ArrayList;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class IngredientsRemoteDataSourceImpl implements IngredientsRemoteDataSource{
    private static IngredientService  ingredientService =null;

    private static IngredientsRemoteDataSourceImpl productsRemoteDataSource;
    private IngredientsRemoteDataSourceImpl(){
        ingredientService = RetrofitClient.getService(IngredientService.class);
    }

    public static IngredientsRemoteDataSourceImpl getInstance(){
        if(ingredientService==null){
            productsRemoteDataSource= new IngredientsRemoteDataSourceImpl();
        }
        return productsRemoteDataSource;
    }
    @Override
    public void makeNetworkCall(NetworkCallBack<List<Ingredient>> networkCallBack) {
        List<Ingredient> result = new ArrayList<>();
        Call<IngredientListResponse> call=  ingredientService.getIngredients("list");
        call.enqueue(new Callback<IngredientListResponse>() {
            @Override
            public void onResponse(Call<IngredientListResponse> call, Response<IngredientListResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    result.addAll(response.body().getALlIngredients());
                    networkCallBack.onSuccessResult(response.body().getALlIngredients());
                }
            }
            @Override
            public void onFailure(Call<IngredientListResponse> call, Throwable t) {
                networkCallBack.onFailureResult(t.getMessage());
            }
        });
    }
}
