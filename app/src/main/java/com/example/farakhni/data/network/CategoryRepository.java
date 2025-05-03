package com.example.farakhni.data.network;

import com.example.farakhni.data.network.NetworkCallBack;
import com.example.farakhni.model.Category;
import java.util.List;

public interface CategoryRepository {
    void getAllCategories(NetworkCallBack<List<Category>> callback);
}