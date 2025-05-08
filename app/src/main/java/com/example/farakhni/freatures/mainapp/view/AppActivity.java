package com.example.farakhni.freatures.mainapp.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.widget.Toast;

import com.example.farakhni.R;
import com.example.farakhni.welcome.WelcomeScreen;
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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class AppActivity extends AppCompatActivity implements AppContract.View {
    private ActivityAppScreenBinding binding;
    private AppContract.Presenter presenter;
    private AppBarConfiguration mAppBarConfiguration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        binding = ActivityAppScreenBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        presenter = new AppPresenter(new AppModel(this));
        presenter.attachView(this);

        setSupportActionBar(binding.appBarAppScreen.toolbar);

        DrawerLayout drawer = binding.drawerLayout;
        NavigationView navView = binding.navView;
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_favorite, R.id.nav_calender, R.id.nav_search)
                .setOpenableLayout(drawer).build();

        NavController navController = Navigation.findNavController(this,
                R.id.nav_host_fragment_content_app_screen);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);

        // Set up logout button
        navView.findViewById(R.id.logoutButton).setOnClickListener(v -> presenter.onLogoutClicked());

        // Restrict navigation for guest users
        Menu menu = navView.getMenu();
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser == null) {
            menu.findItem(R.id.nav_favorite).setVisible(false);
            menu.findItem(R.id.nav_calender).setVisible(false);
        } else {
            menu.findItem(R.id.nav_favorite).setVisible(true);
            menu.findItem(R.id.nav_calender).setVisible(true);
        }

        navController.addOnDestinationChangedListener((controller, destination, arguments) -> {
            if (destination.getId() == R.id.nav_favorite || destination.getId() == R.id.nav_calender) {
                if (FirebaseAuth.getInstance().getCurrentUser() == null) {
                    Toast.makeText(this, "Please log in to access this feature", Toast.LENGTH_SHORT).show();
                    navController.navigate(R.id.nav_home);
                }
            }
        });

        if (currentUser != null) {
            presenter.onNavigationReady(currentUser.getUid());
        } else {
            presenter.onNavigationReady(null);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.app_screen, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this,
                R.id.nav_host_fragment_content_app_screen);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    @Override
    public void setupNavDrawer() {
        // Nothing extra needed
    }



    @Override
    public void showSyncError(String errorMessage) {
        Toast.makeText(this, "Sync error: " + errorMessage, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void navigateToLoginScreen() {
        // Sign out from Firebase
        FirebaseAuth.getInstance().signOut();

        // Navigate to WelcomeScreen
        Intent intent = new Intent(this, WelcomeScreen.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onPause() {
        super.onPause();
        AppDataBase.getInstance(this).forceCheckpoint();
    }

    @Override
    protected void onDestroy() {
        presenter.detachView();
        super.onDestroy();
    }
}