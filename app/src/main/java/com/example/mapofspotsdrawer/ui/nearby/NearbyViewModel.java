package com.example.mapofspotsdrawer.ui.nearby;

import androidx.lifecycle.ViewModel;

import com.example.mapofspotsdrawer.model.Spot;

import java.util.List;

public class NearbyViewModel extends ViewModel {
    private List<Spot> spots;

    public List<Spot> getSpots() {
        return spots;
    }

    public void setSpots(List<Spot> spots) {
        this.spots = spots;
    }
}