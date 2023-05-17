package com.example.mapofspotsdrawer.ui.create_spot;

import android.content.SharedPreferences;
import android.graphics.Rect;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.preference.PreferenceManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.mapofspotsdrawer.R;
import com.example.mapofspotsdrawer.databinding.FragmentCreateSpotInfoBinding;
import com.example.mapofspotsdrawer.map.YandexMapManager;
import com.example.mapofspotsdrawer.model.SpaceType;
import com.example.mapofspotsdrawer.model.SportType;
import com.example.mapofspotsdrawer.model.SpotType;
import com.example.mapofspotsdrawer.ui.utils.UIUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.yandex.mapkit.MapKitFactory;
import com.yandex.mapkit.geometry.Point;
import com.yandex.mapkit.map.InputListener;
import com.yandex.mapkit.map.Map;

import java.lang.reflect.Type;
import java.util.List;

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

        SharedPreferences sharedPreferences =
                PreferenceManager.getDefaultSharedPreferences(requireActivity());
        Gson gson = new Gson();

        // Получение содержимого таблиц-справочников.
        List<SpotType> spotTypes =
                getSpotTypesFromSharedPreferences(sharedPreferences, gson);
        List<SportType> sportTypes =
                getSportTypesFromSharedPreferences(sharedPreferences, gson);
        List<SpaceType> spaceTypes =
                getSpaceTypesFromSharedPreferences(sharedPreferences, gson);

        setSpotTypesMultipleChoiceListView(spotTypes);
        setSportTypesMultipleChoiceListView(sportTypes);
        setSpaceTypeListView(spaceTypes);

        return root;
    }

    private List<SpotType> getSpotTypesFromSharedPreferences(
            SharedPreferences sharedPreferences, Gson gson) {
        String spotTypesJson = sharedPreferences.getString("spot_types", "");
        Type type = new TypeToken<List<SpotType>>() {}.getType();
        return gson.fromJson(spotTypesJson, type);
    }

    private List<SportType> getSportTypesFromSharedPreferences(
            SharedPreferences sharedPreferences, Gson gson) {
        String sportTypesJson = sharedPreferences.getString("sport_types", "");
        Type type = new TypeToken<List<SportType>>() {}.getType();
        return gson.fromJson(sportTypesJson, type);
    }

    private List<SpaceType> getSpaceTypesFromSharedPreferences(
            SharedPreferences sharedPreferences, Gson gson) {
        String spaceTypesJson = sharedPreferences.getString("space_types", "");
        Type type = new TypeToken<List<SpaceType>>() {}.getType();
        return gson.fromJson(spaceTypesJson, type);
    }

    private void setSpotTypesMultipleChoiceListView(List<SpotType> spotTypes) {
        String[] spotTypesNames = new String[spotTypes.size()];
        for (int i = 0; i < spotTypes.size(); i++) {
            spotTypesNames[i] = spotTypes.get(i).getName();
        }

        ArrayAdapter<String> adapter =
                new ArrayAdapter<>(getContext(), R.layout.list_item_multiple_choice, spotTypesNames);
        binding.listviewSpotTypes.setAdapter(adapter);
        UIUtils.setListViewHeightBasedOnItems(binding.listviewSpotTypes);
        binding.listviewSpotTypes.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
    }

    private void setSportTypesMultipleChoiceListView(List<SportType> sportTypes) {
        String[] sportTypesNames = new String[sportTypes.size()];
        for (int i = 0; i < sportTypes.size(); i++) {
            sportTypesNames[i] = sportTypes.get(i).getName();
        }

        ArrayAdapter<String> adapter =
                new ArrayAdapter<>(getContext(), R.layout.list_item_multiple_choice, sportTypesNames);
        binding.listviewSportTypes.setAdapter(adapter);
        UIUtils.setListViewHeightBasedOnItems(binding.listviewSportTypes);
        binding.listviewSportTypes.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
    }

    private void setSpaceTypeListView(List<SpaceType> spaceTypes) {
        String[] spaceTypesNames = new String[spaceTypes.size()];
        for (int i = 0; i < spaceTypes.size(); i++) {
            spaceTypesNames[i] = spaceTypes.get(i).getName();
        }

        ArrayAdapter<String> adapter =
                new ArrayAdapter<>(getContext(), R.layout.list_item_multiple_choice, spaceTypesNames);
        binding.listviewSpaceType.setAdapter(adapter);
        UIUtils.setListViewHeightBasedOnItems(binding.listviewSpaceType);
        binding.listviewSpaceType.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
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