package com.example.mapofspotsdrawer.model;

import java.util.Date;

public class Comment {
    private String text;

    private Date uploadDate;

    private UserWithoutSpots commentatorDto;

    public Comment() {
    }

    public Comment(String text, Date uploadDate, UserWithoutSpots commentatorDto) {
        this.text = text;
        this.uploadDate = uploadDate;
        this.commentatorDto = commentatorDto;
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
