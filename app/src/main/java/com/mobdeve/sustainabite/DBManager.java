package com.mobdeve.sustainabite;


import android.util.Log;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;


public class DBManager {
    private final FirebaseFirestore firestore;
    String newID;

    public DBManager() {
        this.firestore = FirebaseFirestore.getInstance();
    }

    public interface FirestoreCallback {
        void onUserIDRetrieved(String newUserID);
    }

// === USERS ===
public void getLatestUserID(FirestoreCallback callback) {
    firestore.collection("USERS")
            .get()
            .addOnSuccessListener(queryDocumentSnapshots -> {
                String latestUserID = null;

                for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                    latestUserID = document.getId();
                }

                String finalID = incrementUserId(latestUserID);

                Log.d("Increment", "Updated Latest ID: " + finalID);
                Log.d("Firestore", "Latest User ID: " + latestUserID);

                callback.onUserIDRetrieved(finalID);
            });
}


    public String incrementUserId(String LUserID) {
        String numpart = LUserID.substring(1);

        int incrementedID = Integer.parseInt(numpart) + 1;

        newID = String.format("U00%d", incrementedID);

        return newID;
    }

    public void putNewUser(String Uname, String Upass, String Umail, String Uimage) {
        getLatestUserID(newUserID -> {
            Log.d("NEW ID", "Generated New User ID: " + newUserID);

            Map<String, Object> user = new HashMap<>();
            user.put("UName", Uname);
            user.put("UPass", Upass);
            user.put("UEmail", Umail);
            user.put("UImage", Uimage);

            firestore.collection("USERS").document(newUserID).set(user);
        });

    }


// === FOODS ===
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

    // === RECIPES ===
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


