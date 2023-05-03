package com.example.mapofspotsdrawer.map;

import com.example.mapofspotsdrawer.model.Placemark;
import com.yandex.mapkit.Animation;
import com.yandex.mapkit.geometry.Point;
import com.yandex.mapkit.map.CameraPosition;
import com.yandex.mapkit.mapview.MapView;

import java.util.List;

public class YandexMapManager implements IMapManager{
    private static final YandexMapManager instance = new YandexMapManager();
    private MapView mapView = null;

    private YandexMapManager() {
    }

    public void setMapView(MapView mapView) {
        this.mapView = mapView;
    }

    public YandexMapManager getInstance() {
        return instance;
    }

    @Override
    public void initMap(Point position, float zoom) {
        mapView.getMap().move(
                new CameraPosition(position, zoom, 0.0f, 0.0f),
                new Animation(Animation.Type.SMOOTH, 1),
                null);
        mapView.getMap().setRotateGesturesEnabled(true);
    }

    @Override
    public void addPlacemarks(List<Placemark> placemarkInfos) {

    }
}
