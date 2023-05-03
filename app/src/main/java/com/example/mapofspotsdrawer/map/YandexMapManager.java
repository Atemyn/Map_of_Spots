package com.example.mapofspotsdrawer.map;

import com.example.mapofspotsdrawer.model.Placemark;
import com.yandex.mapkit.geometry.Point;

import java.util.List;

public class YandexMapManager implements IMapManager{
    private static final YandexMapManager instance = new YandexMapManager();

    private YandexMapManager() {
    }

    public YandexMapManager getInstance() {
        return instance;
    }

    @Override
    public void initMap(Point position, float zoom) {

    }

    @Override
    public void addPlacemarks(List<Placemark> placemarkInfos) {

    }
}
