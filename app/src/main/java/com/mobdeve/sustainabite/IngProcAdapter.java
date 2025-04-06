package com.mobdeve.sustainabite;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.StyleSpan;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class IngProcAdapter extends RecyclerView.Adapter<IngProcAdapter.FoodViewHolder> {
    private List<FoodItem> foodList;
    private Context context;
    private final ActivityResultLauncher<Intent> recipeLauncher;

    public IngProcAdapter(Context context, List<FoodItem> foodList, ActivityResultLauncher<Intent> recipeLauncher) {
        this.context = context;
        this.foodList = foodList;
        this.recipeLauncher = recipeLauncher;
    }

    public static class FoodViewHolder extends RecyclerView.ViewHolder {
        ImageView foodImage;
        TextView foodName, ingContent, procContent, recipeOwner;
        Button detailsButton;

        public FoodViewHolder(View view) {
            super(view);
                foodImage = view.findViewById(R.id.foodImage);
                foodName = view.findViewById(R.id.foodName);
                ingContent = view.findViewById(R.id.ing_content);
                procContent = view.findViewById(R.id.procedures_content);
                detailsButton = view.findViewById(R.id.detailsButton);
                recipeOwner = view.findViewById(R.id.recipeOwner);

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
            Bitmap decodedBitmap = DBManager.decodeBase64ToBitmap(food.getImageString());
            if (decodedBitmap != null) {
                holder.foodImage.setImageBitmap(decodedBitmap);
            } else {
                holder.foodImage.setImageResource(R.drawable.banana);
            }
        } else if (food.getImageResId() != null) {
            holder.foodImage.setImageResource(food.getImageResId());
        } else {
            holder.foodImage.setImageResource(R.drawable.banana);
        }

        holder.foodName.setText(food.getName());
        holder.ingContent.setText(food.getIngredients());
        holder.procContent.setText(food.getProcedures());

        String ownerId = food.getUNum();
        fetchOwnerName(ownerId, holder.recipeOwner);

        View.OnClickListener listener = v -> {
            Intent intent = new Intent(context, RecipeDetailsActivity.class);
            intent.putExtra("foodItem", food);
            recipeLauncher.launch(intent);
        };

        holder.itemView.setOnClickListener(listener);
        holder.detailsButton.setOnClickListener(listener);
    }

    private void fetchOwnerName(String userId, TextView recipeOwnerTextView) {
        DBManager.getUserById(userId, new DBManager.UserCallback() {
            @Override
            public void onUserRetrieved(String ownerName) {
                String prefix = "recipe by: @";
                String fullText = prefix + ownerName;

                SpannableString spannableString = new SpannableString(fullText);

                spannableString.setSpan(new StyleSpan(Typeface.BOLD), prefix.length(), fullText.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                spannableString.setSpan(new UnderlineSpan(), prefix.length(), fullText.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

                recipeOwnerTextView.setText(spannableString);
            }

            @Override
            public void onError(Exception e) {
                recipeOwnerTextView.setText("Unknown Owner");
                Log.e("IngProcAdapter", "Error fetching owner name", e);
            }
        });
    }

    @Override
    public int getItemCount() {
        return foodList.size();
    }
}
