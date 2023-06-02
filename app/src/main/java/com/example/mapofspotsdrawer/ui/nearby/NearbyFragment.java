package com.example.mapofspotsdrawer.ui.nearby;

import static com.yandex.runtime.Runtime.getApplicationContext;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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
import com.example.mapofspotsdrawer.api.SpotAPI;
import com.example.mapofspotsdrawer.databinding.FragmentNearbyBinding;
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

public class NearbyFragment extends Fragment {

    private FragmentNearbyBinding binding;

    private RecyclerView nearbySpotsRecyclerView;

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
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

        UIUtils.showListMapImageButton(requireActivity().findViewById(R.id.ib_list_map));

        nearbySpotsRecyclerView = binding.recyclerViewNearbySpots;
        nearbySpotsRecyclerView.setLayoutManager(new LinearLayoutManager(requireActivity()));
        nearbySpotsRecyclerView
                .addItemDecoration(new DividerItemDecoration(nearbySpotsRecyclerView.getContext(),
                        DividerItemDecoration.VERTICAL));

        YandexMapManager.getInstance().setMapView(binding.mapviewNearbySpots, requireActivity());

        LocationManager locationManager =
                (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);

        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
                || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
            // Android 6.0 и выше
            if (requireActivity().checkSelfPermission(
                    Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                    && requireActivity().checkSelfPermission(
                            Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // Нет разрешения на доступ к геолокации
                requireActivity().runOnUiThread(() -> Toast.makeText(getActivity(),
                        "Приложению требуется разрешение на использование геолокации",
                        Toast.LENGTH_LONG).show());
            } else {
                Location location = getLastKnownLocation(locationManager);

                SharedPreferences sharedPreferences
                        = PreferenceManager.getDefaultSharedPreferences(requireActivity());
                String radiusString = sharedPreferences.getString("radius", "400");
                try {
                    double radius = Double.parseDouble(radiusString);

                    getNearbySpots(location.getLatitude(), location.getLongitude(), radius);

                }
                catch (NumberFormatException e) {
                    requireActivity().runOnUiThread(() -> Toast.makeText(getActivity(), "В настройках указано" +
                                    "неверное число для максимального расстояния до спота",
                            Toast.LENGTH_LONG).show());
                }
            }
        } else {
            requireActivity().runOnUiThread(() -> Toast.makeText(getActivity(),
                    "Чтобы пользоваться данной функцией, включите геолокация",
                    Toast.LENGTH_LONG).show());
        }

        return root;
    }

    private Location getLastKnownLocation(LocationManager locationManager) {
        List<String> providers = locationManager.getProviders(true);
        Location bestLocation = null;
        for (String provider : providers) {
            @SuppressLint("MissingPermission")
            Location l = locationManager.getLastKnownLocation(provider);
            if (l == null) {
                continue;
            }
            if (bestLocation == null || l.getAccuracy() < bestLocation.getAccuracy()) {
                // Found best last known location: %s", l);
                bestLocation = l;
            }
        }
        return bestLocation;
    }

    private void getNearbySpots(double latitude, double longitude, double radius) {
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

        binding.progressBarNearbySpots.setVisibility(View.VISIBLE);

        // Создание API для совершения запроса к серверу.
        SpotAPI spotAPI = retrofitService.getRetrofit().create(SpotAPI.class);

        spotAPI.getNearbySpots(latitude, longitude, radius)
                .enqueue(new Callback<>() {
                    @Override
                    public void onResponse(@NonNull Call<List<Spot>> call,
                                           @NonNull Response<List<Spot>> response) {
                        if (response.isSuccessful()) {
                            if (isResponseBodyNotEmpty(response.body())) {
                                requireActivity().runOnUiThread(() ->
                                        binding.progressBarNearbySpots.setVisibility(View.GONE));
                                showSpotsOnMap(response.body());
                                setRecyclerView(response.body());
                            } else {
                                disableProgressBarAndShowNotification("Спотов поблизости не найдено");
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
        mapManager.setMapView(binding.mapviewNearbySpots, requireActivity());
        mapManager.addPlacemarks(spots,
                (AppCompatActivity) requireActivity(), R.id.fragment_container_nearby_spots);
    }

    private void setRecyclerView(List<Spot> spots) {
        requireActivity().runOnUiThread(() ->
                nearbySpotsRecyclerView.setAdapter(new SpotAdapter(requireActivity(),
                        R.id.fragment_container_nearby_spots, spots)));
    }

    private boolean isResponseBodyNotEmpty(List<Spot> spots) {
        return spots != null && spots.size() != 0;
    }

    private void disableProgressBarAndShowNotification(String message) {
        requireActivity().runOnUiThread(() ->
                binding.progressBarNearbySpots.setVisibility(View.GONE));
        requireActivity().runOnUiThread(() -> Toast.makeText(getActivity(),
                message, Toast.LENGTH_LONG).show());
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