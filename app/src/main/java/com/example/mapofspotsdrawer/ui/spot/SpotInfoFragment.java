package com.example.mapofspotsdrawer.ui.spot;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.mapofspotsdrawer.R;

public class SpotInfoFragment extends Fragment {

    private SpotInfoViewModel mViewModel;

    public static SpotInfoFragment newInstance() {
        return new SpotInfoFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_spot_info, container, false);
    }

}