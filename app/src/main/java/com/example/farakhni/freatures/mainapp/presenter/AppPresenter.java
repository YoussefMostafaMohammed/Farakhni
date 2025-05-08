package com.example.farakhni.freatures.mainapp.presenter;

import com.example.farakhni.freatures.mainapp.AppContract;

public class AppPresenter implements AppContract.Presenter {
    private AppContract.View view;
    private final AppContract.Model model;

    public AppPresenter(AppContract.Model model) {
        this.model = model;
    }

    @Override
    public void attachView(AppContract.View view) {
        this.view = null;
        this.view = view;
    }

    @Override
    public void detachView() {
        this.view = null;
    }


    @Override
    public void onNavigationReady(String userId) {
        if (view != null) {
            view.setupNavDrawer();
            if (userId != null) {
                model.syncData(new AppContract.Model.OnSyncCompleteListener() {
                    @Override
                    public void onSyncSuccess() {
                        // No action needed on success
                    }

                    @Override
                    public void onSyncFailure(Exception e) {
                        if (view != null) {
                            view.showSyncError(e.getMessage());
                        }
                    }
                });
            }
        }
    }

    @Override
    public void onLogoutClicked() {
        if (view != null) {
            model.clearDatabase(new AppContract.Model.OnClearDatabaseListener() {
                @Override
                public void onClearSuccess() {
                    view.navigateToLoginScreen();
                }

                @Override
                public void onClearFailure(Exception e) {
                    view.navigateToLoginScreen(); // Proceed to login even if clearing fails
                }
            });
        }
    }
}