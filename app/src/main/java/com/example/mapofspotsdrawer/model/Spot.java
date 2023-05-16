package com.example.mapofspotsdrawer.model;

import androidx.annotation.NonNull;

import java.util.Date;
import java.util.List;

public class Spot {
    private String name;

    private Double latitude;

    private Double longitude;

    private Boolean accepted;

    private Date addingDate;

    private Date updatingDate;

    private String description;

    private List<ImageInfoDto> imageInfoDtoList;

    private List<Integer> spotTypeIds;

    private List<Integer> sportTypeIds;

    private Integer spaceTypeId;

    public Spot() {
    }

    public Spot(String name, Double latitude, Double longitude,
                Boolean accepted, Date addingDate, Date updatingDate,
                String description, List<ImageInfoDto> imageInfoDtoList,
                List<Integer> spotTypeIds, List<Integer> sportTypeIds, Integer spaceTypeId) {
        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;
        this.accepted = accepted;
        this.addingDate = addingDate;
        this.updatingDate = updatingDate;
        this.description = description;
        this.imageInfoDtoList = imageInfoDtoList;
        this.spotTypeIds = spotTypeIds;
        this.sportTypeIds = sportTypeIds;
        this.spaceTypeId = spaceTypeId;
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
                ", spotTypeIds=" + spotTypeIds +
                ", sportTypeIds=" + sportTypeIds +
                ", spaceTypeId=" + spaceTypeId +
                '}';
    }
}
