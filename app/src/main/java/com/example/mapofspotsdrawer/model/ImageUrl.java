package com.example.mapofspotsdrawer.model;

public class ImageUrl {
    private Long id;
    private String imageUrl;
    private boolean isFromServer;

    public ImageUrl(Long id, String imageUrl, boolean isFromServer) {
        this.id = id;
        this.imageUrl = imageUrl;
        this.isFromServer = isFromServer;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public void setFromServer(boolean fromServer) {
        isFromServer = fromServer;
    }
}
