package com.example.farakhni.freatures.mainapp;

public interface AppContract {
    interface View {
        void setupNavDrawer();
        void showSyncError(String message);
        void navigateToLoginScreen();
    }

    interface Presenter {
        void attachView(View view);
        void detachView();
        void onNavigationReady(String userId);
        void onLogoutClicked();
    }

    interface Model {
        void syncData(OnSyncCompleteListener listener);
        void clearDatabase(OnClearDatabaseListener listener);

        interface OnSyncCompleteListener {
            void onSyncSuccess();
            void onSyncFailure(Exception e);
        }

        interface OnClearDatabaseListener {
            void onClearSuccess();
            void onClearFailure(Exception e);
        }
    }
}