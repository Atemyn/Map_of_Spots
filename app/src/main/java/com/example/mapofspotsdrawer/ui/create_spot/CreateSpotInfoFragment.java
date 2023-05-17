package com.example.mapofspotsdrawer.ui.create_spot;

import android.graphics.Rect;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.mapofspotsdrawer.R;
import com.example.mapofspotsdrawer.databinding.FragmentCreateSpotInfoBinding;
import com.example.mapofspotsdrawer.map.YandexMapManager;
import com.example.mapofspotsdrawer.ui.utils.UIUtils;
import com.yandex.mapkit.MapKitFactory;
import com.yandex.mapkit.geometry.Point;
import com.yandex.mapkit.map.InputListener;
import com.yandex.mapkit.map.Map;

public class CreateSpotInfoFragment extends Fragment {

    private FragmentCreateSpotInfoBinding binding;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MapKitFactory.initialize(this.requireContext());
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        binding = FragmentCreateSpotInfoBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        YandexMapManager.getInstance().setMapView(binding.mapviewCreateSpot);

        setSpotTypesMultipleChoiceListView();
        setSportTypesMultipleChoiceListView();

        return root;
    }

    private void setSpotTypesMultipleChoiceListView() {
        String[] items = {"SpotType 1", "SpotType 2", "SpotType 3"};
        ArrayAdapter<String> adapter =
                new ArrayAdapter<>(getContext(), R.layout.list_item_multiple_choice, items);
        binding.listviewSpotTypes.setAdapter(adapter);
        UIUtils.setListViewHeightBasedOnItems(binding.listviewSpotTypes);
        binding.listviewSpotTypes.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
    }

    private void setSportTypesMultipleChoiceListView() {
        String[] items = {"Sport 1", "Sport 2", "Sport 3", "Sport 4", "Sport 5"};
        ArrayAdapter<String> adapter =
                new ArrayAdapter<>(getContext(), R.layout.list_item_multiple_choice, items);
        binding.listviewSportTypes.setAdapter(adapter);
        UIUtils.setListViewHeightBasedOnItems(binding.listviewSportTypes);
        binding.listviewSportTypes.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
    }

    @Override
    public void onStop() {
        binding.mapviewCreateSpot.onStop();
        MapKitFactory.getInstance().onStop();
        super.onStop();
    }

    @Override
    public void onStart() {
        super.onStart();
        MapKitFactory.getInstance().onStart();
        binding.mapviewCreateSpot.onStart();
        YandexMapManager mapManager = YandexMapManager.getInstance();

        binding.mapviewCreateSpot.getMap().addInputListener(new InputListener() {
            @Override
            public void onMapTap(@NonNull Map map, @NonNull Point point) {
                mapManager.setMapObject(point, requireActivity());
            }

            @Override
            public void onMapLongTap(@NonNull Map map, @NonNull Point point) {

            }
        });

        mapManager.moveMapTo(new Point(55.751574, 37.573856), 5.0f);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}