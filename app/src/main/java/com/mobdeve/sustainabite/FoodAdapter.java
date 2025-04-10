package com.mobdeve.sustainabite;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class FoodAdapter extends RecyclerView.Adapter<FoodAdapter.FoodViewHolder> {
    private List<FoodHome> foodList;
    private Context context;

    public FoodAdapter(Context context, List<FoodHome> foodList) {
        this.context = context;
        this.foodList = foodList;
    }

    @NonNull
    @Override
    public FoodViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_food, parent, false);
        return new FoodViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FoodViewHolder holder, int position) {
        FoodHome food = foodList.get(position);

        if (food.getImage() != null) {
            holder.foodImage.setImageBitmap(food.getImage());
        } else {
            holder.foodImage.setImageResource(R.drawable.banana);
        }

        holder.foodName.setText(food.getName());
        holder.foodKcal.setText(food.getKcal() + " KCal");
        holder.detailsButton.setOnClickListener(v -> {
            // Implement details button action
        });


        holder.detailsButton.setOnClickListener(v -> {
            String chosenRecipe = food.getName();
            Intent intent = new Intent(context, RecipeDetailsActivity2.class);
            intent.putExtra("RECIPE_ID", chosenRecipe);
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return foodList.size();
    }

    public static class FoodViewHolder extends RecyclerView.ViewHolder {
        ImageView foodImage;
        TextView foodName, foodKcal;
        Button detailsButton;

        public FoodViewHolder(View view) {
            super(view);
            foodImage = view.findViewById(R.id.foodImage);
            foodName = view.findViewById(R.id.foodName);
            foodKcal = view.findViewById(R.id.foodKcal);
            detailsButton = view.findViewById(R.id.detailsButton);
        }
    }
}

