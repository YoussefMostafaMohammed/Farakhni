package com.example.farakhni.network.area;
import com.example.farakhni.model.Area;
import com.example.farakhni.network.NetworkCallBack;

import java.util.List;

public interface AreasRemoteDataSoruce {
    void makeNetworkCall(String strArea, NetworkCallBack<List<Area>> networkCallBack);
}
