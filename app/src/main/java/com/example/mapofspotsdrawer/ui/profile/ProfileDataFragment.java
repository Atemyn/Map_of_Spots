package com.example.mapofspotsdrawer.ui.profile;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.preference.PreferenceManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.mapofspotsdrawer.R;
import com.example.mapofspotsdrawer.databinding.FragmentProfileDataBinding;
import com.example.mapofspotsdrawer.ui.auth.AuthFragment;

public class ProfileDataFragment extends Fragment {

    private FragmentProfileDataBinding binding;

    public static ProfileDataFragment newInstance() {
        return new ProfileDataFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentProfileDataBinding.inflate(inflater, container, false);

        binding.btnSignOut.setOnClickListener(view -> {
            // Обнуление jwtToken'а в SharedPreferences.
            PreferenceManager.getDefaultSharedPreferences(requireActivity())
                    .edit().putString("jwtToken", null).apply();
            showLoginFragment();
        });

        return binding.getRoot();
    }

    public void showLoginFragment() {
        requireActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, new AuthFragment(), "caller_fragment_tag")
                .commit();
    }

}