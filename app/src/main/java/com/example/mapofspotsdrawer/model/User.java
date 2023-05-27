package com.example.mapofspotsdrawer.model;

import androidx.annotation.NonNull;

import java.util.Date;
import java.util.List;

public class User extends UserWithoutSpots {

    private List<Spot> createdSpots;

    public User() {
    }

    public User(String name, String email, String phoneNumber,
                Date birthday, Date registrationDate, String password,
                List<ImageInfoDto> imageInfoDtoList,
                List<Spot> createdSpots, List<Long> likedSpotIds, List<Long> favoriteSpotIds) {
        super(name, email, phoneNumber, birthday,
                registrationDate, password, imageInfoDtoList,
                likedSpotIds, favoriteSpotIds);
        this.createdSpots = createdSpots;
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
                ", createdSpots=" + createdSpots +
                ", likedSpotIds=" + likedSpotIds +
                ", favoriteSpotIds=" + favoriteSpotIds +
                '}';
    }
}
