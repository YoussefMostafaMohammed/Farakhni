package com.example.farakhni.data.network.category;


import com.example.farakhni.model.CategoryListResponse;
import com.example.farakhni.model.Category;
import com.example.farakhni.data.network.NetworkCallBack;
import com.example.farakhni.data.network.RetrofitClient;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CategoryRemoteDataSourceImpl implements CategoryRemoteDataSoruce {
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
    public void makeNetworkCall( NetworkCallBack<List<Category>> networkCallBack) {
        List<Category> result = new ArrayList<>();
        Call<CategoryListResponse> call=  categoryService.getCategories();
        call.enqueue(new Callback<CategoryListResponse>() {
            @Override
            public void onResponse(Call<CategoryListResponse> call, Response<CategoryListResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    result.addAll(response.body().getAllCategories());
                    networkCallBack.onSuccessResult(response.body().getAllCategories());
                }
            }
            @Override
            public void onFailure(Call<CategoryListResponse> call, Throwable t) {
                networkCallBack.onFailureResult(t.getMessage());
            }
        });
    }
}
