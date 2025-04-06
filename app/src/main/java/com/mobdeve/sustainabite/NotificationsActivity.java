package com.mobdeve.sustainabite;

import android.content.Intent;
import android.content.SharedPreferences;
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

                            if (foodExpiryDate != null) {
                                Date expiryDate = new Date(foodExpiryDate); // Assuming the date is in a valid format
                                Date now = new Date();

                                // Check if the food has expired
                                if (expiryDate.before(now)) {
                                    // Item is expired, add to notifications
                                    notificationList.add(new NotificationItem(
                                            R.drawable.noimage, // Replace with an actual icon
                                            foodName + " has expired!",
                                            new SimpleDateFormat("hh:mm a").format(now)
                                    ));
                                }
                            }
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
