package com.example.mapofspotsdrawer.ui.all_spots;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.mapofspotsdrawer.databinding.FragmentAllSpotsBinding;
import com.example.mapofspotsdrawer.map.YandexMapManager;
import com.yandex.mapkit.MapKitFactory;
import com.yandex.mapkit.geometry.Point;

public class AllSpotsFragment extends Fragment {

    private FragmentAllSpotsBinding binding;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MapKitFactory.initialize(this.requireContext());
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        AllSpotsViewModel allSpotsViewModel =
                new ViewModelProvider(this).get(AllSpotsViewModel.class);

        binding = FragmentAllSpotsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        YandexMapManager.getInstance().setMapView(binding.mapview);
        return root;
    }

    @Override
    public void onStop() {
        binding.mapview.onStop();
        MapKitFactory.getInstance().onStop();
        super.onStop();
    }

    @Override
    public void onStart() {
        super.onStart();
        MapKitFactory.getInstance().onStart();
        binding.mapview.onStart();
        YandexMapManager mapManager = YandexMapManager.getInstance();
        // TODO Переделать, чтобы состояние сохранялось при повороте экрана.
        mapManager.moveMapTo(new Point(55.751574, 80.573856), 2.0f);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}