package com.mobdeve.sustainabite;

public class NotificationItem {
    private String imageBase64;
    private String notificationText;
    private String timestamp;
    private String foodId;

    public NotificationItem(String imageBase64, String notificationText, String timestamp, String foodId) {
        this.imageBase64 = imageBase64;
        this.notificationText = notificationText;
        this.timestamp = timestamp;
        this.foodId = foodId;
    }

    public String getImageBase64() {
        return imageBase64;
    }

    public String getNotificationText() {
        return notificationText;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public String getfoodId() {
        return foodId;
    }


}

