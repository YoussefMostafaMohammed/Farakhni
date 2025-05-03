package com.example.farakhni.freatures.mainapp.view;

import android.os.Bundle;
import android.view.Menu;

import com.example.farakhni.R;
import com.example.farakhni.data.DB.AppDataBase;
import com.example.farakhni.databinding.ActivityAppScreenBinding;
import com.example.farakhni.freatures.mainapp.AppContract;
import com.example.farakhni.freatures.mainapp.model.AppModel;
import com.example.farakhni.freatures.mainapp.presenter.AppPresenter;
import com.google.android.material.snackbar.Snackbar;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import com.google.android.material.navigation.NavigationView;

public class AppActivity extends AppCompatActivity implements AppContract.View {
    private ActivityAppScreenBinding binding;
    private AppContract.Presenter presenter;
    private AppBarConfiguration mAppBarConfiguration;

    @Override protected void onCreate(Bundle s) {
        super.onCreate(s);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        binding = ActivityAppScreenBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        presenter = new AppPresenter(new AppModel());
        presenter.attachView(this);

        setSupportActionBar(binding.appBarAppScreen.toolbar);
        binding.appBarAppScreen.fab.setOnClickListener(v -> presenter.onFabClicked());

        DrawerLayout drawer = binding.drawerLayout;
        NavigationView navView = binding.navView;
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow)
                .setOpenableLayout(drawer).build();

        NavController navController = Navigation.findNavController(this,
                R.id.nav_host_fragment_content_app_screen);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);

        presenter.onNavigationReady();
    }

    @Override public boolean onCreateOptionsMenu(Menu m) {
        getMenuInflater().inflate(R.menu.app_screen, m);
        return true;
    }
    @Override public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this,
                R.id.nav_host_fragment_content_app_screen);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }
    @Override public void setupNavDrawer() { /* nothing extra */ }
    @Override public void showSnackbar(String msg) {
        Snackbar.make(binding.appBarAppScreen.fab, msg, Snackbar.LENGTH_LONG)
                .setAnchorView(binding.appBarAppScreen.fab).show();
    }
    @Override protected void onPause() {
        super.onPause();
        AppDataBase.getInstance(this).forceCheckpoint();
    }
    @Override protected void onDestroy() {
        presenter.detachView();
        super.onDestroy();
    }
}