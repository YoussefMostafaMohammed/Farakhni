package com.example.farakhni.data.network.area;


import com.example.farakhni.model.AreaListResponse;
import com.example.farakhni.model.Area;
import com.example.farakhni.data.network.NetworkCallBack;
import com.example.farakhni.data.network.RetrofitClient;

import java.util.ArrayList;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AreasRemoteDataSourceImpl implements AreasRemoteDataSoruce{
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
    public void makeNetworkCall( NetworkCallBack<List<Area>> networkCallBack) {
        List<Area> result = new ArrayList<>();
        Call<AreaListResponse> call=  areaService.getAllAreas("list");
        call.enqueue(new Callback<AreaListResponse>() {
            @Override
            public void onResponse(Call<AreaListResponse> call, Response<AreaListResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    result.addAll(response.body().getAllAreas());
                    networkCallBack.onSuccessResult(response.body().getAllAreas());
                }
            }

            @Override
            public void onFailure(Call<AreaListResponse> call, Throwable t) {
                networkCallBack.onFailureResult(t.getMessage());
            }
        });
    }
}
