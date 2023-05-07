package com.example.mapofspotsdrawer.ui.profile;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.mapofspotsdrawer.R;
import com.example.mapofspotsdrawer.databinding.FragmentProfileDataBinding;

public class ProfileDataFragment extends Fragment {

    private FragmentProfileDataBinding binding;

    public static ProfileDataFragment newInstance() {
        return new ProfileDataFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_profile_data, container, false);
    }

}