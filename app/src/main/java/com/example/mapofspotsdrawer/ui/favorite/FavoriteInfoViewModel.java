package com.example.mapofspotsdrawer.ui.favorite;

import androidx.lifecycle.ViewModel;

import com.example.mapofspotsdrawer.model.Spot;

import java.util.List;

public class FavoriteInfoViewModel extends ViewModel {

    private boolean isViewCreated = false;

    private List<Spot> spots;

    public boolean isViewCreated() {
        return isViewCreated;
    }

    public void setViewCreated(boolean viewCreated) {
        isViewCreated = viewCreated;
    }
    public List<Spot> getSpots() {
        return spots;
    }

    public void setSpots(List<Spot> spots) {
        this.spots = spots;
    }
}