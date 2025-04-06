package com.mobdeve.sustainabite;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class RecipeDetailsActivity extends AppCompatActivity {

    private TextView foodName, ingContent, procContent, foodKcal, recipeDetailsOwner;
    private ImageView foodImage;
    private Button editButton;
    private Button deleteButton;


    private FoodItem foodItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_details);

        foodImage = findViewById(R.id.foodImage);
        foodName = findViewById(R.id.foodName);
        ingContent = findViewById(R.id.ing_content);
        procContent = findViewById(R.id.procedures_content);
        foodKcal = findViewById(R.id.foodKcal);
        editButton = findViewById(R.id.editButton);
        deleteButton = findViewById(R.id.deleteButton);
        recipeDetailsOwner = findViewById(R.id.recipeDetailsOwner);

        foodItem = (FoodItem) getIntent().getSerializableExtra("foodItem");

        if (foodItem != null) {
            foodName.setText(foodItem.getName());
            ingContent.setText(foodItem.getIngredients());
            procContent.setText(foodItem.getProcedures());

            if (foodItem.getKcal() != null && !foodItem.getKcal().isEmpty()) {
                foodKcal.setText(foodItem.getKcal() + " kcal");
            } else {
                foodKcal.setText("No kcal data available");
            }

            Bitmap bitmap = DBManager.decodeBase64ToBitmap(foodItem.getImageString());
            if (bitmap != null) {
                foodImage.setImageBitmap(bitmap);
            } else {
                foodImage.setImageResource(R.drawable.noimage);
            }

            if (foodItem.getUNum() != null && !foodItem.getUNum().isEmpty()) {
                fetchOwnerName(foodItem.getUNum());
            } else {
                recipeDetailsOwner.setText("Unknown Owner");
            }

        }

        DBManager dbManager = new DBManager();
        dbManager.getCurrentUserDetails(this, new DBManager.OnUserDetailsFetchedListener() {
            @Override
            public void onUserDetailsFetched(String userId, String email, String name, String image) {
                if (!userId.equals(foodItem.getUNum())) {
                    editButton.setVisibility(View.GONE);
                    deleteButton.setVisibility(View.GONE);
                }
            }

            @Override
            public void onError(String error) {
                Toast.makeText(RecipeDetailsActivity.this, "Failed to get current user details", Toast.LENGTH_SHORT).show();
            }
        });

        editButton.setOnClickListener(v -> showEditDialog());
        deleteButton.setOnClickListener(v -> confirmDeleteRecipe());
    }

    private void fetchOwnerName(String ownerId) {
        DBManager.getUserById(ownerId, new DBManager.UserCallback() {
            @Override
            public void onUserRetrieved(String ownerName) {
                recipeDetailsOwner.setText("@" + ownerName);
            }

            @Override
            public void onError(Exception e) {
                recipeDetailsOwner.setText("Unknown Owner");
                Toast.makeText(RecipeDetailsActivity.this, "Failed to load owner name", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showEditDialog() {
        LayoutInflater inflater = LayoutInflater.from(this);
        View dialogView = inflater.inflate(R.layout.edit_recipe, null);

        EditText editFoodName = dialogView.findViewById(R.id.edit_foodName);
        EditText editIngContent = dialogView.findViewById(R.id.edit_ing_content);
        EditText editProcContent = dialogView.findViewById(R.id.edit_procedures_content);
        EditText editFoodKcal = dialogView.findViewById(R.id.edit_foodKcal);

        editFoodName.setText(foodItem.getName());
        editIngContent.setText(foodItem.getIngredients());
        editProcContent.setText(foodItem.getProcedures());
        editFoodKcal.setText(foodItem.getKcal());

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Edit Recipe")
                .setView(dialogView)
                .setPositiveButton("Save", null)
                .setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss());

        AlertDialog dialog = builder.create();
        dialog.getWindow().setBackgroundDrawableResource(R.drawable.yellow_rounded_box);

        dialog.setOnShowListener(dialogInterface -> {
            Button saveButton = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
            saveButton.setOnClickListener(v -> {
                String updatedName = editFoodName.getText().toString().trim();
                String updatedKcal = editFoodKcal.getText().toString().trim();
                String updatedIngredients = editIngContent.getText().toString().trim();
                String updatedProcedures = editProcContent.getText().toString().trim();

                if (updatedName.isEmpty() || updatedKcal.isEmpty() || updatedIngredients.isEmpty() || updatedProcedures.isEmpty()) {
                    Toast.makeText(RecipeDetailsActivity.this, "Please fill in all fields before saving.", Toast.LENGTH_SHORT).show();
                    return;
                }

                foodName.setText(updatedName);
                ingContent.setText(updatedIngredients);
                procContent.setText(updatedProcedures);
                foodKcal.setText(updatedKcal);

                DBManager dbManager = new DBManager();
                dbManager.updateRecipeInFirestore(foodItem.getId(), updatedName, updatedKcal, updatedIngredients, updatedProcedures, new DBManager.OnRecipeListener() {
                    @Override
                    public void onSuccess() {
                        Toast.makeText(RecipeDetailsActivity.this, "Recipe updated successfully!", Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    }

                    @Override
                    public void onFailure(Exception e) {
                        Toast.makeText(RecipeDetailsActivity.this, "Failed to update recipe: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            });
        });

        dialog.show();
    }

    private void confirmDeleteRecipe() {
        new AlertDialog.Builder(this)
                .setTitle("Delete Recipe")
                .setMessage("Are you sure you want to delete this recipe?")
                .setPositiveButton("Delete", (dialog, which) -> deleteRecipe())
                .setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss())
                .show();
    }

    private void deleteRecipe() {
        if (foodItem == null || foodItem.getId() == null || foodItem.getId().isEmpty()) {
            Toast.makeText(this, "Error: Recipe ID not found", Toast.LENGTH_SHORT).show();
            return;
        }

        DBManager dbManager = new DBManager();
        dbManager.deleteRecipeFromFirestore(foodItem.getId(), new DBManager.OnRecipeListener() {
            @Override
            public void onSuccess() {
                Toast.makeText(RecipeDetailsActivity.this, "Recipe deleted successfully!", Toast.LENGTH_SHORT).show();

                Intent resultIntent = new Intent();
                resultIntent.putExtra("recipe_deleted", true);
                setResult(RESULT_OK, resultIntent);

                finish();
            }

            @Override
            public void onFailure(Exception e) {
                Toast.makeText(RecipeDetailsActivity.this, "Failed to delete recipe: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });


    }

    /* NAVIGATIONS */
    public void goHome(View view) {
        startActivity(new Intent(this, home.class));
    }

    public void goFood(View view) {
        startActivity(new Intent(this, foodManagement.class));
    }

    public void goCommunity(View view) {
        startActivity(new Intent(this, community.class));
    }

    public void goProfile(View view) {
        startActivity(new Intent(this, profile.class));
    }

}
