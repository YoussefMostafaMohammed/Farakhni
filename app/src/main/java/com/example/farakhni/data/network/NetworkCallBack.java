package com.example.farakhni.data.network;

import com.example.farakhni.model.Category;

import java.util.List;

public interface NetworkCallBack<T> {
    public void onSuccessResult(T meals);
    public void onFailureResult(String FailureMessage);
}
