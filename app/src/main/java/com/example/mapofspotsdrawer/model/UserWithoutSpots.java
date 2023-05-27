package com.example.mapofspotsdrawer.model;

import androidx.annotation.NonNull;

import java.util.Date;
import java.util.List;

public class UserWithoutSpots {
    protected String name;

    protected String email;

    protected String phoneNumber;

    protected Date birthday;

    protected Date regDate;

    protected String password;

    protected List<ImageInfoDto> imageInfoDtoList;

    protected List<Long> likedSpotIds;

    protected List<Long> favoriteSpotIds;

    public UserWithoutSpots() {
    }

    public UserWithoutSpots(String name, String email, String phoneNumber,
                Date birthday, Date registrationDate, String password,
                List<ImageInfoDto> imageInfoDtoList, List<Long> likedSpotIds, List<Long> favoriteSpotIds) {
        this.name = name;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.birthday = birthday;
        this.regDate = registrationDate;
        this.password = password;
        this.imageInfoDtoList = imageInfoDtoList;
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

    @NonNull
    @Override
    public String toString() {
        return "User{" +
                "name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", birthday=" + birthday +
                ", regDate=" + regDate +
                ", imageInfoDtoList=" + imageInfoDtoList +
                ", likedSpotIds=" + likedSpotIds +
                ", favoriteSpotIds=" + favoriteSpotIds +
                '}';
    }
}
