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

import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.example.mapofspotsdrawer.R;
import com.example.mapofspotsdrawer.api.SpotAPI;
import com.example.mapofspotsdrawer.databinding.FragmentCreateSpotInfoBinding;
import com.example.mapofspotsdrawer.map.PlacemarkInputListener;
import com.example.mapofspotsdrawer.map.YandexMapManager;
import com.example.mapofspotsdrawer.model.SpaceType;
import com.example.mapofspotsdrawer.model.SportType;
import com.example.mapofspotsdrawer.model.SpotType;
import com.example.mapofspotsdrawer.retrofit.RetrofitService;
import com.example.mapofspotsdrawer.ui.adapter.image_slider.ImageSliderAdapter;
import com.example.mapofspotsdrawer.ui.create_spot.validation.AllFieldsValidator;
import com.example.mapofspotsdrawer.ui.create_spot.validation.EditTextOnFocusChangeListener;
import com.example.mapofspotsdrawer.ui.create_spot.validation.ListViewOnItemClickListener;
import com.example.mapofspotsdrawer.ui.utils.UIUtils;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.yandex.mapkit.MapKitFactory;
import com.yandex.mapkit.geometry.Point;
import com.yandex.mapkit.mapview.MapView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.lang.reflect.Type;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CreateSpotInfoFragment extends Fragment {

    private FragmentCreateSpotInfoBinding binding;

    private CreateSpotInfoViewModel viewModel;

    private ActivityResultLauncher<Intent> imagePickerLauncher;

    private boolean noImage = true;

    private MapView mapView;

    private List<SpotType> spotTypes;

    private List<SportType> sportTypes;

    private List<SpaceType> spaceTypes;

    private RetrofitService retrofitService;

    private AllFieldsValidator allFieldsValidator;

    private PlacemarkInputListener placemarkInputListener;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MapKitFactory.initialize(this.requireContext());

        viewModel = new ViewModelProvider(this).get(CreateSpotInfoViewModel.class);

        SharedPreferences preferences =
                android.preference.PreferenceManager.getDefaultSharedPreferences(getContext());
        String serverURL = preferences.getString("URL", getString(R.string.server_url));

        if (serverURL.isEmpty() || serverURL.isBlank()) {
            retrofitService = new RetrofitService(getString(R.string.server_url));
        }
        else {
            retrofitService = new RetrofitService(serverURL);
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        binding = FragmentCreateSpotInfoBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        YandexMapManager.getInstance().setMapView(binding.mapviewCreateSpot, requireActivity());

        mapView = YandexMapManager.getInstance().getMapView();

        SharedPreferences sharedPreferences =
                PreferenceManager.getDefaultSharedPreferences(requireActivity());
        Gson gson = new Gson();

        // Получение содержимого таблиц-справочников.
        spotTypes = getSpotTypesFromSharedPreferences(sharedPreferences, gson);
        sportTypes = getSportTypesFromSharedPreferences(sharedPreferences, gson);
        spaceTypes = getSpaceTypesFromSharedPreferences(sharedPreferences, gson);

        setSpotTypesMultipleChoiceListView(spotTypes);
        setSportTypesMultipleChoiceListView(sportTypes);
        setSpaceTypeListView(spaceTypes);

        binding.btnOpenFullscreen.setTag(R.drawable.ic_open_fullscreen);
        binding.btnOpenFullscreen.setOnClickListener(view -> {
            if ((int) binding.btnOpenFullscreen.getTag()
                    == R.drawable.ic_open_fullscreen) {
                binding.btnOpenFullscreen.setImageResource(R.drawable.ic_close_fullscreen);
                binding.btnOpenFullscreen.setTag(R.drawable.ic_close_fullscreen);
                mapView.setVisibility(View.VISIBLE);
            }
            else {
                binding.btnOpenFullscreen.setImageResource(R.drawable.ic_open_fullscreen);
                binding.btnOpenFullscreen.setTag(R.drawable.ic_open_fullscreen);
                mapView.setVisibility(View.GONE);
            }
        });

        if (viewModel.getImagesUrls() == null || viewModel.getImagesUrls().size() == 0) {
            viewModel.setImagesUrls(new ArrayList<>());
            viewModel.addImageUri(getString(R.string.no_image_url));

            ImageSliderAdapter adapter =
                    new ImageSliderAdapter(getContext(), viewModel.getImagesUrls());
            binding.imageSliderCreateSpot.setAdapter(adapter);
        }

        allFieldsValidator
                = new AllFieldsValidator(binding.btnAddSpot, binding.etSpotName,
                binding.etSpotDescription, binding.listviewSpotTypes,
                binding.listviewSportTypes, binding.listviewSpaceType,
                viewModel, getString(R.string.no_image_url));

        binding.etSpotName.setOnFocusChangeListener(new EditTextOnFocusChangeListener(allFieldsValidator));
        binding.etSpotDescription.setOnFocusChangeListener(new EditTextOnFocusChangeListener(allFieldsValidator));
        binding.listviewSpotTypes.setOnItemClickListener(new ListViewOnItemClickListener(allFieldsValidator));
        binding.listviewSportTypes.setOnItemClickListener(new ListViewOnItemClickListener(allFieldsValidator));
        binding.listviewSpaceType.setOnItemClickListener(new ListViewOnItemClickListener(allFieldsValidator));

        placemarkInputListener
                = new PlacemarkInputListener(allFieldsValidator, requireActivity());

        mapView.getMap().addInputListener(placemarkInputListener);

        imagePickerLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
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
                        requireActivity().runOnUiThread(() -> Toast.makeText(requireActivity(),
                                ImagePicker.getError(result.getData()), Toast.LENGTH_LONG).show());
                    } else {
                        requireActivity().runOnUiThread(() -> Toast.makeText(requireActivity(),
                                "Добавление фотографии отменено", Toast.LENGTH_LONG).show());
                    }
                    allFieldsValidator.validateFields();
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

                allFieldsValidator.validateFields();
            }
            else {
                requireActivity().runOnUiThread(() -> Toast.makeText(requireActivity(),
                        "Фотографий нет: удаление невозможно", Toast.LENGTH_LONG).show());
            }
        });

        binding.btnAddSpot.setOnClickListener(view -> addSpot());

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

    private void addSpot() {
        try {
            // Получение токена авторизации.
            String bearer = "Bearer " + PreferenceManager.getDefaultSharedPreferences(requireActivity())
                    .getString("jwtToken", null);

            binding.progressBar.setVisibility(View.VISIBLE);

            JSONObject spotDtoJsonObject = createSpotDtoJSONObject();

            // Cоздание RequestBody для JSON объекта.
            RequestBody spotDtoRequestBody =
                    RequestBody.create(MediaType.parse("application/json"),
                            spotDtoJsonObject.toString());

            // Формирование списка картинок.
            List<File> listOfImages = new ArrayList<>();
            for (String imageStringUrl: viewModel.getImagesUrls()) {
                URL imageUrl = new URL(imageStringUrl);
                File imageFile = new File(imageUrl.getFile());
                listOfImages.add(imageFile);
            }

            // Формирование части запроса с ключом "files", представляющей собой список объектов класса File.
            List<MultipartBody.Part> files = new ArrayList<>();
            for (File file : listOfImages) {
                RequestBody fileRequestBody =
                        RequestBody.create(MediaType.parse("image/*"), file);
                MultipartBody.Part filePart =
                        MultipartBody.Part.createFormData("files", file.getName(), fileRequestBody);
                files.add(filePart);
            }

            SpotAPI spotAPI = retrofitService.getRetrofit().create(SpotAPI.class);
            spotAPI.sendSpotToModeration(bearer, files, spotDtoRequestBody)
                    .enqueue(new Callback<>() {
                        @Override
                        public void onResponse(@NonNull Call<ResponseBody> call,
                                               @NonNull Response<ResponseBody> response) {
                            if (response.isSuccessful()) {
                                disableProgressBarAndShowNotification("Спот успешно добавлен");
                                requireActivity().onBackPressed();
                            } else {
                                disableProgressBarAndShowNotification("Ошибка обработки запроса на сервере");
                            }
                        }

                        @Override
                        public void onFailure(@NonNull Call<ResponseBody> call,
                                              @NonNull Throwable t) {
                            disableProgressBarAndShowNotification("Ошибка отправки запроса на сервер");
                        }
                    });
        }
        catch (JSONException e) {
            requireActivity().runOnUiThread(() -> Toast.makeText(requireActivity(),
                    "Ошибка формирования тела запроса", Toast.LENGTH_LONG).show());
        } catch (MalformedURLException e) {
            requireActivity().runOnUiThread(() -> Toast.makeText(requireActivity(),
                    "Ошибка получения фотографий", Toast.LENGTH_LONG).show());
        }
    }

    private void disableProgressBarAndShowNotification(String message) {
        requireActivity().runOnUiThread(() ->
                binding.progressBar.setVisibility(View.GONE));
        requireActivity().runOnUiThread(() -> Toast.makeText(getActivity(),
                message, Toast.LENGTH_LONG).show());
    }

    private JSONObject createSpotDtoJSONObject() throws JSONException {
        Point point = YandexMapManager.getInstance().getSingleMapObject().getGeometry();
        JSONObject spotDtoJsonObject = new JSONObject();
        spotDtoJsonObject.put("name", binding.etSpotName.getText().toString());
        spotDtoJsonObject.put("latitude", point.getLatitude());
        spotDtoJsonObject.put("longitude", point.getLongitude());
        spotDtoJsonObject.put("description", binding.etSpotDescription.getText().toString());
        spotDtoJsonObject.put("spotTypeIds", new JSONArray(getSpotTypesListViewCheckedItemsIds()));
        spotDtoJsonObject.put("sportTypeIds",new JSONArray(getSportTypesListViewCheckedItemsIds()));
        spotDtoJsonObject.put("spaceTypeId", getSpaceTypesListViewCheckedItemsIds());

        return spotDtoJsonObject;
    }

    private int[] getSpotTypesListViewCheckedItemsIds() {
        ListAdapter adapter = binding.listviewSpotTypes.getAdapter();

        SparseBooleanArray checkedItems = binding.listviewSpotTypes.getCheckedItemPositions();

        List<Integer> checkedItemsIds = new ArrayList<>();

        for (int i = 0; i < checkedItems.size(); i++) {
            if (checkedItems.valueAt(i)) {
                String spotTypeName = (String) adapter.getItem(checkedItems.keyAt(i));
                checkedItemsIds.add(getSpotTypeIdByName(spotTypeName));
            }
        }

        int[] checkedItemsIdsArray = new int[checkedItemsIds.size()];
        for (int i = 0; i < checkedItemsIds.size(); i++) {
            checkedItemsIdsArray[i] = checkedItemsIds.get(i);
        }

        return checkedItemsIdsArray;
    }

    private int[] getSportTypesListViewCheckedItemsIds() {
        ListAdapter adapter = binding.listviewSportTypes.getAdapter();

        SparseBooleanArray checkedItems = binding.listviewSportTypes.getCheckedItemPositions();

        List<Integer> checkedItemsIds = new ArrayList<>();

        for (int i = 0; i < checkedItems.size(); i++) {
            if (checkedItems.valueAt(i)) {
                String sportTypeName = (String) adapter.getItem(checkedItems.keyAt(i));
                checkedItemsIds.add(getSportTypeIdByName(sportTypeName));
            }
        }

        int[] checkedItemsIdsArray = new int[checkedItemsIds.size()];
        for (int i = 0; i < checkedItemsIds.size(); i++) {
            checkedItemsIdsArray[i] = checkedItemsIds.get(i);
        }

        return checkedItemsIdsArray;
    }

    private int getSpaceTypesListViewCheckedItemsIds() {
        ListAdapter adapter = binding.listviewSpaceType.getAdapter();

        SparseBooleanArray checkedItems = binding.listviewSpaceType.getCheckedItemPositions();

        for (int i = 0; i < checkedItems.size(); i++) {
            if (checkedItems.valueAt(i)) {
                String spaceTypeName = (String) adapter.getItem(checkedItems.keyAt(i));
                return getSpaceTypeIdByName(spaceTypeName);
            }
        }

        return -1;
    }

    private int getSpotTypeIdByName(String name) {
        for (SpotType spotType : spotTypes) {
            if (Objects.equals(spotType.getName(), name)) {
                return spotType.getId();
            }
        }
        return -1;
    }

    private int getSportTypeIdByName(String name) {
        for (SportType sportType : sportTypes) {
            if (Objects.equals(sportType.getName(), name)) {
                return sportType.getId();
            }
        }
        return -1;
    }

    private int getSpaceTypeIdByName(String name) {
        for (SpaceType spaceType : spaceTypes) {
            if (Objects.equals(spaceType.getName(), name)) {
                return spaceType.getId();
            }
        }
        return -1;
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

        YandexMapManager.getInstance().moveMapTo(new Point(55.751574, 80.573856), 2.0f);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        YandexMapManager.getInstance().setSingleMapObject(null);
    }
}