package com.example.mapofspotsdrawer.map;

import android.content.Context;

import com.example.mapofspotsdrawer.model.Placemark;
import com.yandex.mapkit.geometry.Point;

import java.util.List;

public interface IMapManager {
    void moveMapTo(Point position, float zoom);

    void addPlacemark(Placemark placemarkInfo, Context context);
    void addPlacemarks(List<Placemark> placemarkInfos, Context context);
}
