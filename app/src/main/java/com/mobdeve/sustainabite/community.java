package com.mobdeve.sustainabite;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class community extends AppCompatActivity {

    private RecyclerView recyclerView;
    private IngProcAdapter ingProcAdapter;
    private List<FoodItem> foodList;
    private DBManager dbManager;

    // Register ActivityResultLauncher
    private final ActivityResultLauncher<Intent> addRecipeLauncher =
            registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
                if (result.getResultCode() == RESULT_OK) {
                    if (result.getData() != null && result.getData().getBooleanExtra("recipe_added", false)) {
                        fetchRecipesFromFirestore(); // Refresh the list
                    }
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_community);

        dbManager = new DBManager();
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));


        foodList = new ArrayList<>();
        // foodList.add(new FoodItem(R.drawable.spinach_omelette, "Spinach Omelette", "Kcal 250", getString(R.string.ingredients1), getString(R.string.procedures1)));
        // foodList.add(new FoodItem(R.drawable.fried_rice, "Fried Rice", "Kcal 180", getString(R.string.ingredients2), getString(R.string.procedures2)));


        ingProcAdapter = new IngProcAdapter(this, foodList);
        recyclerView.setAdapter(ingProcAdapter);

        fetchRecipesFromFirestore(); // Fetch data from Firestore

        Button addRecipeButton = findViewById(R.id.addRecipeButton);
        addRecipeButton.setOnClickListener(v -> {
            Intent intent = new Intent(community.this, AddRecipeActivity.class);
            addRecipeLauncher.launch(intent); // Start activity with launcher
        });
    }

    private void fetchRecipesFromFirestore() {
        dbManager.fetchRecipes(new DBManager.OnRecipesFetchedListener() {
            @Override
            public void onRecipesFetched(List<FoodItem> recipes) {
                foodList.clear();
                foodList.addAll(recipes);
                ingProcAdapter.notifyDataSetChanged();
            }

            @Override
            public void onError(Exception e) {
                Log.e("Firestore", "Error fetching recipes", e);
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
