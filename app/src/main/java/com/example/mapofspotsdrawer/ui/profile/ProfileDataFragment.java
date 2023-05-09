package com.example.mapofspotsdrawer.ui.profile;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.preference.PreferenceManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.mapofspotsdrawer.R;
import com.example.mapofspotsdrawer.api.UserAPI;
import com.example.mapofspotsdrawer.databinding.FragmentProfileDataBinding;
import com.example.mapofspotsdrawer.model.User;
import com.example.mapofspotsdrawer.retrofit.RetrofitService;
import com.example.mapofspotsdrawer.ui.auth.AuthFragment;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileDataFragment extends Fragment {

    private FragmentProfileDataBinding binding;

    private RetrofitService retrofitService;

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

        retrofitService = new RetrofitService(getString(R.string.server_url));

        getUserInfo();

        return binding.getRoot();
    }

    private void getUserInfo() {

        String bearer = "Bearer " + PreferenceManager.getDefaultSharedPreferences(requireActivity())
                .getString("jwtToken", null);

        binding.progressBar.setVisibility(View.VISIBLE);

        // Создание API для совершения запроса к серверу.
        UserAPI userAPI = retrofitService.getRetrofit().create(UserAPI.class);

        userAPI.getUserInfo(bearer)
                .enqueue(new Callback<User>() {
                    @Override
                    public void onResponse(@NonNull Call<User> call,
                                           @NonNull Response<User> response) {
                        if (response.isSuccessful()) {
                            User user = response.body();
                            if (user == null) {
                                disableProgressBarAndShowNotification("Ошибка получения тела ответа");
                                return;
                            }

                            setUserInfoTextViews(user);
                            requireActivity().runOnUiThread(() -> {
                                binding.progressBar.setVisibility(View.GONE);
                            });
                        }
                        else {
                            disableProgressBarAndShowNotification("Ошибка обработки запроса на сервере");
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<User> call,
                                          @NonNull Throwable t) {
                        disableProgressBarAndShowNotification("Ошибка отправки запроса на сервер");
                    }
                });
    }

    private void setUserInfoTextViews(User user) {
        String name = user.getName();
        if (name != null && !name.isEmpty()) {
            binding.tvName.setText(name);
        }

        String email = user.getEmail();
        if (email != null && !email.isEmpty()) {
            binding.tvEmail.setText(email);
        }

        String phone = user.getPhone();
        if (phone != null && !phone.isEmpty()) {
            binding.tvPhoneNumber.setText(phone);
        }

        DateTimeFormatter formatter
                = DateTimeFormatter.ofPattern("dd.MM.yyyy", Locale.getDefault());

        if (user.getBirthday() != null) {
            LocalDate birthday = LocalDate.parse(user.getBirthday());
            binding.tvBirthDate.setText(birthday.format(formatter));
        }

        if (user.getRegistrationDate() != null) {
            LocalDate registrationDate = LocalDate.parse(user.getRegistrationDate());
            binding.tvRegistrationDate.setText(registrationDate.format(formatter));
        }
    }

    private void disableProgressBarAndShowNotification(String message) {
        requireActivity().runOnUiThread(() ->
                binding.progressBar.setVisibility(View.GONE));
        Toast.makeText(getActivity(),
                message, Toast.LENGTH_LONG).show();
    }

    public void showLoginFragment() {
        requireActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, new AuthFragment(), "caller_fragment_tag")
                .commit();
    }

}