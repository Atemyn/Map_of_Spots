package com.example.mapofspotsdrawer.map;

import com.example.mapofspotsdrawer.model.Placemark;
import com.yandex.mapkit.Animation;
import com.yandex.mapkit.MapKitFactory;
import com.yandex.mapkit.geometry.Point;
import com.yandex.mapkit.map.CameraPosition;
import com.yandex.mapkit.map.PlacemarkMapObject;
import com.yandex.mapkit.mapview.MapView;

import java.util.List;

public class YandexMapManager implements IMapManager{
    private static final YandexMapManager instance = new YandexMapManager();
    private MapView mapView = null;

    private String apiKey = null;

    private YandexMapManager() {
    }

    public void setMapView(MapView mapView) {
        this.mapView = mapView;
        mapView.getMap().setRotateGesturesEnabled(true);
    }

    public void setApiKey(String apiKey) {
        if (this.apiKey == null) {
            this.apiKey = apiKey;
            MapKitFactory.setApiKey(apiKey);
        }
    }

    public static YandexMapManager getInstance() {
        return instance;
    }

    @Override
    public void moveMapTo(Point position, float zoom) {
        mapView.getMap().move(
                new CameraPosition(position, zoom, 0.0f, 0.0f),
                new Animation(Animation.Type.SMOOTH, 1),
                null);
    }

    private void addPlacemark(Placemark placemarkInfo) {
        PlacemarkMapObject placemark =
                mapView.getMap().getMapObjects().addPlacemark(placemarkInfo.getPosition());
        placemark.setText(placemarkInfo.getLabelText());
    }

    @Override
    public void addPlacemarks(List<Placemark> placemarkInfos) {
        for (Placemark placemark : placemarkInfos) {
            addPlacemark(placemark);
        }
    }
}
