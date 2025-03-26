package com.mobdeve.sustainabite;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class IngProcAdapter extends RecyclerView.Adapter<IngProcAdapter.FoodViewHolder> {
    private List<FoodItem> foodList;
    private Context context;

    public IngProcAdapter(Context context, List<FoodItem> foodList) {
        this.context = context;
        this.foodList = foodList;
    }

    public static class FoodViewHolder extends RecyclerView.ViewHolder {
        ImageView foodImage;
        TextView foodName, ingContent, procContent;
        Button detailsButton;

        public FoodViewHolder(View view) {
            super(view);
                foodImage = view.findViewById(R.id.foodImage);
                foodName = view.findViewById(R.id.foodName);
                ingContent = view.findViewById(R.id.ing_content);
                procContent = view.findViewById(R.id.procedures_content);
                detailsButton = view.findViewById(R.id.detailsButton);


        }
    }

    @NonNull
    @Override
    public FoodViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_ingredient_procedure, parent, false);
        return new FoodViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FoodViewHolder holder, int position) {
        FoodItem food = foodList.get(position);

        Log.d("Adapter", "Loading image for: " + food.getName());

        if (food.getImageString() != null && !food.getImageString().isEmpty()) {
            // Load Base64 image
            Bitmap decodedBitmap = DBManager.decodeBase64ToBitmap(food.getImageString());
            if (decodedBitmap != null) {
                holder.foodImage.setImageBitmap(decodedBitmap);
            } else {
                holder.foodImage.setImageResource(R.drawable.banana);
            }
        } else if (food.getImageResId() != null) {
            // Load drawable image
            holder.foodImage.setImageResource(food.getImageResId());
        } else {
            holder.foodImage.setImageResource(R.drawable.banana);
        }

        holder.foodName.setText(food.getName());
        holder.ingContent.setText(food.getIngredients());
        holder.procContent.setText(food.getProcedures());
    }



    @Override
    public int getItemCount() {
        return foodList.size();
    }
}
