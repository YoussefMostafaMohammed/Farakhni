package com.example.farakhni.freatures.mealdetails;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.bumptech.glide.Glide;
import com.example.farakhni.R;
import com.example.farakhni.data.repositories.MealRepositoryImpl;
import com.example.farakhni.databinding.FragmentMealDetailsBinding;
import com.example.farakhni.model.FavoriteMeal;
import com.example.farakhni.model.Ingredient;
import com.example.farakhni.utils.IngredientDetailsAdapter;
import com.google.android.material.datepicker.CalendarConstraints;
import com.google.android.material.datepicker.CompositeDateValidator;
import com.google.android.material.datepicker.DateValidatorPointForward;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.firebase.auth.FirebaseAuth;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MealDetailsFragment extends Fragment implements MealDetailsContract.View {
    private static final String ARG_MEAL = "arg_meal";
    private FragmentMealDetailsBinding binding;
    private MealDetailsContract.Presenter presenter;
    private IngredientDetailsAdapter ingredientAdapter;

    public static MealDetailsFragment newInstance(FavoriteMeal meal) {
        MealDetailsFragment fragment = new MealDetailsFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_MEAL, meal);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentMealDetailsBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        FirebaseAuth auth = FirebaseAuth.getInstance();
        String userId = auth.getCurrentUser() != null ? auth.getCurrentUser().getUid() : null;
        presenter = new MealDetailsPresenter(
                new MealDetailsModel(MealRepositoryImpl.getInstance(requireContext())),
                userId
        );
        presenter.attachView(this);

        FavoriteMeal meal = getMealFromArguments();
        presenter.initialize(meal, getViewLifecycleOwner());
    }

    private FavoriteMeal getMealFromArguments() {
        if (getArguments() == null) return null;
        return (FavoriteMeal) getArguments().getSerializable(ARG_MEAL);
    }

    @Override
    public void showMealDetails(String name, String instructions, String imageUrl) {
        binding.mealName.setText(name);
        binding.instructions.setText(instructions);
        Glide.with(this)
                .load(imageUrl)
                .placeholder(R.drawable.app_logo)
                .error(R.drawable.app_logo)
                .into(binding.mealImage);
    }

    @Override
    public void showIngredients(List<Ingredient> ingredients) {
        ingredientAdapter = new IngredientDetailsAdapter(requireContext(), ingredients);
        binding.ingredientsList.setLayoutManager(
                new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        );
        binding.ingredientsList.setAdapter(ingredientAdapter);
    }

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    public void showVideo(String videoUrl) {
        if (!isInternetAvailable()) {
            hideVideo();
            showMessage("No internet connection.");
            return;
        }

        binding.videoWebView.getSettings().setJavaScriptEnabled(true);
        binding.videoWebView.getSettings().setDomStorageEnabled(true);
        binding.videoWebView.setOverScrollMode(View.OVER_SCROLL_NEVER);

        binding.videoWebView.setWebViewClient(new android.webkit.WebViewClient() {
            @Override
            public void onReceivedError(android.webkit.WebView view, android.webkit.WebResourceRequest request, android.webkit.WebResourceError error) {
                super.onReceivedError(view, request, error);
                hideVideo();
                showMessage("Failed to load video.");
            }
        });

        String html = "<html><body style='margin:0;padding:0;'>" +
                "<iframe width='100%' height='100%' src='" + videoUrl + "' " +
                "frameborder='0' allowfullscreen></iframe></body></html>";

        binding.videoWebView.loadDataWithBaseURL(null, html, "text/html", "UTF-8", null);
    }

    @Override
    public void hideVideo() {
        ViewGroup videoCard = (ViewGroup) binding.videoContainer.getParent();
        if (videoCard != null && videoCard.getParent() != null) {
            ((ViewGroup) videoCard.getParent()).removeView(videoCard);
        }
    }

    @Override
    public void setFavoriteStatus(boolean isFavorite) {
        binding.heartIcon.setSelected(isFavorite);
        binding.heartIcon.setOnClickListener(v -> presenter.onFavoriteClicked());
    }

    @Override
    public void setScheduleStatus(boolean isScheduled) {
        binding.calendarIconAdd.setSelected(isScheduled);
        binding.calendarIconDelete.setVisibility(isScheduled ? View.VISIBLE : View.GONE);
        binding.calendarIconAdd.setOnClickListener(v -> presenter.onPlanMealClicked());
        binding.calendarIconDelete.setOnClickListener(v -> presenter.onDeleteMealClicked());
    }

    @Override
    public void showDatePicker() {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        long today = cal.getTimeInMillis();
        cal.add(Calendar.DAY_OF_MONTH, 7);
        long nextWeek = cal.getTimeInMillis();

        CalendarConstraints constraints = new CalendarConstraints.Builder()
                .setStart(today)
                .setEnd(nextWeek)
                .setValidator(CompositeDateValidator.allOf(Arrays.asList(
                        DateValidatorPointForward.from(today)
                )))
                .build();

        MaterialDatePicker<Long> picker = MaterialDatePicker.Builder.datePicker()
                .setTitleText("Choose a date (next 7 days)")
                .setTheme(R.style.ThemeOverlay_Farakhni_DatePicker)
                .setSelection(today)
                .setCalendarConstraints(constraints)
                .build();

        picker.show(getParentFragmentManager(), "MEAL_DATE_PICKER");

        picker.addOnPositiveButtonClickListener(selection -> {
            String selDate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date(selection));
            presenter.onDateSelected(selDate);
            binding.calendarIconAdd.startAnimation(
                    AnimationUtils.loadAnimation(requireContext(), R.anim.calendar_bounce)
            );
        });

        picker.addOnDismissListener(dialog -> binding.calendarIconAdd.setSelected(false));
    }

    @Override
    public void showMessage(String message) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showError(String message) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void navigateBack() {
        requireActivity().onBackPressed();
    }

    private boolean isInternetAvailable() {
        ConnectivityManager cm = (ConnectivityManager) requireContext().getSystemService(requireContext().CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null && activeNetwork.isConnected();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        presenter.detachView();
        binding = null;
    }
}