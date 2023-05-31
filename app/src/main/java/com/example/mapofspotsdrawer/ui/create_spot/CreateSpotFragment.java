package com.example.mapofspotsdrawer.ui.create_spot;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.preference.PreferenceManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.mapofspotsdrawer.R;
import com.example.mapofspotsdrawer.databinding.FragmentCreateSpotBinding;
import com.example.mapofspotsdrawer.ui.auth.AuthFragment;
import com.example.mapofspotsdrawer.ui.manager.UIManager;

public class CreateSpotFragment extends Fragment {
    private FragmentCreateSpotBinding binding;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentCreateSpotBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        UIManager.hideListMapImageButton(requireActivity().findViewById(R.id.ib_list_map));

        if (isLoggedIn()) {
            showCreateSpotInfoFragment();
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
                getString(R.string.fragment_create_spot_indicator));

        AuthFragment authFragment = new AuthFragment();
        authFragment.setArguments(fragmentProfileIndicator);

        requireActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container_create_spot, authFragment)
                .commit();
    }

    private void showCreateSpotInfoFragment() {
        requireActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container_create_spot,
                        new CreateSpotInfoFragment())
                .commit();
    }

}