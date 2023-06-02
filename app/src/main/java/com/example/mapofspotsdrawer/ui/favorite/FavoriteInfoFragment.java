package com.example.mapofspotsdrawer.ui.favorite;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.mapofspotsdrawer.R;
import com.example.mapofspotsdrawer.api.LikesFavoritesAPI;
import com.example.mapofspotsdrawer.databinding.FragmentFavoriteInfoBinding;
import com.example.mapofspotsdrawer.map.YandexMapManager;
import com.example.mapofspotsdrawer.model.Spot;
import com.example.mapofspotsdrawer.retrofit.RetrofitService;
import com.example.mapofspotsdrawer.ui.adapter.recycler_view.SpotAdapter;
import com.example.mapofspotsdrawer.ui.utils.UIUtils;
import com.yandex.mapkit.MapKitFactory;
import com.yandex.mapkit.geometry.Point;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FavoriteInfoFragment extends Fragment {

    private FragmentFavoriteInfoBinding binding;

    private RecyclerView favoriteSpotsRecyclerView;

    private FavoriteInfoViewModel viewModel;

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        viewModel.setViewCreated(true);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MapKitFactory.initialize(this.requireContext());
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        viewModel = new ViewModelProvider(this).get(FavoriteInfoViewModel.class);

        binding = FragmentFavoriteInfoBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        UIUtils.showListMapImageButton(requireActivity().findViewById(R.id.ib_list_map));

        favoriteSpotsRecyclerView = binding.recyclerViewFavoriteSpots;
        favoriteSpotsRecyclerView.setLayoutManager(new LinearLayoutManager(requireActivity()));
        favoriteSpotsRecyclerView
                .addItemDecoration(new DividerItemDecoration(favoriteSpotsRecyclerView.getContext(),
                        DividerItemDecoration.VERTICAL));

        YandexMapManager.getInstance().setMapView(binding.mapviewFavoriteSpots, requireActivity());

        List<Spot> spots = viewModel.getSpots();

        // isViewCreated - создан ли фрагмент в результате поворота экрана.
        if (viewModel.isViewCreated() && spots != null && spots.size() != 0) {
            showSpotsOnMap(spots);
        }
        else {
            getFavoriteSpots();
        }

        return root;
    }

    private void getFavoriteSpots() {
        // Получение токена авторизации.
        String bearer = "Bearer " + PreferenceManager.getDefaultSharedPreferences(requireActivity())
                .getString("jwtToken", null);

        SharedPreferences preferences =
                android.preference.PreferenceManager.getDefaultSharedPreferences(getContext());
        String serverURL = preferences.getString("URL", getString(R.string.server_url));

        RetrofitService retrofitService;
        if (serverURL.isEmpty() || serverURL.isBlank()) {
            retrofitService = new RetrofitService(getString(R.string.server_url));
        }
        else {
            retrofitService = new RetrofitService(serverURL);
        }

        binding.progressBarFavoriteSpots.setVisibility(View.VISIBLE);

        // Создание API для совершения запроса к серверу.
        LikesFavoritesAPI likesFavoritesAPI
                = retrofitService.getRetrofit().create(LikesFavoritesAPI.class);

        likesFavoritesAPI.getUserFavoriteSpots(bearer)
                .enqueue(new Callback<>() {
                    @Override
                    public void onResponse(@NonNull Call<List<Spot>> call,
                                           @NonNull Response<List<Spot>> response) {
                        if (response.isSuccessful()) {
                            if (processSpotsResponseBody(response.body())) {
                                requireActivity().runOnUiThread(() ->
                                        binding.progressBarFavoriteSpots.setVisibility(View.GONE));
                                showSpotsOnMap(response.body());
                                setRecyclerView(response.body());
                            } else {
                                disableProgressBarAndShowNotification("У вас нет избранных спотов");
                            }
                        } else {
                            disableProgressBarAndShowNotification("Ошибка обработки запроса на сервере");
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<List<Spot>> call,
                                          @NonNull Throwable t) {
                        disableProgressBarAndShowNotification("Ошибка отправки запроса на сервер");
                    }
                });
    }

    private void showSpotsOnMap(List<Spot> spots) {
        YandexMapManager mapManager = YandexMapManager.getInstance();

        mapManager.addPlacemarks(spots,
                (AppCompatActivity) requireActivity(), R.id.fragment_container_favorite);
    }

    private void setRecyclerView(List<Spot> spots) {
        requireActivity().runOnUiThread(() ->
                favoriteSpotsRecyclerView.setAdapter(new SpotAdapter(requireActivity(),
                        R.id.fragment_container_favorite_spots, spots)));
    }

    private boolean processSpotsResponseBody(List<Spot> spots) {
        if (spots != null && spots.size() != 0) {
            viewModel.setSpots(spots);
            return true;
        }
        return false;
    }


    private void disableProgressBarAndShowNotification(String message) {
        requireActivity().runOnUiThread(() ->
                binding.progressBarFavoriteSpots.setVisibility(View.GONE));
        Toast.makeText(getActivity(),
                message, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onStop() {
        binding.mapviewFavoriteSpots.onStop();
        MapKitFactory.getInstance().onStop();
        super.onStop();
    }

    @Override
    public void onStart() {
        super.onStart();
        viewModel.setViewCreated(false);

        MapKitFactory.getInstance().onStart();
        binding.mapviewFavoriteSpots.onStart();
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