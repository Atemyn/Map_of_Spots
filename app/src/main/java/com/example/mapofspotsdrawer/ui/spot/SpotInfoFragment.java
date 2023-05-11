package com.example.mapofspotsdrawer.ui.spot;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.mapofspotsdrawer.R;
import com.example.mapofspotsdrawer.databinding.FragmentRegisterBinding;
import com.example.mapofspotsdrawer.databinding.FragmentSpotInfoBinding;
import com.example.mapofspotsdrawer.ui.adapter.ImageSliderAdapter;

public class SpotInfoFragment extends Fragment {

    private FragmentSpotInfoBinding binding;

    private SpotInfoViewModel spotInfoViewModel;

    private int[] images =
            {R.drawable.test_image_1, R.drawable.test_image_2,
                    R.drawable.test_image_3, R.drawable.test_image_4};

    public static SpotInfoFragment newInstance() {
        return new SpotInfoFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentSpotInfoBinding.inflate(inflater, container, false);

        ImageSliderAdapter imageSliderAdapter = new ImageSliderAdapter(getContext(), images);
        binding.imageSlider.setAdapter(imageSliderAdapter);

        binding.indicator.setViewPager(binding.imageSlider);

        imageSliderAdapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onChanged() {
                super.onChanged();
                binding.indicator.setViewPager(binding.imageSlider);
                imageSliderAdapter.setCurrentIndex(binding.imageSlider.getCurrentItem());
            }
        });

        binding.imageSlider.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                imageSliderAdapter.setCurrentIndex(position);
            }
        });

        imageSliderAdapter.setCurrentIndex(binding.imageSlider.getCurrentItem());

        return binding.getRoot();
    }

}