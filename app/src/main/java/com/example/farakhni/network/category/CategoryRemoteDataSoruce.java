package com.example.farakhni.network.category;

import com.example.farakhni.model.Category;
import com.example.farakhni.network.NetworkCallBack;
import java.util.List;

public interface CategoryRemoteDataSoruce {
    void makeNetworkCall( NetworkCallBack<List<Category>> networkCallBack);
}
