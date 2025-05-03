package com.example.farakhni.data.repositories;

import com.example.farakhni.data.network.NetworkCallBack;
import com.example.farakhni.data.network.area.AreasRemoteDataSoruce;
import com.example.farakhni.data.network.area.AreasRemoteDataSourceImpl;
import com.example.farakhni.model.Area;
import java.util.List;

public class AreaRepositoryImpl implements AreaRepository {
    private final AreasRemoteDataSourceImpl areasRemoteDataSource;
    private static AreaRepositoryImpl instance;

    private AreaRepositoryImpl(AreasRemoteDataSourceImpl remoteDataSource) {
        this.areasRemoteDataSource = remoteDataSource;
    }

    public static synchronized AreaRepositoryImpl getInstance() {
        if (instance == null) {
            instance = new AreaRepositoryImpl(AreasRemoteDataSourceImpl.getInstance());
        }
        return instance;
    }

    @Override
    public void getAllAreas(NetworkCallBack<List<Area>> callback) {
        areasRemoteDataSource.makeNetworkCall(callback);
    }
}