package com.mobdeve.sustainabite;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.util.Base64;
import android.util.Log;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DBManager {
    private final FirebaseFirestore firestore;
    FirebaseAuth auth = FirebaseAuth.getInstance();


    public DBManager() {
        this.firestore = FirebaseFirestore.getInstance();
    }

    public interface FirestoreCallback {
        void onUserIDRetrieved(String newUserID);
    }

    public interface RecipeCallback {
        void onRecipeRetrieved(List<String> recipes);
    }

    public void finder(String word, RecipeCallback callback) {
        List<String> results = new ArrayList<>();

        // Query 1: Search in RNAME
        firestore.collection("RECIPES")
                .whereGreaterThanOrEqualTo("RNAME", word)
                .whereLessThanOrEqualTo("RNAME", word + "\uf8ff")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful() && task.getResult() != null) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            results.add(document.getString("RNAME"));
                        }
                    }

                    // Query 2: Search in RCalories
                    firestore.collection("RECIPES")
                            .whereGreaterThanOrEqualTo("RCalories", word)
                            .whereLessThanOrEqualTo("RCalories", word + "\uf8ff")
                            .get()
                            .addOnCompleteListener(task2 -> {
                                if (task2.isSuccessful() && task2.getResult() != null) {
                                    for (QueryDocumentSnapshot document : task2.getResult()) {
                                        String calorieResult = document.getString("RNAME");
                                        if (!results.contains(calorieResult)) {
                                            results.add(calorieResult);
                                        }
                                    }
                                }

                                // Query 3: Search in RIngredients
                                firestore.collection("RECIPES")
                                        .whereGreaterThanOrEqualTo("RIngredients", word)
                                        .whereLessThanOrEqualTo("RIngredients", word + "\uf8ff")
                                        .get()
                                        .addOnCompleteListener(task3 -> {
                                            if (task3.isSuccessful() && task3.getResult() != null) {
                                                for (QueryDocumentSnapshot document : task3.getResult()) {
                                                    String ingredientResult = document.getString("RNAME");
                                                    if (!results.contains(ingredientResult)) {
                                                        results.add(ingredientResult);
                                                    }
                                                }
                                            }

                                            // If no results found, return "N/A"
                                            if (results.isEmpty()) {
                                                results.add("N/A");
                                            }

                                            // Return the merged results
                                            callback.onRecipeRetrieved(results);
                                        })
                                        .addOnFailureListener(e -> {
                                            Log.e("DBManager", "Error fetching ingredients", e);
                                            callback.onRecipeRetrieved(Collections.singletonList("Error retrieving data"));
                                        });

                            })
                            .addOnFailureListener(e -> {
                                Log.e("DBManager", "Error fetching calories", e);
                                callback.onRecipeRetrieved(Collections.singletonList("Error retrieving data"));
                            });

                })
                .addOnFailureListener(e -> {
                    Log.e("DBManager", "Error fetching recipes", e);
                    callback.onRecipeRetrieved(Collections.singletonList("Error retrieving data"));
                });
    }

/*
    public void finder(String word, RecipeCallback callback) {
        firestore.collection("RECIPES").get(RCalories,RIngredients, RNAME) somewhat equal word
        return the result and the document id

        firestore.collection("FOODS").get(FNAME,FQuanType, FQuantity, FRemarks, FStorage) somewhat equal word
        return the result and the document id


    }
*/



    public void getFoodHome(FoodDataCallback callback) {
        firestore.collection("RECIPES").get().addOnCompleteListener(task -> {
                ArrayList<FoodItem> foodList = new ArrayList<>();

                for (QueryDocumentSnapshot document : task.getResult()) {
                    String rName = document.getString("RNAME");
                    String rKCal = document.getString("RCalories");
                    String rImageBase64 = document.getString("RImage");
                    String rProcedure = ("");
                    String rIngredients = ("");

                    Bitmap rImage = base64ToBitmap(rImageBase64);

                    foodList.add(new FoodItem(rImage, rName, rKCal, rProcedure, rIngredients));
                }

                callback.onFoodDataRetrieved(foodList);
        });
    }

    public interface FoodDataCallback {
        void onFoodDataRetrieved(ArrayList<FoodItem> foodList);
    }

    private Bitmap base64ToBitmap(String base64String) {
        if (base64String == null || base64String.isEmpty()) {
            return null;
        }
        byte[] decodedBytes = Base64.decode(base64String, Base64.DEFAULT);
        return android.graphics.BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);
    }



    // === USERS ===
    // == SIGN IN ==
    public void checkUser(Context context, String userEmail, String userPass, OnCheckUserListener listener) {


    firestore.collection("USERS")
            .whereEqualTo("UEmail", userEmail)
            .whereEqualTo("UPass", userPass)
            .get()
            .addOnCompleteListener(task -> {
                if (task.isSuccessful() && !task.getResult().isEmpty()) {
                    DocumentSnapshot document = task.getResult().getDocuments().get(0);

                    // Retrieve user details
                    String userId = document.getId();
                    String email = document.getString("UEmail");
                    String name = document.getString("UName");
                    String image = document.getString("UImage");

                    Log.d("Firestore", "User found: " + name);

                    // Store user details in SharedPreferences
                    SharedPreferences prefs = context.getSharedPreferences("UserPrefs", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = prefs.edit();
                    editor.putString("USER_ID", userId);
                    editor.putString("USER_EMAIL", email);
                    editor.putString("USER_NAME", name);
                    editor.putString("USER_IMAGE", image);
                    editor.putBoolean("IS_LOGGED_IN", true);
                    editor.apply();

                    listener.onResult(true);
                } else {
                    Log.d("Firestore", "User not found.");
                    listener.onResult(false);
                }
            });
}

    public interface OnCheckUserListener {
        void onResult(boolean exists);
    }

    // == SIGN UP ==
    public void getLatestUserID(FirestoreCallback callback) {
        firestore.collection("USERS")
                .orderBy("UName")
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
        if (LUserID == null || LUserID.isEmpty()) {
            return "U001";
        }
        try {
            String numpart = LUserID.substring(1);
            int incrementedID = Integer.parseInt(numpart) + 1;
            return String.format("U%03d", incrementedID);
        } catch (NumberFormatException e) {
            Log.e("IncrementError", "Invalid UserID format: " + LUserID, e);
            return "U001";
        }
    }

    public void putNewUser(String Uname, String Upass, String Umail, String Uimage) {
        getLatestUserID(newUserID -> {
            if (newUserID == null || newUserID.isEmpty()) {
                Log.e("DB_ERROR", "Failed to generate new user ID.");
                return;
            }

            Log.d("NEW ID", "Generated New User ID: " + newUserID);
            Log.d("SignUpDebug", "Username: " + Uname);
            Log.d("SignUpDebug", "Password: " + Upass);
            Log.d("SignUpDebug", "Email: " + Umail);
            Log.d("SignUpDebug", "Image String Length: " + (Uimage != null ? Uimage.length() : 0));

            Map<String, Object> user = new HashMap<>();
            user.put("UName", Uname);
            user.put("UPass", Upass);
            user.put("UEmail", Umail);
            user.put("UImage", Uimage);

            firestore.collection("USERS").document(newUserID).set(user)
                    .addOnSuccessListener(aVoid -> Log.d("Firestore", "User successfully added!"))
                    .addOnFailureListener(e -> Log.e("Firestore", "Error adding user", e));
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


