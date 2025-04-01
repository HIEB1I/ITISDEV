package com.mobdeve.sustainabite;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldPath;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
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

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

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

    public interface OnRecipeListener {
        void onSuccess();
        void onFailure(Exception e);
    }

    public interface OnUserUpdateListener {
        void onSuccess();
        void onFailure(Exception e);
    }

    public interface OnFoodAddedListener {
        void onSuccess();
        void onError(Exception e);
    }

    public interface OnFoodUpdatedListener  {
        void onSuccess();
        void onError(Exception e);
    }

    public interface CheckFoodIDValidity{
        void onSuccess(String storage, String remarks);
        void onFailure(Exception e);
    }

    public interface OnUserDetailsFetchedListener {
        void onUserDetailsFetched(String userId, String email, String name, String image);
        void onError(String message);
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

    // == SIGN OUT ==
    public void logoutUser(Context context) {
        // Clear user session from SharedPreferences
        SharedPreferences prefs = context.getSharedPreferences("UserPrefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.clear();
        editor.apply();

        FirebaseAuth.getInstance().signOut();

        Log.d("Firestore", "User logged out successfully.");
    }

    // === PROFILE ===
    public void getCurrentUserDetails(Context context, OnUserDetailsFetchedListener listener) {
        SharedPreferences prefs = context.getSharedPreferences("UserPrefs", Context.MODE_PRIVATE);
        boolean isLoggedIn = prefs.getBoolean("IS_LOGGED_IN", false);

        if (isLoggedIn) {
            String userId = prefs.getString("USER_ID", "");
            String email = prefs.getString("USER_EMAIL", "");
            String name = prefs.getString("USER_NAME", "");
            String image = prefs.getString("USER_IMAGE", "");

            if (userId.isEmpty()) {
                listener.onError("No user data found.");
            } else {
                listener.onUserDetailsFetched(userId, email, name, image);
            }
        } else {
            listener.onError("User not logged in.");
        }
    }

    // === PROFILE UPDATE ===
    public void updateUserProfile(String userId, String newUsername, String newEmail, String newPassword, OnUserUpdateListener listener) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        Map<String, Object> updatedData = new HashMap<>();
        updatedData.put("UName", newUsername);
        updatedData.put("UEmail", newEmail);
        updatedData.put("UPass", newPassword);

        db.collection("USERS").document(userId)
                .update(updatedData)
                .addOnSuccessListener(aVoid -> {
                    Log.d("Firestore", "User profile updated successfully: " + userId);
                    if (listener != null) listener.onSuccess();
                })
                .addOnFailureListener(e -> {
                    Log.e("Firestore", "Failed to update user profile: " + e.getMessage());
                    if (listener != null) listener.onFailure(e);
                });
    }


    // === FOODS ===
    public void getLatestFoodID(OnSuccessListener<String> onSuccessListener) {
        firestore.collection("FOODS")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    String latestFoodID = "F000"; // Default if empty
                    int maxNum = 0;

                    for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                        String docId = document.getId(); // Gets the ID of the document
                        if (docId.startsWith("F")) { //if the document ID starts with F...
                            try {
                                int num = Integer.parseInt(docId.substring(1)); //Gets the number from the string
                                if (num > maxNum) {
                                    maxNum = num; // Get the highest number
                                }
                            } catch (NumberFormatException e) {
                                Log.e("Firestore", "Invalid Food ID format: " + docId);
                            }
                        }
                    }
                    latestFoodID = "F" + String.format("%03d", maxNum); // Format as "FXXX"
                    Log.d("Firestore", "Manually Found Latest Food ID: " + latestFoodID);
                    onSuccessListener.onSuccess(latestFoodID);
                })
                .addOnFailureListener(e -> {
                    Log.e("Firestore", "Error fetching latest Food ID", e);
                    onSuccessListener.onSuccess("F000"); // Default fallback
                });
    }

    private String generateNextFoodID (String lastID){
        try {
            //Get number after F00
            int lastNumber = Integer.parseInt(lastID.substring(1));
            return String.format("F%03d", lastNumber+1);
        }catch (NumberFormatException e) {
            Log.e("Firestore", "Error parsing Food ID", e);
            return "F000"; // Fallback
        }
    }

    public CollectionReference getProductCollection(){
        return firestore.collection("FOODS");
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
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("RECIPES")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    List<FoodItem> foodItems = new ArrayList<>();
                    for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                        String documentId = document.getId();
                        String name = document.getString("RNAME");
                        String kcal = document.getString("RCalories");
                        String ingredients = document.getString("RIngredients");
                        String procedures = document.getString("RProcedure");
                        String imageString = document.getString("RImage");

                        Bitmap imageBitmap = null;
                        if (imageString != null && !imageString.isEmpty()) {
                            imageBitmap = decodeBase64ToBitmap(imageString);
                            if (imageBitmap == null) {
                                Log.e("Firestore", "Failed to decode image for recipe: " + name);
                            }
                        } else {
                            Log.e("Firestore", "No image data found for recipe: " + name);
                        }

                        foodItems.add(new FoodItem(documentId, imageString, name, kcal, ingredients, procedures));
                    }
                    listener.onRecipesFetched(foodItems);
                })
                .addOnFailureListener(e -> {
                    Log.e("Firestore", "Error fetching recipes", e);
                    listener.onError(e);
                });
    }

    public void addRecipeToFirestore(Context context, String rImage, String rIngredients, String rName, String rProcedure, String rCalories, OnRecipeAddedListener listener) {
        SharedPreferences prefs = context.getSharedPreferences("UserPrefs", Context.MODE_PRIVATE);
        String uNum = prefs.getString("USER_ID", "U000"); // Retrieve stored UNum

        CollectionReference recipesRef = firestore.collection("RECIPES");

        recipesRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                int maxId = 0;
                for (QueryDocumentSnapshot document : task.getResult()) {
                    String docId = document.getId();
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

                // Generate new Recipe ID
                String newRecipeId = "R" + String.format("%03d", maxId + 1);

                // Create Recipe Data
                Map<String, Object> recipeData = new HashMap<>();
                recipeData.put("RImage", rImage);
                recipeData.put("RIngredients", rIngredients);
                recipeData.put("RNAME", rName);
                recipeData.put("RProcedure", rProcedure);
                recipeData.put("RCalories", rCalories);
                recipeData.put("UNum", uNum);

                // Add Recipe to Firestore
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

    public void updateRecipeInFirestore(String recipeId, String name, String kcal, String ingredients, String procedures, OnRecipeListener listener) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        Map<String, Object> updatedData = new HashMap<>();
        updatedData.put("RNAME", name);
        updatedData.put("RCalories", kcal);
        updatedData.put("RIngredients", ingredients);
        updatedData.put("RProcedure", procedures);

        db.collection("RECIPES").document(recipeId)
                .update(updatedData)
                .addOnSuccessListener(aVoid -> {
                    Log.d("Firestore", "Recipe updated successfully: " + recipeId);
                    if (listener != null) listener.onSuccess();
                })
                .addOnFailureListener(e -> {
                    Log.e("Firestore", "Failed to update recipe: " + e.getMessage());
                    if (listener != null) listener.onFailure(e);
                });
    }

    public void deleteRecipeFromFirestore(String documentId, OnRecipeListener listener) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("RECIPES").document(documentId)
                .delete()
                .addOnSuccessListener(aVoid -> listener.onSuccess())
                .addOnFailureListener(e -> listener.onFailure(e));
    }

    public static Bitmap decodeBase64ToBitmap(String base64String) {
        if (base64String == null || base64String.isEmpty()) {
            Log.e("DecodeBase64", "Empty Base64 string");
            return null;
        }

        try {
            byte[] decodedBytes = Base64.decode(base64String, Base64.DEFAULT);
            return BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);
        } catch (Exception e) {
            Log.e("DecodeBase64", "Failed to decode Base64 string", e);
            return null;
        }
    }

    // FOOD MANAGEMENT
    //Fetch all the Products (Food)
    public void fetchProduct(OnProductsFetchedListener listener) {
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
                            String imageResId = document.getString("FImage");

                            Log.d("Firestore", "Food Id: " + FID + ", Name: " + name);
                            Product product = new Product(name, FID, quantity,qty_type,doi,doe,storage,remarks,imageResId); //added FID here so that food ID can be stored on intent.
                            productList.add(product);
                        }
                        // lets listener know that there are products being fetched.
                        listener.onProductsFetched(productList);
                    } else {
                        listener.onError(task.getException());
                    }

                });
    }


    //Code for adding Food

    public void addFoodToFirestore(Context context, String FNAME, String FDOI, String FDOE, Integer FQuantity, String FQuanType, String FSTORAGE, String FRemarks, String FImage, OnFoodAddedListener listener){
        SharedPreferences prefs = context.getSharedPreferences("UserPrefs", Context.MODE_PRIVATE);
        String uNum = prefs.getString("USER_ID", "U000"); // Default to "U000" if not found

        getLatestFoodID(latestFoodID -> {

            String newFoodID = generateNextFoodID(latestFoodID); // Generate next ID

            Map<String, Object> foodData = new HashMap<>();
            foodData.put("FDOE", FDOE);
            foodData.put("FDOI", FDOI);
            foodData.put("FNAME", FNAME);
            foodData.put("FNUM",newFoodID);
            foodData.put("FQuantity", FQuantity);
            foodData.put("FQuanType", FQuanType);
            foodData.put("FSTORAGE", FSTORAGE);
            foodData.put("FRemarks", FRemarks);
            foodData.put("UNUM", uNum);
            foodData.put("FImage", FImage);



            firestore.collection("FOODS")
                    .document(newFoodID)
                    .set(foodData)
                    .addOnSuccessListener(documentReference -> {
                        Log.d("DBManager", "DocumentSnapshot added with ID: " + newFoodID);
                        if (listener != null) listener.onSuccess();
                    })
                    .addOnFailureListener(e -> {
                        Log.w("DBManager", "Error adding document", e);
                        if (listener != null) listener.onError(e);
                    });
        });
    }

    //Get the specific food details
    public void fetchSpecificFood(String FID, CheckFoodIDValidity callback){
        if (FID == null || FID.isEmpty()){
            callback.onFailure(new Exception("Food ID is invalid."));
            return;
        }

        DocumentReference Food = firestore.collection("FOODS").document(FID);

        Food.get().addOnCompleteListener(task -> {
           if (task.isSuccessful() && task.getResult().exists()){
               DocumentSnapshot document = task.getResult();
               String storage = document.getString("FSTORAGE");
               String remarks = document.getString("FRemarks");
               callback.onSuccess(storage, remarks);
           }else{
               callback.onFailure(new Exception("No food item exists."));
           }
        });

    }

    //Code for updating the actual food data
    public void updateFoodInFirestore(Context context, String foodId, String FNAME,
                                      String FDOI, String FDOE, Integer FQuantity, String FQuanType,
                                      String FSTORAGE, String FRemarks, OnFoodUpdatedListener listener){
        //check if foodId exists
        if (foodId == null || foodId.isEmpty()){
            Log.e("DBManager", "Invalid Food ID: Cannot update this one.");
            return;
        }

        //Data for updating
        Map<String, Object> updatedData = new HashMap<>();
        updatedData.put("FDOE", FDOE);
        updatedData.put("FDOI", FDOI);
        updatedData.put("FNAME", FNAME);
        updatedData.put("FQuantity", FQuantity);
        updatedData.put("FQuanType", FQuanType);
        updatedData.put("FSTORAGE", FSTORAGE);
        updatedData.put("FRemarks", FRemarks);

        //This code will do the firestore update itself
        firestore.collection("FOODS")
                .document(foodId)
                .update(updatedData)
                .addOnSuccessListener(aVoid -> {
                    Log.d("DBManager", "Food entry updated successfully");
                    if (listener != null) listener.onSuccess();
                })
                .addOnFailureListener(e -> {
                    Log.e("DBManager", "Error updating food entry", e);
                    if (listener != null) listener.onError(e);
                });
    }

    //format the date so that it shows month day, year

    public static String convertDate(String inputDate) {
        try {

            //This is the initial format of the date
            SimpleDateFormat inputFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH);


            //Ideal outcome/format of the date.
            SimpleDateFormat outputFormat = new SimpleDateFormat("MMMM dd, yyyy", Locale.ENGLISH);

            //This one makes the inputDate into a "date object"
            Date date = inputFormat.parse(inputDate);

            // Returns the date as the ideal format.
            return outputFormat.format(date);

        } catch (ParseException e) {
            e.printStackTrace();
            return "Invalid date";
        }
    }


}


