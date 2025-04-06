package com.mobdeve.sustainabite;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Base64;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import android.text.TextUtils;

public class AddRecipeActivity extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 1;
    private Bitmap selectedBitmap;

    private EditText recipeNameInput, procedureInput, caloriesInput;
    private Button addRecipeButton, addIngredientButton, attachImageButton;
    private LinearLayout ingredientLayout;
    private List<EditText> ingredientInputs = new ArrayList<>();
    private DBManager dbManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_recipe);

        dbManager = new DBManager();

        recipeNameInput = findViewById(R.id.recipeNameInput);
        procedureInput = findViewById(R.id.procedureInput);
        attachImageButton = findViewById(R.id.attachImageButton);
        addRecipeButton = findViewById(R.id.addRecipeButton);
        addIngredientButton = findViewById(R.id.addIngredientButton);
        ingredientLayout = findViewById(R.id.ingredientLayout);
        caloriesInput = findViewById(R.id.caloriesInput);

        addIngredientButton.setOnClickListener(v -> addIngredientField());
        attachImageButton.setOnClickListener(v -> openGallery());
        addRecipeButton.setOnClickListener(v -> addRecipe());

        addIngredientField();
    }

    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri imageUri = data.getData();
            try {
                selectedBitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri);
                attachImageButton.setText("Image Selected");
            } catch (IOException e) {
                Toast.makeText(this, "Error loading image", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
        }
    }

    private void addIngredientField() {
        EditText newIngredientInput = new EditText(this);
        newIngredientInput.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        ));
        newIngredientInput.setHint("Enter an ingredient");
        newIngredientInput.setPadding(10, 10, 10, 10);
        newIngredientInput.setBackgroundResource(android.R.drawable.editbox_background);
        newIngredientInput.setTextSize(16);
        newIngredientInput.setGravity(Gravity.START | Gravity.CENTER_VERTICAL);
        newIngredientInput.setInputType(InputType.TYPE_CLASS_TEXT);

        ingredientLayout.addView(newIngredientInput);
        ingredientInputs.add(newIngredientInput);
    }

    private void addRecipe() {
        String rName = recipeNameInput.getText().toString().trim();
        String rProcedure = procedureInput.getText().toString().trim();
        String rCalories = caloriesInput.getText().toString().trim();

        if (rName.isEmpty() || rProcedure.isEmpty() || ingredientInputs.isEmpty() || selectedBitmap == null || rCalories.isEmpty()) {
            Toast.makeText(this, "Please fill in all required fields!", Toast.LENGTH_SHORT).show();
            return;
        }

        // Retrieve user ID
        SharedPreferences prefs = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        String uNum = prefs.getString("USER_ID", "U000");

        // Collect ingredients as a list
        List<String> ingredientsList = new ArrayList<>();
        for (EditText ingredientInput : ingredientInputs) {
            String ingredient = ingredientInput.getText().toString().trim();
            if (!ingredient.isEmpty()) {
                ingredientsList.add(ingredient);
            }
        }
        String rIngredients = TextUtils.join(", ", ingredientsList);

        String rImage = bitmapToBase64(selectedBitmap);

        dbManager.addRecipeToFirestore(this, rImage, rIngredients, rName, rProcedure, rCalories, new DBManager.OnRecipeAddedListener() {
            @Override
            public void onSuccess() {
                Toast.makeText(AddRecipeActivity.this, "Recipe Added Successfully!", Toast.LENGTH_SHORT).show();
                setResult(RESULT_OK, new Intent().putExtra("recipe_added", true));
                finish();
            }

            @Override
            public void onFailure(Exception e) {
                Toast.makeText(AddRecipeActivity.this, "Failed to add recipe: " + e.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }


    // Convert Bitmap to Base64 String
    private String bitmapToBase64(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(byteArray, Base64.DEFAULT);
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
