package com.example.farakhni.data.network.category;

import com.example.farakhni.model.Category;
import com.example.farakhni.data.network.NetworkCallBack;
import java.util.List;

public interface CategoryRemoteDataSoruce {
    void makeNetworkCall( NetworkCallBack<List<Category>> networkCallBack);
}
