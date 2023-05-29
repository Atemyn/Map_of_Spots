package com.example.mapofspotsdrawer.map;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;

import com.example.mapofspotsdrawer.ui.create_spot.validation.AllFieldsValidator;
import com.yandex.mapkit.geometry.Point;
import com.yandex.mapkit.map.InputListener;
import com.yandex.mapkit.map.Map;

public class PlacemarkInputListener implements InputListener {
    private final AllFieldsValidator allFieldsValidator;

    private final FragmentActivity fragmentActivity;

    public PlacemarkInputListener(AllFieldsValidator allFieldsValidator, FragmentActivity fragmentActivity) {
        this.allFieldsValidator = allFieldsValidator;
        this.fragmentActivity = fragmentActivity;
    }

    @Override
    public void onMapTap(@NonNull Map map, @NonNull Point point) {
        YandexMapManager.getInstance().setMapObject(point, fragmentActivity);
        allFieldsValidator.validateFields();
    }

    @Override
    public void onMapLongTap(@NonNull Map map, @NonNull Point point) {

    }
}
