package com.mobdeve.sustainabite;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class foodManagement extends AppCompatActivity {

    private DBManager dbManager;
    private List<Product> productList;
    private ProductAdapter productAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_foodmanagement);

        dbManager = new DBManager();



        /* IGNORE THIS CODE
        // Sample Product Data
        List<Product> productList = new ArrayList<>();
        productList.add(new Product("Bananas","3","pc", "1/1/2025", "1/2/2025", "Pantry", "Ripe", R.drawable.banana));
        productList.add(new Product("Egg", "12", "pc", "5/24/2025", "11/11/2026", "Cupboard", "Fresh from farm", R.drawable.egg));
        productList.add(new Product("Rice", "1", "kg", "3/14/2015", "4/26/2024","Pantry","Jasmine", R.drawable.rice));
        productList.add(new Product("Bananas","3","pc", "1/1/2025", "1/2/2025", "Pantry", "Ripe", R.drawable.banana));
        productList.add(new Product("Egg", "12", "pc", "5/24/2025", "11/11/2026", "Cupboard", "Fresh from farm", R.drawable.egg));
        productList.add(new Product("Rice", "1", "kg", "3/14/2015", "4/26/2024","Pantry","Jasmine", R.drawable.rice));
        productList.add(new Product("Bananas","3","pc", "1/1/2025", "1/2/2025", "Pantry", "Ripe", R.drawable.banana));
        productList.add(new Product("Egg", "12", "pc", "5/24/2025", "11/11/2026", "Cupboard", "Fresh from farm", R.drawable.egg));
        productList.add(new Product("Rice", "1", "kg", "3/14/2015", "4/26/2024","Pantry","Jasmine", R.drawable.rice));

        ProductAdapter productAdapter = new ProductAdapter(productList);

        recyclerView.setAdapter(productAdapter);
         */

        //Initialize the array for profucts.
        productList = new ArrayList<>();
        productAdapter = new ProductAdapter(productList);

        // Initialize RecyclerView
        RecyclerView recyclerView = findViewById(R.id.recyclerViewProducts);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(productAdapter);

        fetchProductsFromFirestore();
    }

    //Fetch the set of products that are in Firestore.
    private void fetchProductsFromFirestore(){
        dbManager.fetchProduct(new DBManager.OnProductsFetchedListener() {
            @Override
            public void onProductsFetched(List<Product> products) {
                Log.d("Firestore", "Fetched " + products.size() + " products.");
                for (Product product : products) {
                    Log.d("Firestore", "Product: " + product.getName());
                }
                productList.clear();
                productList.addAll(products);
                productAdapter.notifyDataSetChanged();
            }
            @Override
            public void onError(Exception e) {
                Log.e("Firestore", "Error fetching food", e);
            }
        });
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
        startActivity(intent);
    }
}
