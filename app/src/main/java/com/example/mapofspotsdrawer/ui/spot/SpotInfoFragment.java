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
import com.example.mapofspotsdrawer.model.ImageInfoDto;
import com.example.mapofspotsdrawer.model.SpaceType;
import com.example.mapofspotsdrawer.model.SportType;
import com.example.mapofspotsdrawer.model.Spot;
import com.example.mapofspotsdrawer.model.SpotType;
import com.example.mapofspotsdrawer.ui.adapter.ImageSliderAdapter;
import com.example.mapofspotsdrawer.ui.utils.UIUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.stream.Collectors;

public class SpotInfoFragment extends Fragment {

    private FragmentSpotInfoBinding binding;

    private SpotInfoViewModel spotInfoViewModel;

    private List<String> imagesUrls = new ArrayList<>();

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
        setSportTypesListView(spot, sportTypes);
        setSpaceTypeTextView(spot, spaceTypes);
        setAddingDateTextView(spot);
        setUpdateDateTextView(spot);
        setImages(spot);

        ImageSliderAdapter imageSliderAdapter = new ImageSliderAdapter(getContext(), imagesUrls);
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
    private void setSportTypesListView(Spot spot, List<SportType> sportTypes) {
        List<String> sportTypeNames = spotInfoViewModel.getSportTypeNames();
        if (sportTypeNames != null && !sportTypeNames.isEmpty()) {
            ArrayAdapter<String> adapter =
                    new ArrayAdapter<>(getContext(),
                            R.layout.list_view_item, sportTypeNames);
            binding.lvSportTypes.setAdapter(adapter);
            UIUtils.setListViewHeightBasedOnItems(binding.lvSportTypes);
        }
        else if (spot != null &&
                spot.getSportTypeIds() != null && !spot.getSportTypeIds().isEmpty() &&
                sportTypes != null && !sportTypes.isEmpty()) {
            sportTypeNames = spot.getSportTypeIds().stream()
                    .flatMap(id -> sportTypes.stream().filter(s -> Objects.equals(s.getId(), id)))
                    .map(SportType::getName)
                    .collect(Collectors.toList());

            spotInfoViewModel.setSportTypeNames(sportTypeNames);
            ArrayAdapter<String> adapter =
                    new ArrayAdapter<>(getContext(),
                            R.layout.list_view_item, sportTypeNames);
            binding.lvSportTypes.setAdapter(adapter);
            UIUtils.setListViewHeightBasedOnItems(binding.lvSportTypes);
        }
    }

    private void setSpaceTypeTextView(Spot spot, List<SpaceType> spaceTypes) {
        String spaceTypeName = spotInfoViewModel.getSpaceTypeName();
        if (spaceTypeName != null && !spaceTypeName.isEmpty()) {
            binding.tvSpaceType.setText(spaceTypeName);
        }
        else if (spot != null && spot.getSpaceTypeId() != null &&
                spaceTypes != null && !spaceTypes.isEmpty()) {
            Integer spaceTypeId = spot.getSpaceTypeId();
            for (SpaceType spaceType : spaceTypes) {
                if (Objects.equals(spaceType.getId(), spaceTypeId)) {
                    spaceTypeName = spaceType.getName();
                    spotInfoViewModel.setSpaceTypeName(spaceTypeName);
                    binding.tvSpaceType.setText(spaceTypeName);
                    break;
                }
            }
        }
    }

    private void setAddingDateTextView(Spot spot) {
        String addingDateText = spotInfoViewModel.getAddingDateText();
        if (addingDateText != null && !addingDateText.isEmpty()) {
            binding.tvAddingDate.setText(addingDateText);
        }
        else if (spot != null && spot.getAddingDate() != null) {
            DateTimeFormatter formatter
                    = DateTimeFormatter.ofPattern("dd.MM.yyyy", Locale.getDefault());
            LocalDate addingDate =
                    spot.getAddingDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

            spotInfoViewModel.setAddingDateText(addingDate.format(formatter));
            binding.tvAddingDate.setText(addingDate.format(formatter));
        }
    }
    private void setUpdateDateTextView(Spot spot) {
        String updatingDateText = spotInfoViewModel.getUpdatingDateText();
        if (updatingDateText != null && !updatingDateText.isEmpty()) {
            binding.tvUpdateDate.setText(updatingDateText);
        }
        else if (spot != null && spot.getUpdatingDate() != null) {
            DateTimeFormatter formatter
                    = DateTimeFormatter.ofPattern("dd.MM.yyyy", Locale.getDefault());
            LocalDate updatingDate =
                    spot.getAddingDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

            spotInfoViewModel.setAddingDateText(updatingDate.format(formatter));
            binding.tvAddingDate.setText(updatingDate.format(formatter));
        }
        else if (binding.tvAddingDate.getText() != getString(R.string.spot_adding_date)) {
            spotInfoViewModel.setUpdatingDateText(binding.tvAddingDate.getText().toString());
            binding.tvUpdateDate.setText(binding.tvAddingDate.getText());
        }
    }

    private void setImages(Spot spot) {
        List<String> viewModelImagesUrls =
                spotInfoViewModel.getImagesUrls();
        if (viewModelImagesUrls != null && !viewModelImagesUrls.isEmpty()) {
            imagesUrls = viewModelImagesUrls;
        }
        else if (spot != null && spot.getImageInfoDtoList() != null
                && !spot.getImageInfoDtoList().isEmpty()) {
            List<ImageInfoDto> imagesInfosFromServer = spot.getImageInfoDtoList();
            for (ImageInfoDto info : imagesInfosFromServer) {
                imagesUrls.add(info.getUrl());
            }
            spotInfoViewModel.setImagesUrls(imagesUrls);
        }
        else {
            imagesUrls.add(getString(R.string.no_image_url));
        }
    }

}