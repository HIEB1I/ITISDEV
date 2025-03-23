package com.mobdeve.sustainabite;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class foodManagement extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_foodmanagement);

        // Initialize RecyclerView
        RecyclerView recyclerView = findViewById(R.id.recyclerViewProducts);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Sample Product Data
        List<Product> productList = new ArrayList<>();
        productList.add(new Product("Bananas", "3 pcs", R.drawable.banana));
        productList.add(new Product("Egg", "12 pcs", R.drawable.egg));
        productList.add(new Product("Rice", "1 kg", R.drawable.rice));
        productList.add(new Product("Bananas", "3 pcs", R.drawable.banana));
        productList.add(new Product("Egg", "12 pcs", R.drawable.egg));
        productList.add(new Product("Rice", "1 kg", R.drawable.rice));
        productList.add(new Product("Bananas", "3 pcs", R.drawable.banana));
        productList.add(new Product("Egg", "12 pcs", R.drawable.egg));
        productList.add(new Product("Rice", "1 kg", R.drawable.rice));

        ProductAdapter productAdapter = new ProductAdapter(productList);
        recyclerView.setAdapter(productAdapter);
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
