package com.example.mapofspotsdrawer.ui.profile;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.preference.PreferenceManager;

import com.example.mapofspotsdrawer.R;
import com.example.mapofspotsdrawer.databinding.FragmentProfileBinding;
import com.example.mapofspotsdrawer.ui.auth.AuthFragment;

public class ProfileFragment extends Fragment {

    private FragmentProfileBinding binding;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentProfileBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        if (isLoggedIn()) {
            showProfileDataFragment();
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
        // TODO: Добавить сравнение токена из SharedPreferences и с сервера.
        // Проверка, авторизован ли пользователь
        String token = PreferenceManager.getDefaultSharedPreferences(requireActivity())
                .getString("jwtToken", null);
        return token != null && !token.isEmpty();
    }

    private void showLoginFragment() {
        Bundle fragmentProfileIndicator = new Bundle();
        fragmentProfileIndicator.putString(getString(R.string.fragment_indicator_key),
                getString(R.string.fragment_profile_indicator));

        AuthFragment authFragment = new AuthFragment();
        authFragment.setArguments(fragmentProfileIndicator);

        requireActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, authFragment)
                .commit();
    }

    private void showProfileDataFragment() {
        requireActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, new ProfileDataFragment())
                .commit();
    }
}