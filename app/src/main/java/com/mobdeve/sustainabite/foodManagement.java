package com.mobdeve.sustainabite;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.DocumentSnapshot;

import java.util.ArrayList;
import java.util.List;


public class foodManagement extends AppCompatActivity {

    private DBManager dbManager;
    private List<Product> productList, fullProductList, filteredProducts;
    private ProductAdapter productAdapter;
    private boolean isFiltered = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_foodmanagement);

        dbManager = new DBManager();

        //Initialize the array for products.
        productList = new ArrayList<>();
        fullProductList = new ArrayList<>();

        productAdapter = new ProductAdapter(productList);

        // Initialize RecyclerView
        RecyclerView recyclerView = findViewById(R.id.recyclerViewProducts);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(productAdapter);

        // fetchProductsFromFirestore(); -> I removed this because it is a duplicate entry from onResume();
    }

    //Fetch the set of products that are in Firestore.

    private void fetchProductsFromFirestore(){
        //Clear Data first in order to ensure that it does not show the old data.
        productList.clear();
        fullProductList.clear();  // Add this line to clear the backup list
        productAdapter.notifyDataSetChanged();
        dbManager.fetchProduct(new DBManager.OnProductsFetchedListener() {
            @Override

            public void onProductsFetched(List<Product> products) {
                Log.d("Firestore", "Fetched " + products.size() + " products.");
                for (Product product : products) {
                    Log.d("Firestore", "Product: " + product.getName());
                }

                // Always add to fullProductList
                fullProductList.addAll(products);

                // If no filter is applied, update productList with all products
                if (!isFiltered) {
                    productList.addAll(products);
                }

                // Check if fullProductList is populated correctly
                Log.d("Firestore", "Product list size after fetch: " + productList.size());
                Log.d("Firestore", "Full product list size after fetch: " + fullProductList.size());

                // Notify the adapter to update the RecyclerView
                productAdapter.notifyDataSetChanged();
            }
            @Override
            public void onError(Exception e) {
                Log.e("Firestore", "Error fetching food", e);
            }
        });
    }


    @Override
    protected void onResume(){
        super.onResume();
        fetchProductsFromFirestore();  // Only fetch the full list if not filtered
    }

    @Override
    protected void onPause() {
        super.onPause();
        // Reset the filter flag when the activity is paused
        isFiltered = false;
    }

    @Override
    protected void onStop() {
        super.onStop();
        // Reset the filter flag when the activity is stopped
        isFiltered = false;
    }

//handling sort functions
@Override
protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
    super.onActivityResult(requestCode, resultCode, data);

    // Check if we're returning from the Sort activity with a valid result
    if (requestCode == 1 && resultCode == RESULT_OK && data != null) {
        String nameFilter = data.getStringExtra("filterName");  // Get the filter string from the result
        String DOIfilter = data.getStringExtra("filterDOI"); // DOI Filter String
        String DOEfilter = data.getStringExtra("filterDOE"); // DOE Filter String
        Log.d("SortFilter", "Received name filter: " + nameFilter);
        Log.d("SortFilter", "Received DOI filter: " + DOIfilter);
        Log.d("SortFilter", "Received DOE filter: " + DOEfilter);

        boolean hasFilter = (nameFilter != null || DOIfilter != null || DOEfilter != null);

        // Clear product list only for filters (to reset old data)
        productList.clear();
        productAdapter.notifyDataSetChanged();

        if (hasFilter) {
            // Set isFiltered to true since we have filters
            isFiltered = true;


            // Apply the filter and fetch filtered products
            dbManager.fetchFilteredProducts(nameFilter, DOIfilter, DOEfilter, new DBManager.OnProductsFetchedListener() {
                @Override
                public void onProductsFetched(List<Product> products) {
                    Log.d("Firestore", "Filtered product count: " + products.size());
                    for (Product product : products) {
                        Log.d("Firestore", "Filtered product: " + product.getName());
                    }

                    // Add only filtered products
                    productList.clear();  // Make sure to clear the list before adding new data
                    productList.addAll(products);  // Add the filtered products
                    productAdapter.notifyDataSetChanged();  // Update the RecyclerView

                }

                @Override
                public void onError(Exception e) {
                    Log.e("Firestore", "Error fetching filtered data", e);
                }
            });
        } else {
            // No filters applied, reset the list to show all products
            dbManager.fetchProduct(new DBManager.OnProductsFetchedListener() {
                @Override
                public void onProductsFetched(List<Product> products) {
                    // Clear the list to avoid overlap
                    productList.clear();
                    productList.addAll(products);
                    productAdapter.notifyDataSetChanged();
                }

                @Override
                public void onError(Exception e) {
                    Log.e("Firestore", "Error fetching all products", e);
                }
            });
        }
    }
}


    /*NAVIGATIONS*/
    public void goHome(View view) {
        Intent intent = new Intent(this, home.class);
        startActivity(intent);
    }

    public void goFood(View view) {
        Intent intent = new Intent(this, foodManagement.class);
        startActivity(intent);
    }

    public void goCommunity(View view) {
        Intent intent = new Intent(this, community.class);
        startActivity(intent);
    }

    public void goProfile(View view) {
        Intent intent = new Intent(this, profile.class);
        startActivity(intent);
    }

    public void goToInputEntry(View view) {
        Intent intent = new Intent(this, inputEntry.class);
        startActivity(intent);
    }

    public void goToSort(View view) {
        Intent intent = new Intent(this, Sort.class);
        startActivityForResult(intent, 1);
    }
}
