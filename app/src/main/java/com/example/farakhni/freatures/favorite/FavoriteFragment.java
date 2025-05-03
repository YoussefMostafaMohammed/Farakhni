package com.example.farakhni.freatures.favorite;
import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.farakhni.data.DB.FavoriteMealDAO;
import com.example.farakhni.data.DB.AppDataBase;
import com.example.farakhni.databinding.FragmentFavoriteBinding;
import com.example.farakhni.model.Meal;
import com.example.farakhni.common.MealAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.List;

public class FavoriteFragment extends Fragment {
    private FragmentFavoriteBinding binding;
    private MealAdapter mealAdapter;
    private RecyclerView favoriteMealsRecyclerView;
    AppDataBase favoriteMealDataBase;
    List<Meal> mealList = new ArrayList<>();
    FavoriteMealDAO favoriteMealDAO;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        FavoriteViewModel galleryViewModel = new ViewModelProvider(this).get(FavoriteViewModel.class);
        binding = FragmentFavoriteBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        mealAdapter = new MealAdapter(requireContext(), mealList);
        favoriteMealDataBase = AppDataBase.getInstance(this.getContext());
        favoriteMealDAO=favoriteMealDataBase.getFavoriteMealDAO();
        favoriteMealsRecyclerView=binding.mealsList;
        favoriteMealsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        favoriteMealsRecyclerView.setAdapter(mealAdapter);
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        LiveData<List<Meal>> mealList = favoriteMealDAO.getFavoriteMealsForUser();
        Observer<List<Meal>>observer=new Observer<List<Meal>>() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onChanged(List<Meal> meals)
            {
                for (Meal meal : meals) {
                    meal.setFavorite(true);
                }
                mealAdapter.setMealList(meals);
                mealAdapter.notifyDataSetChanged();
            }
        };
        mealList.observe(getViewLifecycleOwner(),observer);
        return root;
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        AppDataBase.getInstance(getContext()).forceCheckpoint();
        binding = null;
    }

}