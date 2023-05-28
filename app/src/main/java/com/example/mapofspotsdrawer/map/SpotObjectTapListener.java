package com.example.mapofspotsdrawer.map;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.mapofspotsdrawer.R;
import com.example.mapofspotsdrawer.model.Spot;
import com.example.mapofspotsdrawer.ui.spot.SpotInfoFragment;
import com.yandex.mapkit.geometry.Point;
import com.yandex.mapkit.map.MapObject;
import com.yandex.mapkit.map.MapObjectTapListener;

public class SpotObjectTapListener implements MapObjectTapListener {
    private AppCompatActivity activity;

    private int fragmentContainerId;

    public void setActivity(AppCompatActivity activity) {
        this.activity = activity;
    }

    public void setFragmentContainerId(int fragmentContainerId) {
        this.fragmentContainerId = fragmentContainerId;
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
                .add(fragmentContainerId, spotInfoFragment)
                .commit();

        return true;
    }

    private void hideAllViewsOnAllSpotsFragment(AppCompatActivity activity) {
        View mapView = null, progressBar = null;

        if (fragmentContainerId == R.id.fragment_container_all_spots) {
            mapView = activity.findViewById(R.id.mapview_all_spots);
            progressBar = activity.findViewById(R.id.progressBar_all_spots);
        }
        else if (fragmentContainerId == R.id.fragment_container_favorite) {
            mapView = activity.findViewById(R.id.mapview_favorite_spots);
            progressBar = activity.findViewById(R.id.progressBar_favorite_spots);
        }
        else if (fragmentContainerId == R.id.fragment_container_nearby_spots) {
            mapView = activity.findViewById(R.id.mapview_nearby_spots);
            progressBar = activity.findViewById(R.id.progressBar_nearby_spots);
        }

        if (mapView != null && progressBar != null) {
            mapView.setVisibility(View.GONE);
            progressBar.setVisibility(View.GONE);
        }
    }
}
