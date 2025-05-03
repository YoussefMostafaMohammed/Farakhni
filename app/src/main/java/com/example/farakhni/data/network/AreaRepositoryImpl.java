package com.example.farakhni.data.network;

import com.example.farakhni.data.network.NetworkCallBack;
import com.example.farakhni.data.network.area.AreasRemoteDataSoruce;
import com.example.farakhni.model.Area;
import java.util.List;

public class AreaRepositoryImpl implements AreaRepository {
    private final AreasRemoteDataSoruce remoteDataSource;
    private static AreaRepositoryImpl instance;

    private AreaRepositoryImpl(AreasRemoteDataSoruce remoteDataSource) {
        this.remoteDataSource = remoteDataSource;
    }

    public static synchronized AreaRepositoryImpl getInstance(
            AreasRemoteDataSoruce remoteDataSource) {
        if (instance == null) {
            instance = new AreaRepositoryImpl(remoteDataSource);
        }
        return instance;
    }

    @Override
    public void getAllAreas(NetworkCallBack<List<Area>> callback) {
        remoteDataSource.makeNetworkCall(callback);
    }
}