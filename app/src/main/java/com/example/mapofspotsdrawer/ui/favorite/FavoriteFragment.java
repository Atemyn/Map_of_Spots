package com.example.mapofspotsdrawer.ui.favorite;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.preference.PreferenceManager;

import com.example.mapofspotsdrawer.R;
import com.example.mapofspotsdrawer.databinding.FragmentFavoriteBinding;
import com.example.mapofspotsdrawer.map.YandexMapManager;
import com.example.mapofspotsdrawer.ui.auth.AuthFragment;
import com.example.mapofspotsdrawer.ui.profile.ProfileDataFragment;
import com.yandex.mapkit.MapKitFactory;
import com.yandex.mapkit.geometry.Point;

public class FavoriteFragment extends Fragment {

    private FragmentFavoriteBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentFavoriteBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        if (isLoggedIn()) {
            showFavoriteInfoFragment();
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
                getString(R.string.fragment_favorite_indicator));

        AuthFragment authFragment = new AuthFragment();
        authFragment.setArguments(fragmentProfileIndicator);

        requireActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container_favorite, authFragment)
                .commit();
    }

    private void showFavoriteInfoFragment() {
        requireActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container_favorite, new FavoriteInfoFragment())
                .commit();
    }
}