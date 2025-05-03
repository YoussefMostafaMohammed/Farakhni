package com.example.farakhni.freatures.mealdetails;

import android.annotation.SuppressLint;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.bumptech.glide.Glide;
import com.example.farakhni.R;
import com.example.farakhni.common.IngredientDetailsAdapter;
import com.example.farakhni.databinding.FragmentMealDetailsBinding;
import com.example.farakhni.model.Ingredient;
import com.example.farakhni.model.Meal;

import java.util.List;

public class MealDetailsFragment extends Fragment {
    private static final String ARG_MEAL = "arg_meal";
    private FragmentMealDetailsBinding binding;
    private IngredientDetailsAdapter ingredientAdapter;

    public static MealDetailsFragment newInstance(Meal meal) {
        MealDetailsFragment f = new MealDetailsFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_MEAL, meal);
        f.setArguments(args);
        return f;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentMealDetailsBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Meal meal = getMealFromArguments();
        if (meal == null) return;

        setupMealDetails(meal);
        setupWebView(meal);
        setupIngredientsList(meal);
    }

    private Meal getMealFromArguments() {
        if (getArguments() == null) return null;
        Meal meal = (Meal) getArguments().getSerializable(ARG_MEAL);
        if (meal == null) {
            Toast.makeText(requireContext(), "Failed to load meal details.", Toast.LENGTH_SHORT).show();
            requireActivity().onBackPressed();
        }
        return meal;
    }

    private void setupMealDetails(Meal meal) {
        binding.mealName.setText(meal.getName() != null ? meal.getName() : "Unknown Meal");
        binding.instructions.setText(meal.getInstructions() != null ? meal.getInstructions() : "No instructions available");

        // Set default values for nutrition info
        binding.calories.setText("N/A Calories");
        binding.protein.setText("N/A Protein");
        binding.carbs.setText("N/A Carbs");

        Glide.with(this)
                .load(meal.getMealThumb())
                .placeholder(R.drawable.app_logo)
                .error(R.drawable.app_logo)
                .into(binding.mealImage);
    }

    private void setupWebView(Meal meal) {
        if (!isInternetAvailable()) {
            removeWebView();
            Toast.makeText(requireContext(), "No internet connection.", Toast.LENGTH_SHORT).show();
            return;
        }

        initializeWebView(meal);
    }

    private void initializeWebView(Meal meal) {
        WebView webView = binding.videoWebView;
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setDomStorageEnabled(true);
        webView.setOverScrollMode(WebView.OVER_SCROLL_NEVER);

        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, android.webkit.WebResourceError error) {
                super.onReceivedError(view, request, error);
                removeWebView();
                Toast.makeText(requireContext(), "Failed to load video.", Toast.LENGTH_SHORT).show();
            }
        });

        String videoUrl = meal.getYoutube();
        if (videoUrl != null && videoUrl.contains("watch?v=")) {
            videoUrl = videoUrl.replace("watch?v=", "embed/");
        }

        if (videoUrl != null && !videoUrl.isEmpty()) {
            String html = "<html><body style='margin:0;padding:0;'>" +
                    "<iframe width='100%' height='100%' src='" + videoUrl + "' " +
                    "frameborder='0' allowfullscreen></iframe></body></html>";

            webView.loadDataWithBaseURL(null, html, "text/html", "UTF-8", null);
        } else {
            removeWebView();
        }
    }

    private void removeWebView() {
        // Get the video container's parent (MaterialCardView)
        ViewGroup videoCard = (ViewGroup) binding.videoContainer.getParent();
        if (videoCard != null && videoCard.getParent() != null) {
            // Remove the entire video card from its parent (main LinearLayout)
            ((ViewGroup) videoCard.getParent()).removeView(videoCard);
        }
    }

    private void setupIngredientsList(Meal meal) {
        List<Ingredient> ingredients = meal.getIngredientsList();
        ingredientAdapter = new IngredientDetailsAdapter(requireContext(), ingredients);
        binding.ingredientsList.setLayoutManager(
                new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        );
        binding.ingredientsList.setAdapter(ingredientAdapter);
    }

    private boolean isInternetAvailable() {
        ConnectivityManager cm = (ConnectivityManager) requireContext().getSystemService(requireContext().CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null && activeNetwork.isConnected();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}