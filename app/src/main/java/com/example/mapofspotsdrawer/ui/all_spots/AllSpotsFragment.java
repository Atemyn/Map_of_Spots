package com.example.mapofspotsdrawer.ui.all_spots;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.mapofspotsdrawer.R;
import com.example.mapofspotsdrawer.api.SpotAPI;
import com.example.mapofspotsdrawer.databinding.FragmentAllSpotsBinding;
import com.example.mapofspotsdrawer.map.YandexMapManager;
import com.example.mapofspotsdrawer.model.Spot;
import com.example.mapofspotsdrawer.retrofit.RetrofitService;
import com.yandex.mapkit.MapKitFactory;
import com.yandex.mapkit.geometry.Point;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AllSpotsFragment extends Fragment {

    private FragmentAllSpotsBinding binding;

    private AllSpotsViewModel allSpotsViewModel;

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        allSpotsViewModel.setViewCreated(true);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MapKitFactory.initialize(this.requireContext());
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        allSpotsViewModel = new ViewModelProvider(this).get(AllSpotsViewModel.class);

        binding = FragmentAllSpotsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        YandexMapManager.getInstance().setMapView(binding.mapviewAllSpots);

        List<Spot> spots = allSpotsViewModel.getSpots();
        // isViewCreated - создан ли фрагмент в результате поворота экрана.
        if (allSpotsViewModel.isViewCreated() && spots != null && spots.size() != 0) {
            showSpotsOnMap(spots);
        }
        else {
            getSpots();
        }

        return root;
    }

    private void showSpotsOnMap(List<Spot> spots) {
        YandexMapManager mapManager = YandexMapManager.getInstance();

        mapManager.addPlacemarks(spots, (AppCompatActivity) requireActivity());
    }

    private void getSpots() {
        RetrofitService retrofitService = new RetrofitService(getString(R.string.server_url));

        binding.progressBarAllSpots.setVisibility(View.VISIBLE);

        // Создание API для совершения запроса к серверу.
        SpotAPI spotAPI = retrofitService.getRetrofit().create(SpotAPI.class);

        spotAPI.getAllSpots()
                .enqueue(new Callback<List<Spot>>() {
                    @Override
                    public void onResponse(@NonNull Call<List<Spot>> call,
                                           @NonNull Response<List<Spot>> response) {
                        if (response.isSuccessful()) {
                            if (processSpotsResponseBody(response.body())) {
                                requireActivity().runOnUiThread(() ->
                                        binding.progressBarAllSpots.setVisibility(View.GONE));
                                showSpotsOnMap(response.body());
                            }
                            else {
                                disableProgressBarAndShowNotification("Ошибка получения тела ответа");
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
            allSpotsViewModel.setSpots(spots);
            return true;
        }
        return false;
    }

    private void disableProgressBarAndShowNotification(String message) {
        requireActivity().runOnUiThread(() ->
                binding.progressBarAllSpots.setVisibility(View.GONE));
        Toast.makeText(getActivity(),
                message, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onStop() {
        binding.mapviewAllSpots.onStop();
        MapKitFactory.getInstance().onStop();
        super.onStop();
    }

    @Override
    public void onStart() {
        super.onStart();
        allSpotsViewModel.setViewCreated(false);

        MapKitFactory.getInstance().onStart();
        binding.mapviewAllSpots.onStart();
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