package com.example.mapofspotsdrawer.ui.spot;

import androidx.lifecycle.ViewModel;

import com.example.mapofspotsdrawer.model.ImageInfoDto;

import java.util.List;

public class SpotInfoViewModel extends ViewModel {
    private String name;

    private String addingDateText;

    private String updatingDateText;

    private String description;

    private List<String> imagesUrls;

    private String likeNumber;

    private String favoriteNumber;

    private List<String> spotTypeNames;

    private List<String> sportTypeNames;

    private String spaceTypeName;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddingDateText() {
        return addingDateText;
    }

    public void setAddingDateText(String addingDateText) {
        this.addingDateText = addingDateText;
    }

    public String getUpdatingDateText() {
        return updatingDateText;
    }

    public void setUpdatingDateText(String updatingDateText) {
        this.updatingDateText = updatingDateText;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<String> getImagesUrls() {
        return imagesUrls;
    }

    public void setImagesUrls(List<String> imagesUrls) {
        this.imagesUrls = imagesUrls;
    }

    public String getLikeNumber() {
        return likeNumber;
    }

    public void setLikeNumber(String likeNumber) {
        this.likeNumber = likeNumber;
    }

    public String getFavoriteNumber() {
        return favoriteNumber;
    }

    public void setFavoriteNumber(String favoriteNumber) {
        this.favoriteNumber = favoriteNumber;
    }

    public List<String> getSpotTypeNames() {
        return spotTypeNames;
    }

    public void setSpotTypeNames(List<String> spotTypeNames) {
        this.spotTypeNames = spotTypeNames;
    }

    public List<String> getSportTypeNames() {
        return sportTypeNames;
    }

    public void setSportTypeNames(List<String> sportTypeNames) {
        this.sportTypeNames = sportTypeNames;
    }

    public String getSpaceTypeName() {
        return spaceTypeName;
    }

    public void setSpaceTypeName(String spaceTypeName) {
        this.spaceTypeName = spaceTypeName;
    }
}