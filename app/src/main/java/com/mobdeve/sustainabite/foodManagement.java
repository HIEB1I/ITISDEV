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
    private List<Product> productList, fullProductList;
    private ProductAdapter productAdapter;
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

        fetchProductsFromFirestore();
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
                productList.addAll(products);
                fullProductList.addAll(products);

                // Check if fullProductList is populated correctly
                Log.d("Firestore", "Full product list size after fetch: " + fullProductList.size());


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
        fetchProductsFromFirestore();
    }

//handling sort functions

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data){
        super.onActivityResult(requestCode, resultCode, data);

        // Check if we're returning from the Sort activity with a valid result
        if (requestCode == 1 && resultCode == RESULT_OK && data != null){
            String nameFilter = data.getStringExtra("filterName");  // Get the filter string from the result
            Log.d("SortFilter", "Received filter: " + nameFilter);

            // Always fetch fresh data from Firestore to prevent duplicate entries
            dbManager.fetchProduct(new DBManager.OnProductsFetchedListener() {
                @Override
                public void onProductsFetched(List<Product> products) {
                    // Clear current product lists to avoid stacking duplicates
                    productList.clear();
                    fullProductList.clear();

                    // Add fetched products to fullProductList for filtering reference
                    fullProductList.addAll(products);

                    // If there's a valid filter, apply it to the updated list
                    if (nameFilter != null && !nameFilter.isEmpty()) {
                        productAdapter.sortByName(nameFilter, fullProductList);
                    }
                    // Otherwise, reset to show all products
                    else {
                        productList.addAll(fullProductList);
                        productAdapter.notifyDataSetChanged();
                    }
                }

                @Override
                public void onError(Exception e) {
                    // Handle fetch errors gracefully
                    Log.e("Firestore", "Error fetching filtered data", e);
                }
            });
        }
        else {
            // Debug logging if no result was returned from Sort activity
            Log.d("SortFilter", "onActivityResult not triggered or filter is null");
        }
    }

    /*
 //Automatically fetches the set of products in firestore.
    private void fetchProductsFromFirestoreAutomatically() {
        dbManager.getProductCollection().addSnapshotListener((querySnapshot, e) -> {
           if (e!=null){
               Log.e("Firestore", "Error fetching food", e);
               return;
           }

           if(querySnapshot!=null){
               productList.clear();
               for (DocumentSnapshot document : querySnapshot.getDocuments()){
                   Product product = document.toObject(Product.class);
                   productList.add(product);
               }
           }
           productAdapter.notifyDataSetChanged();
        });
    }
*/
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
