package com.example.mapofspotsdrawer.map;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;
import androidx.preference.PreferenceManager;

import com.example.mapofspotsdrawer.R;
import com.example.mapofspotsdrawer.model.Spot;

import com.yandex.mapkit.Animation;
import com.yandex.mapkit.MapKitFactory;
import com.yandex.mapkit.geometry.Point;
import com.yandex.mapkit.map.CameraPosition;
import com.yandex.mapkit.map.PlacemarkMapObject;
import com.yandex.mapkit.mapview.MapView;
import com.yandex.runtime.image.ImageProvider;

import java.util.List;

public class YandexMapManager implements IMapManager{
    private static final YandexMapManager instance = new YandexMapManager();
    private MapView mapView = null;

    private String apiKey = null;

    private PlacemarkMapObject singleMapObject;

    private final SpotObjectTapListener listener = new SpotObjectTapListener();

    private YandexMapManager() {
    }

    public void setSingleMapObject(PlacemarkMapObject singleMapObject) {
        this.singleMapObject = singleMapObject;
    }

    public void setMapView(MapView mapView, FragmentActivity activity) {
        this.mapView = mapView;
        mapView.getMap().set2DMode(false);
        mapView.getMap().setRotateGesturesEnabled(false);
        mapView.getMap().setZoomGesturesEnabled(true);

        SharedPreferences sharedPreferences
                = PreferenceManager.getDefaultSharedPreferences(activity);
        boolean nightVisionEnabled = sharedPreferences.getBoolean("night_vision", false);
        mapView.getMap().setNightModeEnabled(nightVisionEnabled);
    }

    public MapView getMapView() {
        return mapView;
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

    public void setMapObject(Point point, Context context) {
        if (this.singleMapObject == null) {
            this.singleMapObject
                    = mapView.getMap().getMapObjects().addPlacemark(point,
                    ImageProvider.fromBitmap(
                            getBitmapFromVectorDrawable(context, R.drawable.ic_spot_placemark)));
        }
        else {
            singleMapObject.setGeometry(point);
        }
    }

    public PlacemarkMapObject getSingleMapObject() {
        return singleMapObject;
    }

    private Bitmap getBitmapFromVectorDrawable(Context context, int drawableId) {
        Drawable drawable = ContextCompat.getDrawable(context, drawableId);

        assert drawable != null;
        Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(),
                drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);

        return bitmap;
    }

    private void addPlacemark(Spot spot, AppCompatActivity activity) {
        PlacemarkMapObject placemark =
                mapView.getMap().getMapObjects().addPlacemark(
                        spot.getPosition(), ImageProvider.fromBitmap(
                                getBitmapFromVectorDrawable(activity,
                                        R.drawable.ic_spot_placemark)));
        placemark.setText(spot.getName());
        placemark.setUserData(spot);
        placemark.addTapListener(listener);
    }

    @Override
    public void addPlacemarks(List<Spot> spots,
                              AppCompatActivity activity, int fragmentContainerId) {
        listener.setActivity(activity);
        listener.setFragmentContainerId(fragmentContainerId);

        for (Spot spot : spots) {
            addPlacemark(spot, activity);
        }
    }
}
