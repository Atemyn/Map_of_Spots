package com.example.mapofspotsdrawer.map;

import android.content.Context;

import androidx.fragment.app.FragmentActivity;

import com.example.mapofspotsdrawer.model.Placemark;
import com.example.mapofspotsdrawer.model.Spot;
import com.yandex.mapkit.geometry.Point;

import java.util.List;

public interface IMapManager {
    void moveMapTo(Point position, float zoom);
    void addPlacemarks(List<Spot> spots, FragmentActivity activity);
}
