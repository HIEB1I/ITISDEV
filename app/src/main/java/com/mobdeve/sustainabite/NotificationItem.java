package com.mobdeve.sustainabite;

public class NotificationItem {
    private int imageResId;
    private String notificationText;
    private String timestamp;

    public NotificationItem(int imageResId, String notificationText, String timestamp) {
        this.imageResId = imageResId;
        this.notificationText = notificationText;
        this.timestamp = timestamp;
    }

    public int getImageResId() {
        return imageResId;
    }

    public String getNotificationText() {
        return notificationText;
    }

    public String getTimestamp() {
        return timestamp;
    }
}

