package com.example.farakhni.freatures.mainapp.presenter;


import com.example.farakhni.freatures.mainapp.AppContract;

public class AppPresenter implements AppContract.Presenter {
    private AppContract.View view;
    public AppPresenter(AppContract.Model model) { /* no state */ }
    @Override
    public void attachView(AppContract.View v) {
        view=v;
    }
    @Override public void detachView() { view = null; }
    @Override public void onFabClicked() {
        if (view!=null) view.showSnackbar("Replace with your own action");
    }
    @Override public void onNavigationReady() {
        if (view!=null) view.setupNavDrawer();
    }
}
