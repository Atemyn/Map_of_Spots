package com.example.mapofspotsdrawer.model;

import androidx.annotation.NonNull;

import java.util.Date;

public class ImageInfoDto {
    private String url;

    private int size;

    private Date uploadDate;

    public ImageInfoDto() {
    }

    public ImageInfoDto(String url, int size, Date uploadDate) {
        this.url = url;
        this.size = size;
        this.uploadDate = uploadDate;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public Date getUploadDate() {
        return uploadDate;
    }

    public void setUploadDate(Date uploadDate) {
        this.uploadDate = uploadDate;
    }

    @NonNull
    @Override
    public String toString() {
        return "ImageInfoDto{" +
                "url='" + url + '\'' +
                ", size=" + size +
                ", uploadDate=" + uploadDate +
                '}';
    }
}
