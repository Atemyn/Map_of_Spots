package com.example.mapofspotsdrawer.ui.adapter.image_slider;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mapofspotsdrawer.R;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class ResponseBodyImageSliderAdapter extends RecyclerView.Adapter<ResponseBodyImageSliderAdapter.SliderViewHolder> {

    private final FragmentActivity activity;

    private final List<String> imagesUrls;

    private int currentIndex;

    public ResponseBodyImageSliderAdapter(FragmentActivity activity, List<String> imagesUrls) {
        this.activity = activity;
        this.imagesUrls = imagesUrls;
    }

    @NonNull
    @Override
    public SliderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View slideLayout = inflater.inflate(R.layout.slide_layout, parent, false);
        return new SliderViewHolder(slideLayout);
    }

    @Override
    public void onBindViewHolder(@NonNull SliderViewHolder holder, int position) {
        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url("http://" + imagesUrls.get(position))
                .build();


        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call,
                                  @NonNull IOException e) {
                activity.runOnUiThread(() -> Toast.makeText(activity,
                        "Ошибка совершения запроса к серверу", Toast.LENGTH_LONG).show());
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
                    activity.runOnUiThread(() -> Toast.makeText(activity,
                            "Ошибка получения фото спота с сервера", Toast.LENGTH_LONG).show());
                }
            }
        });
    }


    @Override
    public int getItemCount() {
        return imagesUrls.size();
    }

    public void setCurrentIndex(int currentIndex) {
        this.currentIndex = currentIndex;
    }

    static class SliderViewHolder extends RecyclerView.ViewHolder {

        ImageView imageView;

        SliderViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.image_slide);
        }
    }
}
