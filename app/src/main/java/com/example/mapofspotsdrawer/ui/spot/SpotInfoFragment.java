package com.example.mapofspotsdrawer.ui.spot;

import androidx.lifecycle.ViewModelProvider;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.example.mapofspotsdrawer.R;
import com.example.mapofspotsdrawer.databinding.FragmentSpotInfoBinding;
import com.example.mapofspotsdrawer.model.SpaceType;
import com.example.mapofspotsdrawer.model.SportType;
import com.example.mapofspotsdrawer.model.Spot;
import com.example.mapofspotsdrawer.model.SpotType;
import com.example.mapofspotsdrawer.ui.adapter.ImageSliderAdapter;
import com.example.mapofspotsdrawer.ui.utils.UIUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class SpotInfoFragment extends Fragment {

    private FragmentSpotInfoBinding binding;

    private SpotInfoViewModel spotInfoViewModel;

    private int[] images =
            {R.drawable.test_image_1, R.drawable.test_image_2,
                    R.drawable.test_image_3, R.drawable.test_image_4};

    public static SpotInfoFragment newInstance() {
        return new SpotInfoFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        spotInfoViewModel
                = new ViewModelProvider(this).get(SpotInfoViewModel.class);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentSpotInfoBinding.inflate(inflater, container, false);

        ImageSliderAdapter imageSliderAdapter = new ImageSliderAdapter(getContext(), images);
        binding.imageSlider.setAdapter(imageSliderAdapter);

        binding.indicator.setViewPager(binding.imageSlider);

        imageSliderAdapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onChanged() {
                super.onChanged();
                binding.indicator.setViewPager(binding.imageSlider);
                imageSliderAdapter.setCurrentIndex(binding.imageSlider.getCurrentItem());
            }
        });

        binding.imageSlider.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                imageSliderAdapter.setCurrentIndex(position);
            }
        });

        imageSliderAdapter.setCurrentIndex(binding.imageSlider.getCurrentItem());

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

        Spot spot = getSpotFromArguments();

        setNameTextView(spot);
        setDescriptionTextView(spot);
        setSpotTypesListView(spot, spotTypes);

        return binding.getRoot();
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


    private Spot getSpotFromArguments() {
        Bundle args = getArguments();
        if (args != null) {
            return  (Spot) args.getSerializable("spot");
        }
        return null;
    }

    private void setNameTextView(Spot spot) {
        String name = spotInfoViewModel.getName();
        if (name != null && !name.isEmpty()) {
            binding.tvName.setText(name);
        }
        else if (spot != null &&
                spot.getName() != null && !spot.getName().isEmpty()) {
            spotInfoViewModel.setName(spot.getName());
            binding.tvName.setText(spot.getName());
        }
    }

    private void setDescriptionTextView(Spot spot) {
        String description = spotInfoViewModel.getDescription();
        if (description != null && !description.isEmpty()) {
            binding.tvDescription.setText(description);
        }
        else if (spot != null &&
                spot.getDescription() != null && !spot.getDescription().isEmpty()) {
            spotInfoViewModel.setDescription(spot.getDescription());
            binding.tvDescription.setText(spot.getDescription());
        }
    }

    private void setSpotTypesListView(Spot spot, List<SpotType> spotTypes) {
        List<String> spotTypeNames = spotInfoViewModel.getSpotTypeNames();
        if (spotTypeNames != null && !spotTypeNames.isEmpty()) {
            ArrayAdapter<String> adapter =
                    new ArrayAdapter<>(getContext(),
                            R.layout.list_view_item, spotTypeNames);
            binding.lvSpotTypes.setAdapter(adapter);
            UIUtils.setListViewHeightBasedOnItems(binding.lvSpotTypes);
        }
        else if (spot != null &&
                spot.getSpotTypeIds() != null && !spot.getSpotTypeIds().isEmpty() &&
                spotTypes != null && !spotTypes.isEmpty()) {
            spotTypeNames = spot.getSpotTypeIds().stream()
                    .flatMap(id -> spotTypes.stream().filter(s -> Objects.equals(s.getId(), id)))
                    .map(SpotType::getName)
                    .collect(Collectors.toList());

            spotInfoViewModel.setSpotTypeNames(spotTypeNames);
            ArrayAdapter<String> adapter =
                    new ArrayAdapter<>(getContext(),
                            R.layout.list_view_item, spotTypeNames);
            binding.lvSpotTypes.setAdapter(adapter);
            UIUtils.setListViewHeightBasedOnItems(binding.lvSpotTypes);
        }
    }
}