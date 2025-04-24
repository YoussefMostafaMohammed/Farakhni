package com.example.farakhni.network;

import com.example.farakhni.model.Category;

import java.util.List;

public interface NetworkCallBack<T> {
    public void onSuccessResult(T products);
    public void onFailureResult(String FailureMessage);
}
