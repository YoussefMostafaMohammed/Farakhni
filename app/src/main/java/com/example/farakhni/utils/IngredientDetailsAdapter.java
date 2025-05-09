package com.example.farakhni.utils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.example.farakhni.R;
import com.example.farakhni.model.Ingredient;
import java.util.List;

public class IngredientDetailsAdapter extends RecyclerView.Adapter<IngredientDetailsAdapter.IngredientDetailsViewHolder> {
    private Context context;
    private List<Ingredient> ingredientList;
    public IngredientDetailsAdapter(Context context, List<Ingredient> ingredientList) {
        this.context = context;
        this.ingredientList = ingredientList;
    }

    @NonNull
    @Override
    public IngredientDetailsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.ingredient_details_item, parent, false);
        return new IngredientDetailsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull IngredientDetailsViewHolder holder, int position) {
        Ingredient ingredient = ingredientList.get(position);
        String name = (ingredient.getIngredient() != null ? ingredient.getIngredient() : "Unknown");
        String measure = (ingredient.getIngredient() != null ? ingredient.getMeasure() : "Unknown");
        holder.ingredientName.setText(name);
        holder.ingredientMeasure.setText(measure);
        String imageUrl = "https://www.themealdb.com/images/ingredients/" + ingredient.getIngredient() + "-Small.png";
        Glide.with(context)
                .load(imageUrl)
                .into(holder.ingredientImage);
        holder.itemCard.setOnLongClickListener(v -> {
            View popupView = LayoutInflater.from(context)
                    .inflate(R.layout.popup_layout, null, false);
            TextView desc = popupView.findViewById(R.id.popupIngredientDescription);
            desc.setText(
                    ingredient.getDescription() != null
                            ? ingredient.getDescription()
                            : "No description available."
            );

            // measure & show
            popupView.measure(
                    View.MeasureSpec.UNSPECIFIED,
                    View.MeasureSpec.UNSPECIFIED
            );
            PopupWindow popup = new PopupWindow(
                    popupView,
                    popupView.getMeasuredWidth(),
                    popupView.getMeasuredHeight(),
                    true
            );
            popup.setOutsideTouchable(true);
            popup.showAsDropDown(v,
                    /* xOffset */ 0,
                    /* yOffset */ -v.getHeight() - popupView.getMeasuredHeight()
            );
            return true;  // consume the long-press
        });
    }

    @Override
    public int getItemCount() {
        return ingredientList.size();
    }

    public static class IngredientDetailsViewHolder extends RecyclerView.ViewHolder {
        ImageView ingredientImage;
        TextView ingredientName;
        TextView ingredientMeasure;
        CardView itemCard;
        public IngredientDetailsViewHolder(@NonNull View itemView) {
            super(itemView);
            ingredientImage = itemView.findViewById(R.id.ingredientImage);
            ingredientName = itemView.findViewById(R.id.ingredientName);
            ingredientMeasure = itemView.findViewById(R.id.ingredientMeasure);
            itemCard=itemView.findViewById(R.id.itemCard);
        }
    }
}