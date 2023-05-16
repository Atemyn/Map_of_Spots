package com.example.mapofspotsdrawer.ui.spot;

import androidx.lifecycle.ViewModel;

import com.example.mapofspotsdrawer.model.ImageInfoDto;

import java.util.Date;
import java.util.List;

public class SpotInfoViewModel extends ViewModel {
    private String name;

    private Date addingDate;

    private Date updatingDate;

    private String description;

    private List<ImageInfoDto> imageInfoDtoList;

    private List<String> spotTypeNames;

    private List<String> sportTypeNames;

    private String spaceTypeName;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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