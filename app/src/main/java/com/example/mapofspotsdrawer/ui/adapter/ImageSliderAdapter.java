package com.example.mapofspotsdrawer.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mapofspotsdrawer.R;

public class ImageSliderAdapter extends RecyclerView.Adapter<ImageSliderAdapter.SliderViewHolder> {

    private Context context;

    private int[] images;

    private int currentIndex;

    public ImageSliderAdapter(Context context, int[] images) {
        this.context = context;
        this.images = images;
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
        holder.imageView.setImageResource(images[position]);
    }

    @Override
    public int getItemCount() {
        return images.length;
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
