package com.example.farakhni.network.ingredient;

import com.example.farakhni.model.IngredientListResponse;
import com.example.farakhni.model.Ingredient;
import com.example.farakhni.network.NetworkCallBack;
import com.example.farakhni.network.RetrofitClient;

import java.util.ArrayList;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class IngredientsRemoteDataSourceImpl implements IngredientsRemoteDataSoruce{
    private static final String BASE_URL="https://www.themealdb.com/api/json/v1/1/";
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
    public void makeNetworkCall(String strIngredients, NetworkCallBack<List<Ingredient>> networkCallBack) {
        List<Ingredient> result = new ArrayList<>();
        Call<IngredientListResponse> call=  ingredientService.getIngredients(strIngredients);
        call.enqueue(new Callback<IngredientListResponse>() {
            @Override
            public void onResponse(Call<IngredientListResponse> call, Response<IngredientListResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    result.addAll(response.body().getProducts());
                    networkCallBack.onSuccessResult(response.body().getProducts());
                }
            }
            @Override
            public void onFailure(Call<IngredientListResponse> call, Throwable t) {
                networkCallBack.onFailureResult(t.getMessage());
            }
        });
    }
}
