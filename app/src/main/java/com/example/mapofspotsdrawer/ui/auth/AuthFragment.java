package com.example.mapofspotsdrawer.ui.auth;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.mapofspotsdrawer.databinding.FragmentAuthBinding;
import com.example.mapofspotsdrawer.ui.auth.adapter.AuthPagerAdapter;
import com.example.mapofspotsdrawer.ui.auth.login.LoginFragment;
import com.example.mapofspotsdrawer.ui.auth.register.RegisterFragment;


public class AuthFragment extends Fragment {

    private FragmentAuthBinding binding;

    public AuthFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentAuthBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        AuthPagerAdapter authPagerAdapter
                = new AuthPagerAdapter(getChildFragmentManager(), getLifecycle());
        authPagerAdapter.addFragment(new LoginFragment());
        authPagerAdapter.addFragment(new RegisterFragment());
        binding.viewPager.setAdapter(authPagerAdapter);
        return root;
    }

}