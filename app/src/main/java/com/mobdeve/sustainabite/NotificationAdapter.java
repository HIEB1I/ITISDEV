package com.mobdeve.sustainabite;

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
        holder.notificationImage.setImageResource(notification.getImageResId());
        holder.notificationText.setText(notification.getNotificationText());
        holder.notificationTime.setText(notification.getTimestamp());
    }

    @Override
    public int getItemCount() {
        return notificationList.size();
    }
}

