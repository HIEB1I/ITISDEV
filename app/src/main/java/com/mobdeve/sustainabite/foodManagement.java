package com.mobdeve.sustainabite;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.DocumentSnapshot;

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
        //Clear Data first in order to ensure that it does not show the old data.
        productList.clear();
        productAdapter.notifyDataSetChanged();
        dbManager.fetchProduct(new DBManager.OnProductsFetchedListener() {
            @Override

            public void onProductsFetched(List<Product> products) {
                Log.d("Firestore", "Fetched " + products.size() + " products.");
                for (Product product : products) {
                    Log.d("Firestore", "Product: " + product.getName());
                }
                productList.addAll(products);
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
        startActivity(intent);
    }
}
