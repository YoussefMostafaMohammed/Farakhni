package com.example.farakhni.ui.home;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.farakhni.R;
import com.example.farakhni.model.Category;

import java.util.ArrayList;
import java.util.List;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder> {
    private Context context;
    private List<Category> categoryList;
    public CategoryAdapter(Context context, List<Category> categoryList) {
        this.context = context;
        this.categoryList = categoryList != null ? categoryList : new ArrayList<>();
    }

    @NonNull
    @Override
    public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.category_item, parent, false);
        return new CategoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryViewHolder holder, int position) {
        Category category = categoryList.get(position);

        holder.categoryName.setText(category.getCategory() != null ? category.getCategory() : "Unknown Category");

        String imageUrl = category.getCategoryThumb();
        Glide.with(context)
                .load(imageUrl)
                .into(holder.categoryImage);

        holder.itemView.setOnTouchListener(new View.OnTouchListener() {
            PopupWindow popup;

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getActionMasked()) {
                    case MotionEvent.ACTION_DOWN:
                        View popupView = LayoutInflater.from(context)
                                .inflate(R.layout.popup_layout, null, false);
                        TextView desc = popupView.findViewById(R.id.popupIngredientDescription);
                        desc.setText(category.getCategoryDescription() != null ? category.getCategoryDescription() : "No description available.");

                        popupView.measure(
                                View.MeasureSpec.UNSPECIFIED,
                                View.MeasureSpec.UNSPECIFIED
                        );

                        popup = new PopupWindow(
                                popupView,
                                popupView.getMeasuredWidth(),
                                popupView.getMeasuredHeight(),
                                true
                        );
                        popup.setOutsideTouchable(false);
                        popup.setClippingEnabled(true);
                        popup.showAsDropDown(v, 0, -v.getHeight() - popupView.getMeasuredHeight());
                        return true;

                    case MotionEvent.ACTION_UP:
                    case MotionEvent.ACTION_CANCEL:
                        if (popup != null && popup.isShowing()) {
                            popup.dismiss();
                        }
                        return true;
                }
                return false;
            }
        });
    }

    @Override
    public int getItemCount() {
        return categoryList.size();
    }
    public void updateCategories(List<Category> newCategories) {
        categoryList.clear();
        categoryList.addAll(newCategories);
        notifyDataSetChanged();
    }

    public static class CategoryViewHolder extends RecyclerView.ViewHolder {
        ImageView categoryImage;
        TextView categoryName;
        public CategoryViewHolder(@NonNull View itemView) {
            super(itemView);
            categoryImage = itemView.findViewById(R.id.categoryImage);
            categoryName = itemView.findViewById(R.id.categoryName);
        }
    }
}