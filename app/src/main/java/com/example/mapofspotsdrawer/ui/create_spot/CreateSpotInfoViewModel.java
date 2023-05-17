package com.example.mapofspotsdrawer.ui.create_spot;

import android.net.Uri;

import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.List;

public class CreateSpotInfoViewModel extends ViewModel {
    private List<String> imagesUrls = new ArrayList<>();

    public void addImageUri(String uri) {
        imagesUrls.add(uri);
    }

    public List<String> getImagesUrls() {
        return imagesUrls;
    }

    public void setImagesUrls(List<String> imagesUrls) {
        this.imagesUrls = imagesUrls;
    }
}