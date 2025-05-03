package com.example.farakhni.data.network;

public interface NetworkCallBack<T> {
    public void onSuccessResult(T meals);
    public void onFailureResult(String FailureMessage);
}
