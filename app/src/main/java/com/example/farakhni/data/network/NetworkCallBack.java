package com.example.farakhni.data.network;

public interface NetworkCallBack<T> {

    // Success callback
    void onSuccessResult(T result);
    // Failure callback with error message
    void onFailureResult(String failureMessage);

    // Callback for loading state (e.g., showing a loading spinner)
    void onLoading();

    // Callback for network error (e.g., no internet connection)
    void onNetworkError(String errorMessage);

    // Callback for empty data (e.g., no meals found)
    void onEmptyData();

    // Optional callback for progress updates (e.g., when fetching large data)
    void onProgress(int progress);
}
