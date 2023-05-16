package com.example.mapofspotsdrawer;

import android.os.Bundle;
import android.view.Menu;
import android.widget.Toast;

import com.example.mapofspotsdrawer.api.SportTypeAPI;
import com.example.mapofspotsdrawer.api.SpotTypeAPI;
import com.example.mapofspotsdrawer.map.YandexMapManager;
import com.example.mapofspotsdrawer.model.SportType;
import com.example.mapofspotsdrawer.model.SpotType;
import com.example.mapofspotsdrawer.retrofit.RetrofitService;
import com.google.android.material.navigation.NavigationView;

import androidx.annotation.NonNull;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;

import com.example.mapofspotsdrawer.databinding.ActivityMainBinding;
import com.google.gson.Gson;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        YandexMapManager.getInstance().setApiKey(getResources().getString(R.string.mapkit_api_key));

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.appBarMain.toolbar);

        DrawerLayout drawer = binding.drawerLayout;
        NavigationView navigationView = binding.navView;
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_all_spots, R.id.nav_favorite, R.id.nav_profile, R.id.nav_nearby)
                .setOpenableLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        RetrofitService retrofitService = new RetrofitService(getString(R.string.server_url));
        getSpotTypes(retrofitService);
    }

    private void getSpotTypes(RetrofitService retrofitService) {
        // binding.progressBar.setVisibility(View.VISIBLE);

        // Создание API для совершения запроса к серверу.
        SpotTypeAPI spotTypeAPI = retrofitService.getRetrofit().create(SpotTypeAPI.class);

        spotTypeAPI.getAllSpotTypes()
                .enqueue(new Callback<List<SpotType>>() {
                    @Override
                    public void onResponse(@NonNull Call<List<SpotType>> call,
                                               @NonNull Response<List<SpotType>> response) {
                        if (response.isSuccessful()) {
                            if(processSpotTypesResponseBody(response.body())) {
                                getSportTypes(retrofitService);
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
                    public void onFailure(@NonNull Call<List<SpotType>> call,
                                          @NonNull Throwable t) {
                        disableProgressBarAndShowNotification("Ошибка отправки запроса на сервер");
                    }
                });
    }

    private void getSportTypes(RetrofitService retrofitService) {
        // binding.progressBar.setVisibility(View.VISIBLE);

        // Создание API для совершения запроса к серверу.
        SportTypeAPI spotTypeAPI = retrofitService.getRetrofit().create(SportTypeAPI.class);

        spotTypeAPI.getAllSportTypes()
                .enqueue(new Callback<List<SportType>>() {
                    @Override
                    public void onResponse(@NonNull Call<List<SportType>> call,
                                           @NonNull Response<List<SportType>> response) {
                        if (response.isSuccessful()) {
                            if(processSportTypesResponseBody(response.body())) {

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
                    public void onFailure(@NonNull Call<List<SportType>> call,
                                          @NonNull Throwable t) {
                        disableProgressBarAndShowNotification("Ошибка отправки запроса на сервер");
                    }
                });
    }

    private boolean processSpotTypesResponseBody(List<SpotType> spotTypes) {
        if (spotTypes != null && spotTypes.size() != 0) {
            Gson gson = new Gson();
            String json = gson.toJson(spotTypes);

            PreferenceManager.getDefaultSharedPreferences(this)
                    .edit().putString("spot_types", json).apply();

            return true;
        }
        return false;
    }

    private boolean processSportTypesResponseBody(List<SportType> sportTypes) {
        if (sportTypes != null && sportTypes.size() != 0) {
            Gson gson = new Gson();
            String json = gson.toJson(sportTypes);

            PreferenceManager.getDefaultSharedPreferences(this)
                    .edit().putString("sport_types", json).apply();

            return true;
        }
        return false;
    }


    private void disableProgressBarAndShowNotification(String message) {
/*        requireActivity().runOnUiThread(() ->
                binding.progressBar.setVisibility(View.GONE));*/
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }
}