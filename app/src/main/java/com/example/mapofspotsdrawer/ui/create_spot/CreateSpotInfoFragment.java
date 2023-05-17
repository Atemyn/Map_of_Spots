package com.example.mapofspotsdrawer.ui.create_spot;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.example.mapofspotsdrawer.R;
import com.example.mapofspotsdrawer.databinding.FragmentCreateSpotInfoBinding;
import com.example.mapofspotsdrawer.map.YandexMapManager;
import com.example.mapofspotsdrawer.model.SpaceType;
import com.example.mapofspotsdrawer.model.SportType;
import com.example.mapofspotsdrawer.model.SpotType;
import com.example.mapofspotsdrawer.ui.adapter.ImageSliderAdapter;
import com.example.mapofspotsdrawer.ui.utils.UIUtils;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.yandex.mapkit.MapKitFactory;
import com.yandex.mapkit.geometry.Point;
import com.yandex.mapkit.map.InputListener;
import com.yandex.mapkit.map.Map;
import com.yandex.mapkit.mapview.MapView;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class CreateSpotInfoFragment extends Fragment {

    private FragmentCreateSpotInfoBinding binding;

    private CreateSpotInfoViewModel viewModel;

    private ActivityResultLauncher<Intent> imagePickerLauncher;

    private boolean noImage = true;

    private MapView mapView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MapKitFactory.initialize(this.requireContext());
        viewModel = new ViewModelProvider(this).get(CreateSpotInfoViewModel.class);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        binding = FragmentCreateSpotInfoBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        mapView = binding.mapviewCreateSpot;

        YandexMapManager.getInstance().setMapView(mapView);

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

        binding.btnOpenFullscreen.setTag(R.drawable.ic_open_fullscreen);
        binding.btnOpenFullscreen.setOnClickListener(view -> {
            if ((int) binding.btnOpenFullscreen.getTag()
                    == R.drawable.ic_open_fullscreen) {
                binding.btnOpenFullscreen.setImageResource(R.drawable.ic_close_fullscreen);
                binding.btnOpenFullscreen.setTag(R.drawable.ic_close_fullscreen);
                ViewGroup.LayoutParams layoutParams = mapView.getLayoutParams();
                layoutParams.height = transformDpUnitsInPx(250);
                mapView.setLayoutParams(layoutParams);
            }
            else {
                binding.btnOpenFullscreen.setImageResource(R.drawable.ic_open_fullscreen);
                binding.btnOpenFullscreen.setTag(R.drawable.ic_open_fullscreen);
                ViewGroup.LayoutParams layoutParams = mapView.getLayoutParams();
                layoutParams.height = 0;
                mapView.setLayoutParams(layoutParams);
            }
        });

        if (viewModel.getImagesUrls() == null || viewModel.getImagesUrls().size() == 0) {
            viewModel.setImagesUrls(new ArrayList<>());
            viewModel.addImageUri(getString(R.string.no_image_url));

            ImageSliderAdapter adapter =
                    new ImageSliderAdapter(getContext(), viewModel.getImagesUrls());
            binding.imageSliderCreateSpot.setAdapter(adapter);
        }

        imagePickerLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    int resultCode = result.getResultCode();
                    if (resultCode == Activity.RESULT_OK) {
                        assert result.getData() != null;
                        if (noImage) {
                            viewModel.setImagesUrls(new ArrayList<>());
                            noImage = false;
                        }
                        viewModel.addImageUri(result.getData().getData().toString());

                        ImageSliderAdapter adapter =
                                new ImageSliderAdapter(getContext(), viewModel.getImagesUrls());
                        binding.imageSliderCreateSpot.setAdapter(adapter);

                        setAdapterAndIndicatorConfigs(adapter);

                    } else if (resultCode == ImagePicker.RESULT_ERROR) {
                        Toast.makeText(requireActivity(),
                                ImagePicker.getError(result.getData()), Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(requireActivity(),
                                "Добавление фотографии отменено", Toast.LENGTH_LONG).show();
                    }
                });

        binding.btnAddSpotImage.setOnClickListener(
                view -> ImagePicker.Companion.with(requireActivity())
                .crop()
                .compress(1024)
                .maxResultSize(1080, 1080)
                .createIntent(intent -> {
                    imagePickerLauncher.launch(intent);
                    return null;
                }));

        binding.btnDeleteSpotImage.setOnClickListener(view -> {
            if (!noImage) {
                if (viewModel.getImagesUrls().size() <= 1) {
                    viewModel.setImagesUrls(new ArrayList<>());
                    viewModel.addImageUri(getString(R.string.no_image_url));
                    noImage = true;
                }
                else {
                    viewModel.removeImageUriAt(binding.imageSliderCreateSpot.getCurrentItem());
                }

                ImageSliderAdapter adapter =
                        new ImageSliderAdapter(getContext(), viewModel.getImagesUrls());
                binding.imageSliderCreateSpot.setAdapter(adapter);

                if (!noImage) {
                    setAdapterAndIndicatorConfigs(adapter);
                }
            }
            else {
                Toast.makeText(requireActivity(),
                        "Фотографий нет: удаление невозможно", Toast.LENGTH_LONG).show();
            }
        });

        return root;
    }

    private void setAdapterAndIndicatorConfigs(ImageSliderAdapter adapter) {
        binding.indicatorCreateSpot.setViewPager(binding.imageSliderCreateSpot);
        adapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onChanged() {
                super.onChanged();
                binding.indicatorCreateSpot.setViewPager(binding.imageSliderCreateSpot);
                adapter.setCurrentIndex(binding.imageSliderCreateSpot.getCurrentItem());
            }
        });
        binding.imageSliderCreateSpot.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                adapter.setCurrentIndex(position);
            }
        });

        adapter.setCurrentIndex(binding.imageSliderCreateSpot.getCurrentItem());
    }

    private int transformDpUnitsInPx(int unitsInPx) {
        return  (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP, unitsInPx, getResources().getDisplayMetrics());
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
        mapView.onStop();
        MapKitFactory.getInstance().onStop();
        super.onStop();
    }

    @Override
    public void onStart() {
        super.onStart();
        MapKitFactory.getInstance().onStart();
        mapView.onStart();
        YandexMapManager mapManager = YandexMapManager.getInstance();

        mapView.getMap().addInputListener(new InputListener() {
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