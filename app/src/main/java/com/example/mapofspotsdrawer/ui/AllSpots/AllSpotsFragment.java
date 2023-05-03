package com.example.mapofspotsdrawer.ui.AllSpots;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.mapofspotsdrawer.databinding.FragmentAllSpotsBinding;

public class AllSpotsFragment extends Fragment {

    private FragmentAllSpotsBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        AllSpotsViewModel allSpotsViewModel =
                new ViewModelProvider(this).get(AllSpotsViewModel.class);

        binding = FragmentAllSpotsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final TextView textView = binding.textAllSpots;
        allSpotsViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}