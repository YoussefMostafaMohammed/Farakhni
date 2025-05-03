package com.example.farakhni.data.repositories;
import com.example.farakhni.data.network.NetworkCallBack;
import com.example.farakhni.data.network.category.CategoryRemoteDataSoruce;
import com.example.farakhni.data.network.category.CategoryRemoteDataSoruceImpl;
import com.example.farakhni.model.Category;
import java.util.List;

public class CategoryRepositoryImpl implements CategoryRepository {
    private final CategoryRemoteDataSoruceImpl remoteDataSource;
    private static CategoryRepositoryImpl instance;

    private CategoryRepositoryImpl(CategoryRemoteDataSoruceImpl remoteDataSource) {
        this.remoteDataSource = remoteDataSource;
    }

    public static synchronized CategoryRepositoryImpl getInstance(){
        if (instance == null) {
            instance = new CategoryRepositoryImpl(CategoryRemoteDataSoruceImpl.getInstance());
        }
        return instance;
    }

    @Override
    public void getAllCategories(NetworkCallBack<List<Category>> callback) {
        remoteDataSource.makeNetworkCall(callback);
    }
}