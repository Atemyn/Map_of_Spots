package com.example.mapofspotsdrawer.ui.create_spot;

import androidx.lifecycle.ViewModel;

import com.yandex.mapkit.geometry.Point;
import com.yandex.mapkit.map.PlacemarkMapObject;

import java.util.ArrayList;
import java.util.List;

public class CreateSpotInfoViewModel extends ViewModel {
    private List<String> imagesUrls = new ArrayList<>();

    public void addImageUri(String uri) {
        imagesUrls.add(uri);
    }

    public void removeImageUriAt(int index) {
        imagesUrls.remove(index);
    }


    public List<String> getImagesUrls() {
        return imagesUrls;
    }

    public void setImagesUrls(List<String> imagesUrls) {
        this.imagesUrls = imagesUrls;
    }

}