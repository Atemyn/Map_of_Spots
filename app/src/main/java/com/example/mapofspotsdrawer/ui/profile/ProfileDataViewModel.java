package com.example.mapofspotsdrawer.ui.profile;

import androidx.lifecycle.ViewModel;

import com.example.mapofspotsdrawer.model.ImageUrl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProfileDataViewModel extends ViewModel {
    private String name;
    private String email;
    private String phone;
    private String birthday;
    private String registrationDate;

    private List<ImageUrl> imagesUrls = new ArrayList<>();

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

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getRegistrationDate() {
        return registrationDate;
    }

    public void setRegistrationDate(String registrationDate) {
        this.registrationDate = registrationDate;
    }

    public void addImageUri(Long id, String uri, boolean isFromServer) {
        imagesUrls.add(new ImageUrl(id, uri, isFromServer));
    }

    public void removeImageUriAt(int index) {
        imagesUrls.remove(index);
    }


    public List<ImageUrl> getImagesUrls() {
        return imagesUrls;
    }

    public void setImagesUrls(List<ImageUrl> imagesUrls) {
        this.imagesUrls = imagesUrls;
    }
}