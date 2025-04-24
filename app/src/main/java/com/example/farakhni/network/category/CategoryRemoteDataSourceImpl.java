package com.example.farakhni.network.category;


import com.example.farakhni.model.CategoryListResponse;
import com.example.farakhni.model.Category;
import com.example.farakhni.network.NetworkCallBack;
import com.example.farakhni.network.RetrofitClient;
import com.example.farakhni.network.ingredient.IngredientService;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class CategoryRemoteDataSourceImpl implements CategoryRemoteDataSoruce {
    private static final String BASE_URL="www.themealdb.com/api/json/v1/1/";
    private static CategoryService  categoryService =null;

    private static CategoryRemoteDataSourceImpl categoryRemoteDataSource;
    private CategoryRemoteDataSourceImpl(){
        categoryService = RetrofitClient.getService(CategoryService.class);
    }

    public static CategoryRemoteDataSourceImpl getInstance(){
        if(categoryService==null){
            categoryRemoteDataSource= new CategoryRemoteDataSourceImpl();
        }
        return categoryRemoteDataSource;
    }
    @Override
    public void makeNetworkCall(String strCategory, NetworkCallBack<List<Category>> networkCallBack) {
        List<Category> result = new ArrayList<>();
        Call<CategoryListResponse> call=  categoryService.getCategories(strCategory);
        call.enqueue(new Callback<CategoryListResponse>() {
            @Override
            public void onResponse(Call<CategoryListResponse> call, Response<CategoryListResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    result.addAll(response.body().getProducts());
                    networkCallBack.onSuccessResult(response.body().getProducts());
                }
            }
            @Override
            public void onFailure(Call<CategoryListResponse> call, Throwable t) {
                networkCallBack.onFailureResult(t.getMessage());
            }
        });
    }
}
