package com.example.mapofspotsdrawer.ui.about;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.mapofspotsdrawer.databinding.FragmentAboutBinding;
import com.example.mapofspotsdrawer.ui.spot.SpotInfoFragment;

public class AboutFragment extends Fragment {

    private FragmentAboutBinding binding;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentAboutBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        binding.btnSpotInfoTest.setOnClickListener(view -> {
            binding.btnSpotInfoTest.setVisibility(View.GONE);
            requireActivity().getSupportFragmentManager().beginTransaction()
                    .replace(binding.fragmentContainer.getId(), new SpotInfoFragment())
                    .commit();
        });

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}