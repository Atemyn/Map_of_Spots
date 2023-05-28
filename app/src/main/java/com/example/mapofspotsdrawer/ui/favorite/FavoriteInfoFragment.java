package com.example.mapofspotsdrawer.ui.favorite;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.preference.PreferenceManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.mapofspotsdrawer.R;
import com.example.mapofspotsdrawer.api.LikesFavoritesAPI;
import com.example.mapofspotsdrawer.api.SpotAPI;
import com.example.mapofspotsdrawer.databinding.FragmentFavoriteInfoBinding;
import com.example.mapofspotsdrawer.map.YandexMapManager;
import com.example.mapofspotsdrawer.model.Spot;
import com.example.mapofspotsdrawer.retrofit.RetrofitService;
import com.example.mapofspotsdrawer.ui.all_spots.AllSpotsViewModel;
import com.yandex.mapkit.MapKitFactory;
import com.yandex.mapkit.geometry.Point;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FavoriteInfoFragment extends Fragment {

    private FragmentFavoriteInfoBinding binding;

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

        YandexMapManager.getInstance().setMapView(binding.mapviewFavoriteSpots);

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

    private void showSpotsOnMap(List<Spot> spots) {
        YandexMapManager mapManager = YandexMapManager.getInstance();

        mapManager.addPlacemarks(spots,
                (AppCompatActivity) requireActivity(), R.id.fragment_container_favorite);
    }

    private void getFavoriteSpots() {
        // Получение токена авторизации.
        String bearer = "Bearer " + PreferenceManager.getDefaultSharedPreferences(requireActivity())
                .getString("jwtToken", null);

        RetrofitService retrofitService = new RetrofitService(getString(R.string.server_url));

        binding.progressBarFavoriteSpots.setVisibility(View.VISIBLE);

        // Создание API для совершения запроса к серверу.
        LikesFavoritesAPI likesFavoritesAPI
                = retrofitService.getRetrofit().create(LikesFavoritesAPI.class);

        likesFavoritesAPI.getUserFavoriteSpots(bearer)
                .enqueue(new Callback<List<Spot>>() {
                    @Override
                    public void onResponse(@NonNull Call<List<Spot>> call,
                                           @NonNull Response<List<Spot>> response) {
                        if (response.isSuccessful()) {
                            if (processSpotsResponseBody(response.body())) {
                                requireActivity().runOnUiThread(() ->
                                        binding.progressBarFavoriteSpots.setVisibility(View.GONE));
                                showSpotsOnMap(response.body());
                            }
                            else {
                                disableProgressBarAndShowNotification("У вас нет избранных спотов");
                            }
                        }
                        else {
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