package com.example.mapofspotsdrawer.model;

import androidx.annotation.NonNull;

import com.yandex.mapkit.geometry.Point;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class Spot implements Serializable {
    private Long id;

    private String name;

    private Double latitude;

    private Double longitude;

    private Boolean accepted;

    private Date addingDate;

    private Date updatingDate;

    private String description;

    private List<ImageInfoDto> imageInfoDtoList;

    private Integer likeNumber;

    private Integer favoriteNumber;

    private List<Integer> spotTypeIds;

    private List<Integer> sportTypeIds;

    private Integer spaceTypeId;

    public Spot() {
    }

    public Spot(Long id, String name, Double latitude, Double longitude,
                Boolean accepted, Date addingDate, Date updatingDate,
                String description, List<ImageInfoDto> imageInfoDtoList,
                Integer likeNumber, Integer favoriteNumber,
                List<Integer> spotTypeIds, List<Integer> sportTypeIds, Integer spaceTypeId) {
        this.id = id;
        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;
        this.accepted = accepted;
        this.addingDate = addingDate;
        this.updatingDate = updatingDate;
        this.description = description;
        this.imageInfoDtoList = imageInfoDtoList;
        this.likeNumber = likeNumber;
        this.favoriteNumber = favoriteNumber;
        this.spotTypeIds = spotTypeIds;
        this.sportTypeIds = sportTypeIds;
        this.spaceTypeId = spaceTypeId;
    }

    public Point getPosition() {
        return new Point(latitude, longitude);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public Boolean getAccepted() {
        return accepted;
    }

    public void setAccepted(Boolean accepted) {
        this.accepted = accepted;
    }

    public Date getAddingDate() {
        return addingDate;
    }

    public void setAddingDate(Date addingDate) {
        this.addingDate = addingDate;
    }

    public Date getUpdatingDate() {
        return updatingDate;
    }

    public void setUpdatingDate(Date updatingDate) {
        this.updatingDate = updatingDate;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<ImageInfoDto> getImageInfoDtoList() {
        return imageInfoDtoList;
    }

    public void setImageInfoDtoList(List<ImageInfoDto> imageInfoDtoList) {
        this.imageInfoDtoList = imageInfoDtoList;
    }

    public Integer getLikeNumber() {
        return likeNumber;
    }

    public void setLikeNumber(Integer likeNumber) {
        this.likeNumber = likeNumber;
    }

    public Integer getFavoriteNumber() {
        return favoriteNumber;
    }

    public void setFavoriteNumber(Integer favoriteNumber) {
        this.favoriteNumber = favoriteNumber;
    }

    public List<Integer> getSpotTypeIds() {
        return spotTypeIds;
    }

    public void setSpotTypeIds(List<Integer> spotTypeIds) {
        this.spotTypeIds = spotTypeIds;
    }

    public List<Integer> getSportTypeIds() {
        return sportTypeIds;
    }

    public void setSportTypeIds(List<Integer> sportTypeIds) {
        this.sportTypeIds = sportTypeIds;
    }

    public Integer getSpaceTypeId() {
        return spaceTypeId;
    }

    public void setSpaceTypeId(Integer spaceTypeId) {
        this.spaceTypeId = spaceTypeId;
    }

    @NonNull
    @Override
    public String toString() {
        return "Spot{" +
                "name='" + name + '\'' +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                ", accepted=" + accepted +
                ", addingDate=" + addingDate +
                ", updatingDate=" + updatingDate +
                ", description='" + description + '\'' +
                ", imageInfoDtoList=" + imageInfoDtoList +
                ", likeNumber=" + likeNumber +
                ", favoriteNumber=" + favoriteNumber +
                ", spotTypeIds=" + spotTypeIds +
                ", sportTypeIds=" + sportTypeIds +
                ", spaceTypeId=" + spaceTypeId +
                '}';
    }
}
