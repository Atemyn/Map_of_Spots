package com.example.mapofspotsdrawer.model;

import java.util.Date;
import java.util.List;

public class User {
    private String name;

    private String email;

    private String phoneNumber;

    private Date birthday;

    private Date regDate;

    private String password;

    private List<ImageInfoDto> imageInfoDtoList;

    private List<Spot> createdSpots;

    private List<Long> likedSpotIds;

    private List<Long> favoriteSpotIds;

    public User() {
    }

    public User(String name, String email, String phoneNumber,
                Date birthday, Date registrationDate, String password,
                List<ImageInfoDto> imageInfoDtoList,
                List<Spot> createdSpots, List<Long> likedSpotIds, List<Long> favoriteSpotIds) {
        this.name = name;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.birthday = birthday;
        this.regDate = registrationDate;
        this.password = password;
        this.imageInfoDtoList = imageInfoDtoList;
        this.createdSpots = createdSpots;
        this.likedSpotIds = likedSpotIds;
        this.favoriteSpotIds = favoriteSpotIds;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    public Date getRegDate() {
        return regDate;
    }

    public void setRegDate(Date registrationDate) {
        this.regDate = registrationDate;
    }

    public List<ImageInfoDto> getImageInfoDtoList() {
        return imageInfoDtoList;
    }

    public void setImageInfoDtoList(List<ImageInfoDto> imageInfoDtoList) {
        this.imageInfoDtoList = imageInfoDtoList;
    }

    public List<Spot> getCreatedSpots() {
        return createdSpots;
    }

    public void setCreatedSpots(List<Spot> createdSpots) {
        this.createdSpots = createdSpots;
    }

    public List<Long> getLikedSpotIds() {
        return likedSpotIds;
    }

    public void setLikedSpotIds(List<Long> likedSpotIds) {
        this.likedSpotIds = likedSpotIds;
    }

    public List<Long> getFavoriteSpotIds() {
        return favoriteSpotIds;
    }

    public void setFavoriteSpotIds(List<Long> favoriteSpotIds) {
        this.favoriteSpotIds = favoriteSpotIds;
    }

    @Override
    public String toString() {
        return "User{" +
                "name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", birthday=" + birthday +
                ", regDate=" + regDate +
                ", imageInfoDtoList=" + imageInfoDtoList +
                ", createdSpots=" + createdSpots +
                ", likedSpotIds=" + likedSpotIds +
                ", favoriteSpotIds=" + favoriteSpotIds +
                '}';
    }
}
