package com.mobdeve.sustainabite;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class NotificationsActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private NotificationAdapter adapter;
    private List<NotificationItem> notificationList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notifications);


        // Initialize the notification list first
        notificationList = new ArrayList<>();

        recyclerView = findViewById(R.id.notificationsRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        SharedPreferences prefs = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        String currentUserId = prefs.getString("USER_ID", null);

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        if (currentUserId != null) {
            Log.d("NotifActivity", "User ID: " + currentUserId);

            // 🔥 Fetch food items owned by the user and check if expired
            db.collection("FOODS")
                    .whereEqualTo("UNUM", currentUserId)  // Filter by user's ID (UNum field)
                    .get()
                    .addOnSuccessListener(queryDocumentSnapshots -> {
                        for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                            String foodName = document.getString("FNAME");
                            String foodExpiryDate = document.getString("FDOE"); // Date of Expiry
                            String foodImageBase64 = document.getString("FImage"); // food image on base64.


                            Log.d("Firestore", "foodName: " + foodName);
                            Log.d("NotifActivity", "FoodExpiryDate: " + foodExpiryDate);

                            if (foodExpiryDate != null) {
                                try {
                                    // Use SimpleDateFormat to parse the date string
                                    SimpleDateFormat dateFormat = new SimpleDateFormat("d/M/yyyy");
                                    Date expiryDate = dateFormat.parse(foodExpiryDate); // Parse the expiry date string

                                    Date now = new Date();

                                    // Check if expired
                                    if (expiryDate.before(now)) {
                                        String imageBase64 = foodImageBase64 != null && !foodImageBase64.isEmpty() ? foodImageBase64 : null;

                                        notificationList.add(new NotificationItem(
                                                imageBase64,
                                                foodName + " has expired!",
                                                new SimpleDateFormat("hh:mm a").format(now)
                                        ));
                                    }
                                } catch (Exception e) {
                                    Log.e("NotifActivity", "Error parsing expiry date: " + foodExpiryDate, e);
                                }
                            }
                        }
                        // Log the contents of notificationList
                        for (NotificationItem notification : notificationList) {
                            Log.d("NotifList",
                                    "Text: " + notification.getNotificationText() +
                                    ", Time: " + notification.getTimestamp());
                        }
                        // Update the notification list and notify adapter
                        adapter.notifyDataSetChanged();
                    })
                    .addOnFailureListener(e -> Log.e("NotifActivity", "Error fetching food data", e));

        } else {
            Log.e("NotifActivity", "User ID not found in SharedPreferences");
        }


        notificationList = new ArrayList<>();
        /*
        notificationList.add(new NotificationItem(R.drawable.sample_profile, "New recipe added!", "10:05 am"));
        notificationList.add(new NotificationItem(R.drawable.sample_profile, "Your order has been shipped", "12:00 pm"));
        notificationList.add(new NotificationItem(R.drawable.sample_profile, "Discount on healthy foods!", "4:40 pm"));
        notificationList.add(new NotificationItem(R.drawable.sample_profile, "Reminder: Check your meal plan", "6:30 pm"));
        */
        adapter = new NotificationAdapter(notificationList);
        recyclerView.setAdapter(adapter);
    }

    /* NAVIGATIONS */
    public void goHome(View view) {
        startActivity(new Intent(this, home.class));
    }

    public void goFood(View view) {
        startActivity(new Intent(this, foodManagement.class));
    }

    public void goCommunity(View view) {
        startActivity(new Intent(this, community.class));
    }

    public void goProfile(View view) {
        startActivity(new Intent(this, profile.class));
    }
}
