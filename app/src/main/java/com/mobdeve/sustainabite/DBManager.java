package com.mobdeve.sustainabite;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
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

    public interface OnRecipesFetchedListener {
        void onRecipesFetched(List<FoodItem> recipes);
        void onError(Exception e);
    }

    public interface OnProductsFetchedListener {
        void onProductsFetched(List<Product> foods);
        void onError(Exception e);
    }

    public interface OnRecipeAddedListener {
        void onSuccess();
        void onFailure(Exception e);
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

    public void fetchRecipes(OnRecipesFetchedListener listener) {
        firestore.collection("RECIPES")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        List<FoodItem> foodList = new ArrayList<>();
                        for (DocumentSnapshot document : task.getResult()) {
                            String name = document.getString("RNAME");
                            String image = document.getString("RImage");
                            String ingredients = document.getString("RIngredients");
                            String procedure = document.getString("RProcedure");

                            int imageResId = R.drawable.banana;

                            foodList.add(new FoodItem(imageResId, name, image, ingredients, procedure));
                        }
                        listener.onRecipesFetched(foodList);
                    } else {
                        listener.onError(task.getException());
                    }
                });
    }

    // Fetch last Recipe ID and add a new one
    public void addRecipeToFirestore(Context context, String rImage, String rIngredients, String rName, String rProcedure, OnRecipeAddedListener listener) {
        // Retrieve user ID (UNum) from SharedPreferences
        SharedPreferences prefs = context.getSharedPreferences("UserPrefs", Context.MODE_PRIVATE);
        String uNum = prefs.getString("UNum", "U000"); // Default to "U000" if not found

        CollectionReference recipesRef = firestore.collection("RECIPES");

        // Get last document ID and determine next ID
        recipesRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                int maxId = 0;

                for (QueryDocumentSnapshot document : task.getResult()) {
                    String docId = document.getId(); // e.g., "R001"
                    if (docId.startsWith("R")) {
                        try {
                            int num = Integer.parseInt(docId.substring(1));
                            if (num > maxId) {
                                maxId = num;
                            }
                        } catch (NumberFormatException e) {
                            Log.e("Firestore", "Invalid Recipe ID format: " + docId);
                        }
                    }
                }

                // New recipe ID (increment from last found)
                String newRecipeId = "R" + String.format("%03d", maxId + 1);

                // Recipe data
                Map<String, Object> recipeData = new HashMap<>();
                recipeData.put("RImage", rImage);
                recipeData.put("RIngredients", rIngredients);
                recipeData.put("RNAME", rName);
                recipeData.put("RProcedure", rProcedure);
                recipeData.put("UNum", uNum); // Assign logged-in user ID

                // Add to Firestore
                recipesRef.document(newRecipeId)
                        .set(recipeData)
                        .addOnSuccessListener(unused -> {
                            Log.d("Firestore", "Recipe added with ID: " + newRecipeId);
                            if (listener != null) listener.onSuccess();
                        })
                        .addOnFailureListener(e -> {
                            Log.e("Firestore", "Error adding recipe", e);
                            if (listener != null) listener.onFailure(e);
                        });

            } else {
                Log.e("Firestore", "Error fetching recipes", task.getException());
                if (listener != null) listener.onFailure(task.getException());
            }
        });
    }

    // FOOD MANAGEMENT
    //Fetch all the Products (Food)
    public void fetchProduct(OnProductsFetchedListener listener){
        firestore.collection("FOODS")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        List<Product> productList = new ArrayList<>();
                        for (DocumentSnapshot document : task.getResult()) {
                            String FID = document.getId();
                            String name = document.getString("FNAME");

                            // Integer quantity = document.getLong("FQuantity").intValue();
                            Long quantityLong = document.getLong("FQuantity");
                            int quantity = (quantityLong != null) ? quantityLong.intValue() : 0;
                            String doi = document.getString("FDOI");
                            String doe = document.getString("FDOE");
                            String qty_type = document.getString("FQuanType");
                            String storage = document.getString("FRemarks");
                            String remarks = document.getString("FSTORAGE");
                            int imageResId = R.drawable.banana;

                            Log.d("Firestore", "Food Id: " + FID + ", Name: " + name);
                            Product product = new Product(name, quantity,qty_type,doi,doe,storage,remarks,imageResId);
                            productList.add(product);
                        }
                        listener.onProductsFetched(productList);
                    } else {
                        listener.onError(task.getException());
                    }
                });
    }

    //format the date so that it shows month day, year

    public static String convertDate(String inputDate){
        try {

            //This is the initial format of the date
            SimpleDateFormat inputFormat = new SimpleDateFormat("M/d/yyyy", Locale.ENGLISH);


            //Ideal outcome/format of the date.
            SimpleDateFormat outputFormat = new SimpleDateFormat("MMMM dd, yyyy", Locale.ENGLISH);

            //This one makes the inputDate into a "date object"
            Date date = inputFormat.parse(inputDate);

            // Returns the date as the ideal format.
            return outputFormat.format(date);

        }catch(ParseException e){
            e.printStackTrace();
            return "Invalid date";
        }
    }
}


