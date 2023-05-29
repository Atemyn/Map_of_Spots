package com.example.mapofspotsdrawer.ui.create_spot;

import androidx.lifecycle.ViewModel;

import com.yandex.mapkit.geometry.Point;

import java.util.ArrayList;
import java.util.List;

public class CreateSpotInfoViewModel extends ViewModel {
    private List<String> imagesUrls = new ArrayList<>();

    private Point placemarkPosition;

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

    public Point getPlacemarkPosition() {
        return placemarkPosition;
    }

    public void setPlacemarkPosition(Point placemarkPosition) {
        this.placemarkPosition = placemarkPosition;
    }
}