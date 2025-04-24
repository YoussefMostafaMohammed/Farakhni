package com.example.farakhni.network.area;


import com.example.farakhni.model.AreaListResponse;
import com.example.farakhni.model.Area;
import com.example.farakhni.network.NetworkCallBack;
import com.example.farakhni.network.RetrofitClient;

import java.util.ArrayList;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class AreasRemoteDataSourceImpl implements AreasRemoteDataSoruce{
    private static final String BASE_URL = "www.themealdb.com/api/json/v1/1/";
    private static AreaService  areaService = null;
    private static AreasRemoteDataSourceImpl areasRemoteDataSource;

    private AreasRemoteDataSourceImpl() {
        areaService = RetrofitClient.getService(AreaService.class);
    }
        public static AreasRemoteDataSourceImpl getInstance(){
        if(areaService==null){
            areasRemoteDataSource= new AreasRemoteDataSourceImpl();
        }
        return areasRemoteDataSource;
    }
    @Override
    public void makeNetworkCall(String strArea, NetworkCallBack<List<Area>> networkCallBack) {
        List<Area> result = new ArrayList<>();
        Call<AreaListResponse> call=  areaService.getAllAreas(strArea);
        call.enqueue(new Callback<AreaListResponse>() {
            @Override
            public void onResponse(Call<AreaListResponse> call, Response<AreaListResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    result.addAll(response.body().getProducts());
                    networkCallBack.onSuccessResult(response.body().getProducts());
                }
            }

            @Override
            public void onFailure(Call<AreaListResponse> call, Throwable t) {
                networkCallBack.onFailureResult(t.getMessage());
            }
        });
    }
}
