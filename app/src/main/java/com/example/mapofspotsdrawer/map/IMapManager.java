package com.example.mapofspotsdrawer.map;


import androidx.appcompat.app.AppCompatActivity;

import com.example.mapofspotsdrawer.model.Spot;
import com.yandex.mapkit.geometry.Point;

import java.util.List;

public interface IMapManager {
    void moveMapTo(Point position, float zoom);
    void addPlacemarks(List<Spot> spots, AppCompatActivity activity, int fragmentContainerId);
    void clearMap();
}
