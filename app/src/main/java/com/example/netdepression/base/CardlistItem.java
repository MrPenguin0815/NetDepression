package com.example.netdepression.base;

public class CardlistItem {
    private String title;
    private String imageUrl;
    private String resourceId;


    public String getResourceId() {
        return resourceId;
    }

    public void setResourceId(String resourceId) {
        this.resourceId = resourceId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public CardlistItem(String title, String imageUrl, String resourceId) {
        this.title = title;
        this.imageUrl = imageUrl;
        this.resourceId = resourceId;
    }
}
