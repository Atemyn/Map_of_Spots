package com.example.mapofspotsdrawer.ui.auth.login;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.preference.PreferenceManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.mapofspotsdrawer.R;
import com.example.mapofspotsdrawer.api.AuthAPI;
import com.example.mapofspotsdrawer.databinding.FragmentLoginBinding;
import com.example.mapofspotsdrawer.retrofit.RetrofitService;
import com.example.mapofspotsdrawer.ui.auth.validation.AuthValidator;
import com.example.mapofspotsdrawer.ui.auth.validation.EmailTextWatcher;
import com.example.mapofspotsdrawer.ui.auth.validation.PasswordTextWatcher;
import com.example.mapofspotsdrawer.ui.create_spot.CreateSpotInfoFragment;
import com.example.mapofspotsdrawer.ui.favorite.FavoriteInfoFragment;
import com.example.mapofspotsdrawer.ui.profile.ProfileDataFragment;
import com.google.gson.Gson;
import com.google.gson.JsonElement;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginFragment extends Fragment {

    private FragmentLoginBinding binding;

    private RetrofitService retrofitService;

    public LoginFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentLoginBinding.inflate(inflater, container, false);

        // Установка TextWatcher'ов для валидации полей.
        binding.etEmail.addTextChangedListener(new EmailTextWatcher(binding.etEmail));
        binding.etPassword.addTextChangedListener(new PasswordTextWatcher(binding.etPassword));

        retrofitService = new RetrofitService(getString(R.string.server_url));

        binding.btnLogin.setOnClickListener(view -> authorizeUser(getArguments()));

        return binding.getRoot();
    }

    private void authorizeUser(Bundle callerFragmentArgument) {
        AuthValidator authValidator = new AuthValidator(binding.etEmail, binding.etPassword);

        if(!authValidator.isAuthorizationDataValid()) {
            disableProgressBarAndShowNotification("Введенные данные неверны");
            return;
        }

        try {
            RequestBody requestBody =
                    RequestBody.create(MediaType.parse("application/json"),
                            createJSONObject().toString());

            binding.progressBar.setVisibility(View.VISIBLE);

            // Создание API для совершения запроса к серверу.
            AuthAPI authAPI = retrofitService.getRetrofit().create(AuthAPI.class);

            authAPI.loginUser(requestBody)
                    .enqueue(new Callback<ResponseBody>() {
                        @Override
                        public void onResponse(@NonNull Call<ResponseBody> call,
                                               @NonNull Response<ResponseBody> response) {
                            if (response.isSuccessful()) {
                                try {
                                    String jwtToken = processResponseBody(response);
                                    if (jwtToken == null) {
                                        return;
                                    }

                                    requireActivity().runOnUiThread(() -> {
                                        binding.progressBar.setVisibility(View.GONE);
                                        showAuthorizedInfoFragment(callerFragmentArgument);
                                    });


                                } catch (IOException e) {
                                    disableProgressBarAndShowNotification("Ошибка получения тела ответа");
                                }
                            } else {
                                disableProgressBarAndShowNotification("Ошибка обработки запроса на сервере");
                            }
                        }

                        @Override
                        public void onFailure(@NonNull Call<ResponseBody> call,
                                              @NonNull Throwable t) {
                            disableProgressBarAndShowNotification("Ошибка отправки запроса на сервер");
                        }
                    });
        }
        catch (JSONException e) {
            disableProgressBarAndShowNotification("Ошибка формирования запроса к серверу");
        }
    }

    private JSONObject createJSONObject() throws JSONException {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("email", binding.etEmail.getText().toString());
        jsonObject.put("password", binding.etPassword.getText().toString());

        return jsonObject;
    }

    private String processResponseBody(Response<ResponseBody> response) throws IOException {
        if (response.body() == null) {
            disableProgressBarAndShowNotification("Ошибка получения тела ответа");
            return null;
        }
        String responseBody = response.body().string();

        JsonElement jsonElement = new Gson().fromJson(responseBody, JsonElement.class);
        if (jsonElement.getAsJsonObject().get("jwtToken") == null) {
            if (jsonElement.getAsJsonObject().get("message") == null) {
                disableProgressBarAndShowNotification("Ошибка получения токена авторизации");
            }
            else {
                disableProgressBarAndShowNotification("Введенные данные неверны");
            }
            return null;
        }
        String jwtToken = jsonElement.getAsJsonObject().get("jwtToken").getAsString();

        // Запись jwtToken'а в SharedPreferences.
        PreferenceManager.getDefaultSharedPreferences(requireActivity())
                .edit().putString("jwtToken", jwtToken).apply();

        return jwtToken;
    }

    private void disableProgressBarAndShowNotification(String message) {
        requireActivity().runOnUiThread(() ->
                binding.progressBar.setVisibility(View.GONE));
        Toast.makeText(getActivity(),
                message, Toast.LENGTH_LONG).show();
    }

    private void showAuthorizedInfoFragment(Bundle callerFragmentArgument) {
        if (callerFragmentArgument != null) {
            String fragmentCallerIndicatorString =
                    callerFragmentArgument.getString(getString(R.string.fragment_indicator_key));
            if (fragmentCallerIndicatorString.equals(getString(R.string.fragment_profile_indicator))) {
                requireActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container_profile, new ProfileDataFragment())
                        .commit();
            } else if (fragmentCallerIndicatorString.equals(getString(R.string.fragment_favorite_indicator))) {
                requireActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container_favorite, new FavoriteInfoFragment())
                        .commit();
            } else if (fragmentCallerIndicatorString
                    .equals(getString(R.string.fragment_create_spot_indicator))) {
                requireActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container_create_spot, new CreateSpotInfoFragment())
                        .commit();
            }
        }
    }
}