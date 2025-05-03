package com.example.farakhni.freatures.mainapp;

public interface AppContract {
    interface View {
        void setupNavDrawer();
        void showSnackbar(String msg);
    }
    interface Presenter {
        void attachView(View v);
        void detachView();
        void onFabClicked();
        void onNavigationReady();
    }
    interface Model {
    }
}
