package com.mobdeve.sustainabite;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
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

        notificationList = new ArrayList<>();
        notificationList.add(new NotificationItem(R.drawable.sample_profile, "New recipe added!", "10:05 am"));
        notificationList.add(new NotificationItem(R.drawable.sample_profile, "Your order has been shipped", "12:00 pm"));
        notificationList.add(new NotificationItem(R.drawable.sample_profile, "Discount on healthy foods!", "4:40 pm"));
        notificationList.add(new NotificationItem(R.drawable.sample_profile, "Reminder: Check your meal plan", "6:30 pm"));

        adapter = new NotificationAdapter(notificationList);
        recyclerView.setAdapter(adapter);
    }
}
