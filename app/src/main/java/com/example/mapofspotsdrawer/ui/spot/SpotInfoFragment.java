package com.example.mapofspotsdrawer.ui.spot;

import androidx.lifecycle.ViewModelProvider;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.example.mapofspotsdrawer.R;
import com.example.mapofspotsdrawer.api.CommentsAPI;
import com.example.mapofspotsdrawer.api.LikesFavoritesAPI;
import com.example.mapofspotsdrawer.api.UserAPI;
import com.example.mapofspotsdrawer.databinding.FragmentSpotInfoBinding;
import com.example.mapofspotsdrawer.model.Comment;
import com.example.mapofspotsdrawer.model.ImageInfoDto;
import com.example.mapofspotsdrawer.model.SpaceType;
import com.example.mapofspotsdrawer.model.SportType;
import com.example.mapofspotsdrawer.model.Spot;
import com.example.mapofspotsdrawer.model.SpotType;
import com.example.mapofspotsdrawer.model.SpotUserDto;
import com.example.mapofspotsdrawer.model.User;
import com.example.mapofspotsdrawer.retrofit.RetrofitService;
import com.example.mapofspotsdrawer.ui.adapter.image_slider.ResponseBodyImageSliderAdapter;
import com.example.mapofspotsdrawer.ui.adapter.recycler_view.CommentAdapter;
import com.example.mapofspotsdrawer.ui.utils.UIUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.stream.Collectors;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SpotInfoFragment extends Fragment {

    private FragmentSpotInfoBinding binding;

    private SpotInfoViewModel spotInfoViewModel;

    private RetrofitService retrofitService;

    private RecyclerView commentsRecyclerView;

    private String userEmail = null;

    private List<String> imagesUrls = new ArrayList<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        spotInfoViewModel
                = new ViewModelProvider(this).get(SpotInfoViewModel.class);

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
        // Inflate the layout for this fragment
        binding = FragmentSpotInfoBinding.inflate(inflater, container, false);

        if (isLoggedIn()) {
            binding.enterCommentContainer.setVisibility(View.VISIBLE);
            getUserEmail();
        }
        else {
            binding.enterCommentContainer.setVisibility(View.GONE);
            doPostUserEmailGetWork();
        }

        return binding.getRoot();
    }

    private void doPostUserEmailGetWork() {
        commentsRecyclerView = binding.recyclerViewComments;
        commentsRecyclerView.setLayoutManager(new LinearLayoutManager(requireActivity()));

        UIUtils.hideListMapImageButton(requireActivity().findViewById(R.id.ib_list_map));

        configureFragmentViews();

        configureOnClickListeners();

        configureImageSlider();
    }

    private void configureFragmentViews() {
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

        if (spot != null) {
            setNameTextView(spot);
            setDescriptionTextView(spot);
            setLikeNumberTextView(spot);
            setFavoriteNumberTextView(spot);
            setSpotTypesListView(spot, spotTypes);
            setSportTypesListView(spot, sportTypes);
            setSpaceTypeTextView(spot, spaceTypes);
            setAddingDateTextView(spot);
            setUpdateDateTextView(spot);
            setImages(spot);
            getSpotComments(spot.getId());
        }

        String token = PreferenceManager.getDefaultSharedPreferences(requireActivity())
                .getString("jwtToken", null);
        if (token != null && !token.isEmpty()) {
            setLikedAndAddedToFavoriteImageButtons(spot, token);
        }
        else {
            binding.cvLikes.setVisibility(View.GONE);
            binding.cvFavorites.setVisibility(View.GONE);
        }

        binding.ibPostComment.setOnClickListener(view -> {
            if (binding.etEnterComment.getText() == null
                    || binding.etEnterComment.getText().toString().isEmpty()) {
                Toast.makeText(getActivity(),
                        "Текст комментария не может быть пустым", Toast.LENGTH_LONG).show();
            }
            else if (spot != null) {
                postComment(spot.getId());
            }
        });
    }

    private void configureOnClickListeners() {
        binding.ibLike.setOnClickListener(view -> {
            SpotUserDto spotUserDto = spotInfoViewModel.getSpotUserDto();
            if (spotUserDto != null) {
                changeLikeStateOnServer(spotUserDto);
            }
        });

        binding.ibFavorite.setOnClickListener(view -> {
            SpotUserDto spotUserDto = spotInfoViewModel.getSpotUserDto();
            if (spotUserDto != null) {
                changeFavoriteStateOnServer(spotUserDto);
            }
        });
    }

    private void configureImageSlider() {
        ResponseBodyImageSliderAdapter imageSliderAdapter =
                new ResponseBodyImageSliderAdapter(requireActivity(), imagesUrls);
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
    }

    private boolean isLoggedIn() {
        // Проверка, авторизован ли пользователь
        String token = PreferenceManager.getDefaultSharedPreferences(requireActivity())
                .getString("jwtToken", null);
        return token != null && !token.isEmpty();
    }

    private void getUserEmail() {
        SharedPreferences preferences =
                android.preference.PreferenceManager.getDefaultSharedPreferences(requireActivity());
        String token = preferences.getString("jwtToken", null);
        if (token != null && !token.isEmpty()) {
            String serverURL = preferences.getString("URL", getString(R.string.server_url));

            RetrofitService retrofitService;
            if (serverURL.isEmpty() || serverURL.isBlank()) {
                retrofitService = new RetrofitService(getString(R.string.server_url));
            }
            else {
                retrofitService = new RetrofitService(serverURL);
            }

            UserAPI userAPI = retrofitService.getRetrofit().create(UserAPI.class);

            userAPI.getUserInfo("Bearer " + token)
                    .enqueue(new retrofit2.Callback<>() {
                        @Override
                        public void onResponse(@NonNull retrofit2.Call<User> call,
                                               @NonNull retrofit2.Response<User> response) {
                            if (response.isSuccessful() && response.body() != null) {
                                User user = response.body();
                                userEmail = user.getEmail();
                                doPostUserEmailGetWork();
                            }
                        }

                        @Override
                        public void onFailure(@NonNull retrofit2.Call<User> call,
                                              @NonNull Throwable t) {
                            t.printStackTrace();
                        }
                    });
        }
    }

    private void getSpotComments(Long spotId) {
        binding.progressBar.setVisibility(View.VISIBLE);

        CommentsAPI commentsAPI = retrofitService.getRetrofit().create(CommentsAPI.class);

        commentsAPI.getSpotComments(spotId)
                .enqueue(new Callback<>() {
                    @Override
                    public void onResponse(@NonNull Call<List<Comment>> call,
                                           @NonNull Response<List<Comment>> response) {
                        if (response.isSuccessful()) {
                            requireActivity().runOnUiThread(()
                                    -> binding.progressBar.setVisibility(View.GONE));
                            setRecyclerView(response.body());
                        } else {
                            disableProgressBarAndShowNotification(
                                    "Ошибка обработки запроса на получение комментариев на сервере");
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<List<Comment>> call,
                                          @NonNull Throwable t) {
                        disableProgressBarAndShowNotification(
                                "Ошибка отправки запроса на получение комментариев на сервер");
                    }
                });
    }

    private void setRecyclerView(List<Comment> comments) {
        requireActivity().runOnUiThread(() ->
                commentsRecyclerView.setAdapter(new CommentAdapter(commentsRecyclerView, requireActivity(),
                        comments, userEmail)));
    }

    private void postComment(Long spotId) {
        String token = PreferenceManager.getDefaultSharedPreferences(requireActivity())
                .getString("jwtToken", null);
        if (token == null || token.isEmpty())
            return;

        String bearer = "Bearer " + token;

        try {

            RequestBody requestBody =
                    RequestBody.create(MediaType.parse("application/json"),
                            createJSONObject().toString());

            binding.progressBar.setVisibility(View.VISIBLE);

            CommentsAPI commentsAPI = retrofitService.getRetrofit().create(CommentsAPI.class);

            commentsAPI.postComment(spotId, bearer, requestBody)
                    .enqueue(new Callback<>() {
                        @Override
                        public void onResponse(@NonNull Call<ResponseBody> call,
                                               @NonNull Response<ResponseBody> response) {
                            if (response.isSuccessful()) {
                                binding.etEnterComment.setText("");
                                getSpotComments(spotId);
                                disableProgressBarAndShowNotification("Комментарий успешно добавлен!");
                            } else {
                                disableProgressBarAndShowNotification("Ошибка обработки комментария на сервере");
                            }
                        }

                        @Override
                        public void onFailure(@NonNull Call<ResponseBody> call,
                                              @NonNull Throwable t) {
                            disableProgressBarAndShowNotification("Ошибка отправки комментария на сервер");
                        }
                    });
        }
        catch (JSONException e) {
            disableProgressBarAndShowNotification("Ошибка формирования запроса к серверу");
        }
    }

    private JSONObject createJSONObject() throws JSONException {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("text", binding.etEnterComment.getText().toString());

        return jsonObject;
    }

    private void changeLikeStateOnServer(SpotUserDto spotUserDto) {
        String token = PreferenceManager.getDefaultSharedPreferences(requireActivity())
                .getString("jwtToken", null);
        if (token == null || token.isEmpty())
            return;

        String bearer = "Bearer " + token;

        binding.progressBar.setVisibility(View.VISIBLE);

        // Создание API для совершения запроса к серверу.
        LikesFavoritesAPI likesFavoritesAPI =
                retrofitService.getRetrofit().create(LikesFavoritesAPI.class);

        likesFavoritesAPI.changeLikeStateForSpot(spotUserDto.getSpotId(), bearer)
                .enqueue(new Callback<>() {
                    @Override
                    public void onResponse(@NonNull Call<ResponseBody> call,
                                           @NonNull Response<ResponseBody> response) {
                        if (response.isSuccessful()) {
                            if (response.body() == null) {
                                disableProgressBarAndShowNotification("Ошибка получения тела ответа");
                                return;
                            }

                            SpotUserDto spotUserDto = spotInfoViewModel.getSpotUserDto();
                            if (spotUserDto.getLiked()) {
                                binding.ibLike.setImageResource(R.drawable.heart_empty);
                                spotUserDto.setLiked(false);
                                spotInfoViewModel.setLikeNumber(
                                        Integer.toString(Integer.parseInt(spotInfoViewModel.getLikeNumber()) - 1));
                            } else {
                                binding.ibLike.setImageResource(R.drawable.heart_filled);
                                spotUserDto.setLiked(true);
                                spotInfoViewModel.setLikeNumber(
                                        Integer.toString(Integer.parseInt(spotInfoViewModel.getLikeNumber()) + 1));
                            }
                            binding.tvLikes.setText(spotInfoViewModel.getLikeNumber());

                            requireActivity().runOnUiThread(()
                                    -> binding.progressBar.setVisibility(View.GONE));
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

    private void changeFavoriteStateOnServer(SpotUserDto spotUserDto) {
        String token = PreferenceManager.getDefaultSharedPreferences(requireActivity())
                .getString("jwtToken", null);
        if (token == null || token.isEmpty())
            return;

        String bearer = "Bearer " + token;

        binding.progressBar.setVisibility(View.VISIBLE);

        // Создание API для совершения запроса к серверу.
        LikesFavoritesAPI likesFavoritesAPI =
                retrofitService.getRetrofit().create(LikesFavoritesAPI.class);

        likesFavoritesAPI.changeFavoriteStateForSpot(spotUserDto.getSpotId(), bearer)
                .enqueue(new Callback<>() {
                    @Override
                    public void onResponse(@NonNull Call<ResponseBody> call,
                                           @NonNull Response<ResponseBody> response) {
                        if (response.isSuccessful()) {
                            if (response.body() == null) {
                                disableProgressBarAndShowNotification("Ошибка получения тела ответа");
                                return;
                            }

                            if (spotUserDto.getFavorite()) {
                                binding.ibFavorite.setImageResource(R.drawable.star_empty);
                                spotUserDto.setFavorite(false);
                                spotInfoViewModel.setFavoriteNumber(
                                        Integer.toString(Integer.parseInt(spotInfoViewModel.getFavoriteNumber()) - 1));
                            } else {
                                binding.ibFavorite.setImageResource(R.drawable.star_filled);
                                spotUserDto.setFavorite(true);
                                spotInfoViewModel.setFavoriteNumber(
                                        Integer.toString(Integer.parseInt(spotInfoViewModel.getFavoriteNumber()) + 1));
                            }
                            binding.tvFavorites.setText(spotInfoViewModel.getFavoriteNumber());

                            requireActivity().runOnUiThread(()
                                    -> binding.progressBar.setVisibility(View.GONE));
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

    private void setLikedAndAddedToFavoriteImageButtons(Spot spot, String token) {
        SpotUserDto spotUserDto = spotInfoViewModel.getSpotUserDto();
        if (spotUserDto != null && spotUserDto.getLiked() != null
                && spotUserDto.getFavorite() != null) {
            setLikedAndAddedToFavoriteIcons(spotUserDto.getLiked(), spotUserDto.getFavorite());
        }
        else {
            getLikedAndAddedToFavoriteFromServer(token, spot.getId());
        }
    }

    private void setLikedAndAddedToFavoriteIcons(boolean isLiked, boolean isAddedToFavorite) {
        if (isLiked) {
            binding.ibLike.setImageResource(R.drawable.heart_filled);
        }
        else {
            binding.ibLike.setImageResource(R.drawable.heart_empty);
        }

        if (isAddedToFavorite) {
            binding.ibFavorite.setImageResource(R.drawable.star_filled);
        }
        else {
            binding.ibFavorite.setImageResource(R.drawable.star_empty);
        }
    }

    private void getLikedAndAddedToFavoriteFromServer(String token, Long spotId) {

        String bearer = "Bearer " + token;

        binding.progressBar.setVisibility(View.VISIBLE);

        // Создание API для совершения запроса к серверу.
        LikesFavoritesAPI likesFavoritesAPI = retrofitService.getRetrofit().create(LikesFavoritesAPI.class);

        likesFavoritesAPI.getLikesAndFavoritesForSpot(spotId, bearer)
                .enqueue(new Callback<>() {
                    @Override
                    public void onResponse(@NonNull Call<SpotUserDto> call,
                                           @NonNull Response<SpotUserDto> response) {
                        if (response.isSuccessful()) {
                            SpotUserDto spotUserDto = response.body();
                            if (spotUserDto == null) {
                                disableProgressBarAndShowNotification("Ошибка получения тела ответа");
                                return;
                            }

                            spotInfoViewModel.setSpotUserDto(spotUserDto);
                            setLikedAndAddedToFavoriteIcons(spotUserDto.getLiked(), spotUserDto.getFavorite());
                            requireActivity().runOnUiThread(()
                                    -> binding.progressBar.setVisibility(View.GONE));
                        } else {
                            disableProgressBarAndShowNotification("Ошибка обработки запроса на сервере");
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<SpotUserDto> call,
                                          @NonNull Throwable t) {
                        disableProgressBarAndShowNotification("Ошибка отправки запроса на сервер");
                    }
                });
    }

    private void disableProgressBarAndShowNotification(String message) {
        requireActivity().runOnUiThread(() ->
                binding.progressBar.setVisibility(View.GONE));
        Toast.makeText(getActivity(),
                message, Toast.LENGTH_LONG).show();
    }

    @SuppressLint("SetTextI18n")
    private void setLikeNumberTextView(Spot spot) {
        String likeNumberString = spotInfoViewModel.getLikeNumber();
        if (likeNumberString != null && !likeNumberString.isEmpty()) {
            binding.tvLikes.setText(likeNumberString);
        }
        else if (spot != null && spot.getLikeNumber() != null) {
            Integer likeNumber = spot.getLikeNumber();
            spotInfoViewModel.setLikeNumber(likeNumber.toString());
            binding.tvLikes.setText(likeNumber.toString());
        }
    }

    @SuppressLint("SetTextI18n")
    private void setFavoriteNumberTextView(Spot spot) {
        String favoriteNumberString = spotInfoViewModel.getFavoriteNumber();
        if (favoriteNumberString != null && !favoriteNumberString.isEmpty()) {
            binding.tvFavorites.setText(favoriteNumberString);
        }
        else if (spot != null && spot.getLikeNumber() != null) {
            Integer favoriteNumber = spot.getFavoriteNumber();
            spotInfoViewModel.setFavoriteNumber(favoriteNumber.toString());
            binding.tvFavorites.setText(favoriteNumber.toString());
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