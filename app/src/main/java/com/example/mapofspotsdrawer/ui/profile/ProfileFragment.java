package com.example.mapofspotsdrawer.ui.profile;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.mapofspotsdrawer.R;
import com.example.mapofspotsdrawer.databinding.FragmentProfileBinding;
import com.example.mapofspotsdrawer.ui.auth.AuthFragment;
import com.example.mapofspotsdrawer.ui.auth.login.LoginFragment;

public class ProfileFragment extends Fragment {

    private FragmentProfileBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentProfileBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        if (isLoggedIn()) {
            showProfileFragment();
        } else {
            showLoginFragment();
        }

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private boolean isLoggedIn() {
        // TODO Добавить проверку токена авторизации в SharedPreferences.
        // Проверка, авторизован ли пользователь
        return false;
    }

    public void showLoginFragment() {
        requireActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, new AuthFragment())
                .commit();
    }

    public void showProfileFragment() {
        requireActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, new ProfileFragment())
                .commit();
    }
}