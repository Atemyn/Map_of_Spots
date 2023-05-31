package com.example.mapofspotsdrawer.model;

public class ImageUrl {
    private String imageUrl;
    private boolean isFromServer;

    public ImageUrl(String imageUrl, Boolean isFromServer) {
        this.imageUrl = imageUrl;
        this.isFromServer = isFromServer;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public Boolean isFromServer() {
        return isFromServer;
    }

    public void setFromServer(Boolean fromServer) {
        isFromServer = fromServer;
    }
}
