package com.example.mapofspotsdrawer.model;

import java.util.Date;

public class Comment {
    private Long id;
    private String text;

    private Date uploadDate;

    private UserWithoutSpots commentatorDto;

    public Comment() {
    }

    public Comment(Long id, String text, Date uploadDate, UserWithoutSpots commentatorDto) {
        this.id = id;
        this.text = text;
        this.uploadDate = uploadDate;
        this.commentatorDto = commentatorDto;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Date getUploadDate() {
        return uploadDate;
    }

    public void setUploadDate(Date uploadDate) {
        this.uploadDate = uploadDate;
    }

    public UserWithoutSpots getCommentatorDto() {
        return commentatorDto;
    }

    public void setCommentatorDto(UserWithoutSpots commentatorDto) {
        this.commentatorDto = commentatorDto;
    }
}
