package com.mobdeve.sustainabite;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class community extends AppCompatActivity {

    private RecyclerView recyclerView, searchResultsRV;
    private IngProcAdapter ingProcAdapter;
    private List<FoodItem> foodList;
    private List<SearchResult> searchResults;
    private EditText searchBar;
    private ImageView searchButton;
    private SearchResultsAdapter searchResultsAdapter;
    private DBManager dbManager;

    private final ActivityResultLauncher<Intent> recipeLauncher =
            registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
                if (result.getResultCode() == RESULT_OK) {
                    Intent data = result.getData();
                    if (data != null) {
                        if (data.getBooleanExtra("recipe_added", false) || data.getBooleanExtra("recipe_deleted", false)) {
                            fetchRecipesFromFirestore();
                        }
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


        ingProcAdapter = new IngProcAdapter(this, foodList, recipeLauncher);
        recyclerView.setAdapter(ingProcAdapter);

        fetchRecipesFromFirestore();

        Button addRecipeButton = findViewById(R.id.addRecipeButton);
        addRecipeButton.setOnClickListener(v -> {
            Intent intent = new Intent(community.this, AddRecipeActivity.class);
            recipeLauncher.launch(intent);
        });

        searchResultsRV = findViewById(R.id.searchResultsRV);
        searchResultsRV.setLayoutManager(new LinearLayoutManager(this));

        searchBar = findViewById(R.id.search_bar);
        searchButton = findViewById(R.id.search_button);

        searchResults = new ArrayList<>();
        searchResultsAdapter = new SearchResultsAdapter(this, searchResults, searchResult -> {
            dbManager.getUserId(searchResult, new DBManager.FirestoreCallback() {
                @Override
                public void onUserIDRetrieved(String resultType) {
                    Intent intent;
                    if (resultType.equals("Food Exists")) {
                        intent = new Intent(community.this, ProductDetailsActivity2.class);
                        intent.putExtra("FOOD_NAME", searchResult);
                    } else {
                        intent = new Intent(community.this, RecipeDetailsActivity2.class);
                        intent.putExtra("RECIPE_ID", searchResult);
                    }
                    startActivity(intent);
                }
            });
        });

        searchResultsRV.setAdapter(searchResultsAdapter);

        searchButton.setOnClickListener(v -> performSearch());


    }

    private void fetchRecipesFromFirestore() {
        dbManager.fetchRecipes(this, new DBManager.OnRecipesFetchedListener() {
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

    private void performSearch() {
        String query = searchBar.getText().toString().trim();

        if (query.isEmpty()) {
            Toast.makeText(this, "Enter a search term", Toast.LENGTH_SHORT).show();
            return;
        }

        searchResults.clear();

        dbManager.finder(query, new DBManager.RecipeCallback() {
            @Override
            public void onRecipeRetrieved(List<String> foundSearch) {
                if (foundSearch.isEmpty()) {
                    foundSearch.add("N/A - No Info");
                }

                for (String result : foundSearch) {
                    String[] parts = result.split(" - ", 2);
                    String name = parts[0];
                    String secondaryInfo = (parts.length > 1) ? parts[1] : "No Info";
                    searchResults.add(new SearchResult(name, secondaryInfo));
                }

                searchResultsAdapter.notifyDataSetChanged();
                searchResultsRV.setVisibility(View.VISIBLE);
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
