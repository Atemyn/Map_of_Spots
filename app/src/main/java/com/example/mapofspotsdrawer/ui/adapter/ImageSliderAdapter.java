package com.example.mapofspotsdrawer.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.mapofspotsdrawer.R;

import java.util.List;

public class ImageSliderAdapter extends RecyclerView.Adapter<ImageSliderAdapter.SliderViewHolder> {

    private Context context;

    private List<String> imagesUrls;

    private int currentIndex;

    public ImageSliderAdapter(Context context, List<String> imagesUrls) {
        this.context = context;
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
        Glide.with(holder.itemView.getContext())
                .load(imagesUrls.get(position))
                .into(holder.imageView);
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
