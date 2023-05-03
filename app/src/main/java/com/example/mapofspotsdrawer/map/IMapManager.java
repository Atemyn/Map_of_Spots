package com.example.mapofspotsdrawer.map;

import com.example.mapofspotsdrawer.model.Placemark;
import com.yandex.mapkit.geometry.Point;

import java.util.List;

public interface IMapManager {
    void moveMapTo(Point position, float zoom);
    void addPlacemarks(List<Placemark> placemarkInfos);
}
