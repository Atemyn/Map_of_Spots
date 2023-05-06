package com.example.mapofspotsdrawer.ui.auth.login;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.mapofspotsdrawer.databinding.FragmentLoginBinding;
import com.example.mapofspotsdrawer.ui.auth.validation.PasswordTextWatcher;

public class LoginFragment extends Fragment {

    FragmentLoginBinding binding;
    public LoginFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentLoginBinding.inflate(inflater, container, false);

        binding.etPassword.addTextChangedListener(new PasswordTextWatcher(binding.etPassword));

        return binding.getRoot();
    }
}