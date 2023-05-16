package com.example.mapofspotsdrawer.ui.profile;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.preference.PreferenceManager;

import android.text.Editable;
import android.text.TextWatcher;
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

    private ProfileDataViewModel profileDataViewModel;

    private RetrofitService retrofitService;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        profileDataViewModel
                = new ViewModelProvider(this).get(ProfileDataViewModel.class);

        retrofitService = new RetrofitService(getString(R.string.server_url));
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentProfileDataBinding.inflate(inflater, container, false);

        addNameTextChangedListener();
        addEmailTextChangedListener();
        addPhoneTextChangedListener();
        addBirthdayTextChangedListener();
        addRegistrationDateTextChangedListener();

        binding.btnSignOut.setOnClickListener(view -> {
            // Обнуление jwtToken'а в SharedPreferences.
            PreferenceManager.getDefaultSharedPreferences(requireActivity())
                    .edit().putString("jwtToken", null).apply();
            showLoginFragment();
        });

        String name = profileDataViewModel.getName();
        String email = profileDataViewModel.getEmail();
        String phone = profileDataViewModel.getPhone();
        String birthday = profileDataViewModel.getBirthday();
        String registrationDate = profileDataViewModel.getRegistrationDate();

        if (name != null && email != null && phone != null
                && birthday != null && registrationDate != null) {
            binding.tvName.setText(name);
            binding.tvEmail.setText(email);
            binding.tvPhoneNumber.setText(phone);
            binding.tvBirthDate.setText(birthday);
            binding.tvRegistrationDate.setText(registrationDate);
        }
        else {
            getUserInfo();
        }

        return binding.getRoot();
    }

    public void addNameTextChangedListener() {
        binding.tvName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    profileDataViewModel.setName(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    public void addEmailTextChangedListener() {
        binding.tvEmail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                profileDataViewModel.setEmail(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    public void addPhoneTextChangedListener() {
        binding.tvPhoneNumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                profileDataViewModel.setPhone(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    public void addBirthdayTextChangedListener() {
        binding.tvBirthDate.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                profileDataViewModel.setBirthday(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    public void addRegistrationDateTextChangedListener() {
        binding.tvRegistrationDate.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                profileDataViewModel.setRegistrationDate(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
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
                            requireActivity().runOnUiThread(()
                                    -> binding.progressBar.setVisibility(View.GONE));
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
            LocalDate birthday =
                    LocalDate.parse(user.getBirthday().split(" ")[0]);
            binding.tvBirthDate.setText(birthday.format(formatter));
        }

        if (user.getRegistrationDate() != null) {
            LocalDate registrationDate =
                    LocalDate.parse(user.getRegistrationDate().split(" ")[0]);
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