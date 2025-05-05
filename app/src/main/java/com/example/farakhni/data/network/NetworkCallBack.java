package com.example.farakhni.data.network;

public interface NetworkCallBack<T> {
    void onSuccessResult(T result);
    void onFailureResult(String failureMessage);
    void onLoading();
    void onNetworkError(String errorMessage);
    void onEmptyData();
    void onProgress(int progress);
}