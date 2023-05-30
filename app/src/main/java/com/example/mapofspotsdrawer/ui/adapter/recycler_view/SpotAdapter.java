package com.example.mapofspotsdrawer.ui.adapter.recycler_view;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mapofspotsdrawer.R;
import com.example.mapofspotsdrawer.model.Spot;
import com.example.mapofspotsdrawer.ui.spot.SpotInfoFragment;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class SpotAdapter extends RecyclerView.Adapter<SpotAdapter.SpotViewHolder> {

    private final FragmentActivity activity;

    private final int fragmentContainerId;

    private final List<Spot> spots;

    public SpotAdapter(FragmentActivity activity, int fragmentContainerId, List<Spot> spots) {
        this.activity = activity;
        this.fragmentContainerId = fragmentContainerId;
        this.spots = spots;
    }

    @NonNull
    @Override
    public SpotViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(
                parent.getContext()).inflate(R.layout.list_spot_item, parent, false);
        return new SpotViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull SpotViewHolder holder, int position) {
        Spot currentSpot = spots.get(position);

        // Установка названия спота.
        holder.nameTextView.setText(currentSpot.getName());

        // Установка даты добавления спота.
        DateTimeFormatter formatter
                = DateTimeFormatter.ofPattern("dd.MM.yyyy", Locale.getDefault());
        LocalDate addingDate =
                currentSpot.getAddingDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        holder.dateTextView.setText(addingDate.format(formatter));

        holder.layout.setOnClickListener(view -> {
            SpotInfoFragment spotInfoFragment = new SpotInfoFragment();

            Bundle bundle = new Bundle();
            bundle.putSerializable("spot", currentSpot);

            spotInfoFragment.setArguments(bundle);

            hideAllViewsOnAllSpotsFragment((AppCompatActivity)  activity);

            activity.getSupportFragmentManager().beginTransaction()
                    .add(fragmentContainerId, spotInfoFragment)
                    .commit();
        });

        if (currentSpot.getImageInfoDtoList().size() == 0) {
            return;
        }

        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url("http://" + currentSpot.getImageInfoDtoList().get(0).getUrl())
                .build();

        // Установка первой фотографии спота.
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call,
                                  @NonNull IOException e) {
                Toast.makeText(activity,
                        "Ошибка совершения запроса к серверу", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onResponse(@NonNull Call call,
                                   @NonNull Response response) {
                if (response.isSuccessful()) {
                    assert response.body() != null;
                    InputStream inputStream = response.body().byteStream();
                    Bitmap bitmap = BitmapFactory.decodeStream(inputStream);

                    activity.runOnUiThread(() -> holder.imageView.setImageBitmap(bitmap));
                }
                else {
                    Toast.makeText(activity,
                            "Ошибка получения фото спота с сервера", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void hideAllViewsOnAllSpotsFragment(AppCompatActivity activity) {
        View recyclerView = null, progressBar = null;

        Objects.requireNonNull(
                activity.getSupportActionBar()).setTitle(R.string.spot_info_app_bar_title);

        if (fragmentContainerId == R.id.fragment_container_all_spots) {
            recyclerView = activity.findViewById(R.id.recycler_view_all_spots);
            progressBar = activity.findViewById(R.id.progressBar_all_spots);
        }
        else if (fragmentContainerId == R.id.fragment_container_favorite_spots) {
            recyclerView = activity.findViewById(R.id.recycler_view_favorite_spots);
            progressBar = activity.findViewById(R.id.progressBar_favorite_spots);
        }
        else if (fragmentContainerId == R.id.fragment_container_nearby_spots) {
            recyclerView = activity.findViewById(R.id.recycler_view_nearby_spots);
            progressBar = activity.findViewById(R.id.progressBar_nearby_spots);
        }

        if (recyclerView != null && progressBar != null) {
            recyclerView.setVisibility(View.GONE);
            progressBar.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return spots.size();
    }

    public static class SpotViewHolder extends RecyclerView.ViewHolder {
        public CircleImageView imageView;
        public TextView nameTextView;
        public TextView dateTextView;

        public ConstraintLayout layout;

        public SpotViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.civ_spot_item);
            nameTextView = itemView.findViewById(R.id.tv_name_spot_item);
            dateTextView = itemView.findViewById(R.id.tv_adding_date_spot_item);
            layout = itemView.findViewById(R.id.spot_item_layout);
        }
    }
}
