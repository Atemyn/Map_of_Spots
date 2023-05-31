package com.example.mapofspotsdrawer.ui.all_spots;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mapofspotsdrawer.R;
import com.example.mapofspotsdrawer.api.SpotAPI;
import com.example.mapofspotsdrawer.databinding.FragmentAllSpotsBinding;
import com.example.mapofspotsdrawer.map.YandexMapManager;
import com.example.mapofspotsdrawer.model.Spot;
import com.example.mapofspotsdrawer.retrofit.RetrofitService;
import com.example.mapofspotsdrawer.ui.adapter.recycler_view.SpotAdapter;
import com.example.mapofspotsdrawer.ui.manager.UIManager;
import com.yandex.mapkit.MapKitFactory;
import com.yandex.mapkit.geometry.Point;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AllSpotsFragment extends Fragment {

    private FragmentAllSpotsBinding binding;

    private RecyclerView allSpotsRecyclerView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MapKitFactory.initialize(this.requireContext());
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentAllSpotsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        allSpotsRecyclerView = binding.recyclerViewAllSpots;
        allSpotsRecyclerView.setLayoutManager(new LinearLayoutManager(requireActivity()));
        allSpotsRecyclerView
                .addItemDecoration(new DividerItemDecoration(allSpotsRecyclerView.getContext(),
                        DividerItemDecoration.VERTICAL));

        YandexMapManager.getInstance().setMapView(binding.mapviewAllSpots, requireActivity());

        getSpots();

        return root;
    }

    private void getSpots() {
        SharedPreferences preferences =
                android.preference.PreferenceManager.getDefaultSharedPreferences(getContext());
        String serverURL = preferences.getString("URL", "");

        RetrofitService retrofitService = new RetrofitService(serverURL);

        binding.progressBarAllSpots.setVisibility(View.VISIBLE);

        // Создание API для совершения запроса к серверу.
        SpotAPI spotAPI = retrofitService.getRetrofit().create(SpotAPI.class);

        spotAPI.getAllSpots()
                .enqueue(new Callback<List<Spot>>() {
                    @Override
                    public void onResponse(@NonNull Call<List<Spot>> call,
                                           @NonNull Response<List<Spot>> response) {
                        if (response.isSuccessful()) {
                            if (isResponseBodyNotEmpty(response.body())) {
                                requireActivity().runOnUiThread(() ->
                                        binding.progressBarAllSpots.setVisibility(View.GONE));
                                showSpotsOnMap(response.body());
                                setRecyclerView(response.body());
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

    private void showSpotsOnMap(List<Spot> spots) {
        YandexMapManager mapManager = YandexMapManager.getInstance();
        mapManager.setMapView(binding.mapviewAllSpots, requireActivity());
        mapManager.addPlacemarks(spots,
                (AppCompatActivity) requireActivity(), R.id.fragment_container_all_spots);
    }

    private void setRecyclerView(List<Spot> spots) {
        requireActivity().runOnUiThread(() ->
                allSpotsRecyclerView.setAdapter(new SpotAdapter(requireActivity(),
                        R.id.fragment_container_all_spots, spots)));
    }

    private boolean isResponseBodyNotEmpty(List<Spot> spots) {
        return spots != null && spots.size() != 0;
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

        UIManager.showListMapImageButton(requireActivity().findViewById(R.id.ib_list_map));

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