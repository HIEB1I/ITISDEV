package com.mobdeve.sustainabite;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.NotificationViewHolder> {

    private List<NotificationItem> notificationList;

    public NotificationAdapter(List<NotificationItem> notificationList) {
        this.notificationList = notificationList;
    }

    public static class NotificationViewHolder extends RecyclerView.ViewHolder {
        ImageView notificationImage;
        TextView notificationText, notificationTime;

        public NotificationViewHolder(View view) {
            super(view);
            notificationImage = view.findViewById(R.id.notificationImage);
            notificationText = view.findViewById(R.id.notificationText);
            notificationTime = view.findViewById(R.id.notificationTime);
        }
    }

    @NonNull
    @Override
    public NotificationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_notification, parent, false);
        return new NotificationViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NotificationViewHolder holder, int position) {
        NotificationItem notification = notificationList.get(position);

        // Retrieve base64 string from NotificationItem
        String base64Image = notification.getImageBase64(); // Use appropriate method to get the base64 string

        // Decode the base64 string to a Bitmap (decode only when binding the view)
        Bitmap notificationImage = DBManager.decodeBase64ToBitmap(base64Image);


        holder.notificationImage.setImageBitmap(notificationImage);
        holder.notificationText.setText(notification.getNotificationText());
        holder.notificationTime.setText(notification.getTimestamp());


        // Set a click listener to navigate to ProductDetailsActivity
        holder.itemView.setOnClickListener(view -> {
            // Get the foodId
            String foodId = notification.getfoodId();

            // Navigate to ProductDetailsActivity and pass the foodId
            Intent intent = new Intent(view.getContext(), ProductDetailsActivity.class);
            intent.putExtra("foodId", foodId);
            view.getContext().startActivity(intent);
        });

        // If decoding fails, use a default image
        if (notificationImage == null) {
            notificationImage = BitmapFactory.decodeResource(holder.itemView.getContext().getResources(), R.drawable.noimage);
        }
    }

    @Override
    public int getItemCount() {
        return notificationList.size();
    }
}

