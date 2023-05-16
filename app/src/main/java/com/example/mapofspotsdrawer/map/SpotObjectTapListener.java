package com.example.mapofspotsdrawer.map;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;

import com.example.mapofspotsdrawer.R;
import com.example.mapofspotsdrawer.model.Spot;
import com.example.mapofspotsdrawer.ui.spot.SpotInfoFragment;
import com.yandex.mapkit.geometry.Point;
import com.yandex.mapkit.map.MapObject;
import com.yandex.mapkit.map.MapObjectTapListener;

public class SpotObjectTapListener implements MapObjectTapListener {
    private FragmentActivity activity;

    public void setActivity(FragmentActivity activity) {
        this.activity = activity;
    }

    @Override
    public boolean onMapObjectTap(@NonNull MapObject mapObject, @NonNull Point point) {
        Spot spot = (Spot) mapObject.getUserData();

        SpotInfoFragment spotInfoFragment = new SpotInfoFragment();

        Bundle bundle = new Bundle();
        bundle.putSerializable("spot", spot);

        spotInfoFragment.setArguments(bundle);

        hideAllViewsOnAllSpotsFragment(activity);
        activity.getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container_all_spots, spotInfoFragment)
                .commit();

        return true;
    }

    private void hideAllViewsOnAllSpotsFragment(FragmentActivity activity) {
        View mapView = activity.findViewById(R.id.mapview_all_spots);
        View progressBar = activity.findViewById(R.id.progressBar_all_spots);

        mapView.setVisibility(View.GONE);
        progressBar.setVisibility(View.GONE);
    }
}
