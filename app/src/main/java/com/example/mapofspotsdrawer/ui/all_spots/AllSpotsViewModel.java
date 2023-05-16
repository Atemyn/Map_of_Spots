package com.example.mapofspotsdrawer.ui.all_spots;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.mapofspotsdrawer.model.Spot;

import java.util.List;

public class AllSpotsViewModel extends ViewModel {

    private List<Spot> spots;

    public List<Spot> getSpots() {
        return spots;
    }

    public void setSpots(List<Spot> spots) {
        this.spots = spots;
    }
}