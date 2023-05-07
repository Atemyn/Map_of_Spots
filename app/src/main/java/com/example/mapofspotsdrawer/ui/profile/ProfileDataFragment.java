package com.example.mapofspotsdrawer.ui.profile;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.mapofspotsdrawer.databinding.FragmentProfileDataBinding;

public class ProfileDataFragment extends Fragment {

    private FragmentProfileDataBinding binding;

    public static ProfileDataFragment newInstance() {
        return new ProfileDataFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentProfileDataBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

}