package com.example.mapofspotsdrawer.ui.favorite;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.mapofspotsdrawer.databinding.FragmentFavoriteBinding;
import com.example.mapofspotsdrawer.map.YandexMapManager;
import com.yandex.mapkit.MapKitFactory;
import com.yandex.mapkit.geometry.Point;

public class FavoriteFragment extends Fragment {

    private FragmentFavoriteBinding binding;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MapKitFactory.initialize(this.requireContext());
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        FavoriteViewModel favoriteViewModel =
                new ViewModelProvider(this).get(FavoriteViewModel.class);

        binding = FragmentFavoriteBinding.inflate(inflater, container, false);
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
        mapManager.moveMapTo(new Point(55.751574, 37.573856), 5.0f);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}