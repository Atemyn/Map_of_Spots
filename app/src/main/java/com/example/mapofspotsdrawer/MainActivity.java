package com.example.mapofspotsdrawer;

import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Toast;

import com.example.mapofspotsdrawer.api.SpaceTypeAPI;
import com.example.mapofspotsdrawer.api.SportTypeAPI;
import com.example.mapofspotsdrawer.api.SpotTypeAPI;
import com.example.mapofspotsdrawer.databinding.ActivityMainBinding;
import com.example.mapofspotsdrawer.map.YandexMapManager;
import com.example.mapofspotsdrawer.model.SpaceType;
import com.example.mapofspotsdrawer.model.SportType;
import com.example.mapofspotsdrawer.model.SpotType;
import com.example.mapofspotsdrawer.retrofit.RetrofitService;
import com.google.android.material.navigation.NavigationView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.yandex.mapkit.mapview.MapView;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        YandexMapManager.getInstance().setApiKey(getResources().getString(R.string.mapkit_api_key));

        ActivityMainBinding binding = ActivityMainBinding.inflate(getLayoutInflater());
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

        binding.appBarMain.ibListMap.setTag(R.drawable.icon_show_list);
        binding.appBarMain.ibListMap.setOnClickListener(view -> {
            NavDestination navDestination = navController.getCurrentDestination();
            ActionBar actionBar = getSupportActionBar();
            if (navDestination != null && navDestination.getLabel() != null &&
                    actionBar != null && actionBar.getTitle() != null &&
                    !actionBar.getTitle().toString().equals(getString(R.string.spot_info_app_bar_title))) {
                MapView mapView;
                RecyclerView recyclerView;
                if (navDestination.getLabel().toString().equals(getString(R.string.menu_all_spots))) {
                    mapView = findViewById(R.id.mapview_all_spots);
                    recyclerView = findViewById(R.id.recycler_view_all_spots);
                } else if (navDestination.getLabel().toString().equals(getString(R.string.menu_favorite))) {
                    mapView = findViewById(R.id.mapview_favorite_spots);
                    recyclerView = findViewById(R.id.recycler_view_favorite_spots);
                } else if (navDestination.getLabel().toString().equals(getString(R.string.menu_nearby))) {
                    mapView = findViewById(R.id.mapview_nearby_spots);
                    recyclerView = findViewById(R.id.recycler_view_nearby_spots);
                } else {
                    return;
                }
                if (mapView != null && recyclerView != null) {
                    if (mapView.getVisibility() == View.GONE) {
                        mapView.setVisibility(View.VISIBLE);
                        recyclerView.setVisibility(View.GONE);
                    } else {
                        mapView.setVisibility(View.GONE);
                        recyclerView.setVisibility(View.VISIBLE);
                    }
                    if ((int) binding.appBarMain.ibListMap.getTag() == R.drawable.icon_show_list) {
                        binding.appBarMain.ibListMap.setImageResource(R.drawable.icon_show_map);
                        binding.appBarMain.ibListMap.setTag(R.drawable.icon_show_map);
                    }
                    else {
                        binding.appBarMain.ibListMap.setImageResource(R.drawable.icon_show_list);
                        binding.appBarMain.ibListMap.setTag(R.drawable.icon_show_list);
                    }
                }
            }
        });

        // Получение всех справочников (типы спотов, типы спорта, типы помещений).
        getSpotTypes();
    }

    private void getSpotTypes() {
        RetrofitService retrofitService = new RetrofitService(getString(R.string.server_url));

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
                                showNotification("Ошибка получения тела ответа");
                            }
                        }
                        else {
                            showNotification("Ошибка обработки запроса на сервере");
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<List<SpotType>> call,
                                          @NonNull Throwable t) {
                        showNotification("Ошибка отправки запроса на сервер");
                    }
                });
    }

    private void getSportTypes(RetrofitService retrofitService) {
        // Создание API для совершения запроса к серверу.
        SportTypeAPI spotTypeAPI = retrofitService.getRetrofit().create(SportTypeAPI.class);

        spotTypeAPI.getAllSportTypes()
                .enqueue(new Callback<List<SportType>>() {
                    @Override
                    public void onResponse(@NonNull Call<List<SportType>> call,
                                           @NonNull Response<List<SportType>> response) {
                        if (response.isSuccessful()) {
                            if(processSportTypesResponseBody(response.body())) {
                                getSpaceTypes(retrofitService);
                            }
                            else {
                                showNotification("Ошибка получения тела ответа");
                            }
                        }
                        else {
                            showNotification("Ошибка обработки запроса на сервере");
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<List<SportType>> call,
                                          @NonNull Throwable t) {
                        showNotification("Ошибка отправки запроса на сервер");
                    }
                });
    }

    private void getSpaceTypes(RetrofitService retrofitService) {
        // Создание API для совершения запроса к серверу.
        SpaceTypeAPI spotTypeAPI = retrofitService.getRetrofit().create(SpaceTypeAPI.class);

        spotTypeAPI.getAllSpaceTypes()
                .enqueue(new Callback<List<SpaceType>>() {
                    @Override
                    public void onResponse(@NonNull Call<List<SpaceType>> call,
                                           @NonNull Response<List<SpaceType>> response) {
                        if (response.isSuccessful()) {
                            if(!processSpaceTypesResponseBody(response.body())) {
                                showNotification("Ошибка получения тела ответа");
                            }
                        }
                        else {
                            showNotification("Ошибка обработки запроса на сервере");
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<List<SpaceType>> call,
                                          @NonNull Throwable t) {
                        showNotification("Ошибка отправки запроса на сервер");
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

    private boolean processSpaceTypesResponseBody(List<SpaceType> spaceTypes) {
        if (spaceTypes != null && spaceTypes.size() != 0) {
            Gson gson = new Gson();
            String json = gson.toJson(spaceTypes);

            PreferenceManager.getDefaultSharedPreferences(this)
                    .edit().putString("space_types", json).apply();

            return true;
        }
        return false;
    }


    private void showNotification(String message) {
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