package com.example.farakhni.data.repositories;
import com.example.farakhni.data.network.NetworkCallBack;
import com.example.farakhni.data.network.category.CategoryRemoteDataSoruce;
import com.example.farakhni.model.Category;
import java.util.List;

public class CategoryRepositoryImpl implements CategoryRepository {
    private final CategoryRemoteDataSoruce remoteDataSource;
    private static CategoryRepositoryImpl instance;

    private CategoryRepositoryImpl(CategoryRemoteDataSoruce remoteDataSource) {
        this.remoteDataSource = remoteDataSource;
    }

    public static synchronized CategoryRepositoryImpl getInstance(
            CategoryRemoteDataSoruce remoteDataSource) {
        if (instance == null) {
            instance = new CategoryRepositoryImpl(remoteDataSource);
        }
        return instance;
    }

    @Override
    public void getAllCategories(NetworkCallBack<List<Category>> callback) {
        remoteDataSource.makeNetworkCall(callback);
    }
}