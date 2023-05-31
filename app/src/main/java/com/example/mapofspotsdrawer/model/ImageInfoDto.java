package com.example.mapofspotsdrawer.model;

import androidx.annotation.NonNull;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Date;

public class ImageInfoDto implements Serializable {

    private Long id;

    private String url;

    private int size;

    private Date uploadDate;

    public ImageInfoDto() {
    }

    public ImageInfoDto(Long id, String url, int size, Date uploadDate) {
        this.id = id;
        this.url = url;
        this.size = size;
        this.uploadDate = uploadDate;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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
