package com.mobdeve.sustainabite;


import android.util.Log;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;


public class DBManager {
    private final FirebaseFirestore firestore;

    public DBManager() {
        this.firestore = FirebaseFirestore.getInstance();
    }

    public void getLatestUserID() {
        firestore.collection("USERS")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    String latestUserID = null;

                    for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                        latestUserID = document.getId();
                    }

                    Log.d("Firestore", "Latest User ID: " + latestUserID);
                });
    }


  public void getLatestFoodID() {
        firestore.collection("FOODS")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    String latestFoodID = null;

                    for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                        latestFoodID = document.getId();
                    }

                    Log.d("Firestore", "Latest Food ID: " + latestFoodID);
                });
    }

    public void getLatestRecipeID() {
        firestore.collection("RECIPES")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    String latestRecipeID = null;

                    for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                        latestRecipeID = document.getId();
                    }

                    Log.d("Firestore", "Latest Recipe ID: " + latestRecipeID);
                });
    }
}


