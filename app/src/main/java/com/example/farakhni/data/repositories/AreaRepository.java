package com.example.farakhni.data.repositories;

import com.example.farakhni.data.network.NetworkCallBack;
import com.example.farakhni.model.Area;
import java.util.List;

public interface AreaRepository {
    void getAllAreas(NetworkCallBack<List<Area>> callback);
}


