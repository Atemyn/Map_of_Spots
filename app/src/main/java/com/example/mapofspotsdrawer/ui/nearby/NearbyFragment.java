package com.example.mapofspotsdrawer.ui.nearby;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.Manifest;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.preference.PreferenceManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.mapofspotsdrawer.R;
import com.example.mapofspotsdrawer.api.SpotAPI;
import com.example.mapofspotsdrawer.databinding.FragmentNearbyBinding;
import com.example.mapofspotsdrawer.map.YandexMapManager;
import com.example.mapofspotsdrawer.model.Spot;
import com.example.mapofspotsdrawer.retrofit.RetrofitService;
import com.yandex.mapkit.MapKitFactory;
import com.yandex.mapkit.geometry.Point;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NearbyFragment extends Fragment {

    private FragmentNearbyBinding binding;

    public static NearbyFragment newInstance() {
        return new NearbyFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MapKitFactory.initialize(this.requireContext());
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentNearbyBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        YandexMapManager.getInstance().setMapView(binding.mapviewNearbySpots);

        LocationManager locationManager =
                (LocationManager) requireActivity().getSystemService(Context.LOCATION_SERVICE);

        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
                || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
            // Android 6.0 и выше
            if (requireActivity().checkSelfPermission(
                    Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                    && requireActivity().checkSelfPermission(
                            Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // Нет разрешения на доступ к геолокации
                Toast.makeText(getActivity(), "Приложению требуется разрешение на использование геолокации",
                        Toast.LENGTH_LONG).show();
            } else {
                Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

                SharedPreferences sharedPreferences
                        = PreferenceManager.getDefaultSharedPreferences(requireActivity());
                String radiusString = sharedPreferences.getString("radius", "400");
                try {
                    double radius = Double.parseDouble(radiusString);

                    // isViewCreated - создан ли фрагмент в результате поворота экрана.
                    getNearbySpots(location.getLatitude(), location.getLongitude(), radius);
                }
                catch (NumberFormatException e) {
                    Toast.makeText(getActivity(), "В настройках указано" +
                                    "неверное число для максимального расстояния до спота",
                            Toast.LENGTH_LONG).show();
                }
            }
        } else {
            Toast.makeText(getActivity(), "Чтобы пользоваться данной функцией, включите геолокация",
                    Toast.LENGTH_LONG).show();
        }

        return root;
    }

    private void showSpotsOnMap(List<Spot> spots) {
        YandexMapManager mapManager = YandexMapManager.getInstance();
        mapManager.clearMap();
        mapManager.addPlacemarks(spots,
                (AppCompatActivity) requireActivity(), R.id.fragment_container_nearby_spots);
    }

    private void getNearbySpots(double latitude, double longitude, double radius) {
        RetrofitService retrofitService = new RetrofitService(getString(R.string.server_url));

        binding.progressBarNearbySpots.setVisibility(View.VISIBLE);

        // Создание API для совершения запроса к серверу.
        SpotAPI spotAPI = retrofitService.getRetrofit().create(SpotAPI.class);

        spotAPI.getNearbySpots(latitude, longitude, radius)
                .enqueue(new Callback<List<Spot>>() {
                    @Override
                    public void onResponse(@NonNull Call<List<Spot>> call,
                                           @NonNull Response<List<Spot>> response) {
                        if (response.isSuccessful()) {
                            if (response.body() != null && response.body().size() != 0) {
                                requireActivity().runOnUiThread(() ->
                                        binding.progressBarNearbySpots.setVisibility(View.GONE));
                                showSpotsOnMap(response.body());
                            }
                            else {
                                disableProgressBarAndShowNotification("Спотов поблизости не найдено");
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

    private void disableProgressBarAndShowNotification(String message) {
        requireActivity().runOnUiThread(() ->
                binding.progressBarNearbySpots.setVisibility(View.GONE));
        Toast.makeText(getActivity(),
                message, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onStop() {
        binding.mapviewNearbySpots.onStop();
        MapKitFactory.getInstance().onStop();
        super.onStop();
    }

    @Override
    public void onStart() {
        super.onStart();
        MapKitFactory.getInstance().onStart();
        binding.mapviewNearbySpots.onStart();
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