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

        return binding.getRoot();
    }

    private void authorizeUser() {
        AuthValidator authValidator = new AuthValidator(binding.etEmail, binding.etPassword);

        if(!authValidator.isAuthorizationDataValid()) {
            requireActivity().runOnUiThread(() ->
                    binding.progressBar.setVisibility(View.GONE));
            Toast.makeText(getActivity(), "Введенные данные неверны", Toast.LENGTH_LONG).show();
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
                                        showProfileDataFragment();
                                    });


                                } catch (IOException e) {
                                    requireActivity().runOnUiThread(() ->
                                            binding.progressBar.setVisibility(View.GONE));
                                    Toast.makeText(getActivity(),
                                            "Ошибка получения тела ответа", Toast.LENGTH_LONG).show();
                                }
                            } else {
                                requireActivity().runOnUiThread(() ->
                                        binding.progressBar.setVisibility(View.GONE));
                                Toast.makeText(getActivity(),
                                        "Ошибка обработки запроса на сервере", Toast.LENGTH_LONG).show();
                            }
                        }

                        @Override
                        public void onFailure(@NonNull Call<ResponseBody> call,
                                              @NonNull Throwable t) {
                            requireActivity().runOnUiThread(() ->
                                    binding.progressBar.setVisibility(View.GONE));
                            Toast.makeText(getActivity(),
                                    "Ошибка отправки запроса на сервер", Toast.LENGTH_LONG).show();
                        }
                    });
        }
        catch (JSONException e) {
            requireActivity().runOnUiThread(() ->
                    binding.progressBar.setVisibility(View.GONE));
            Toast.makeText(getActivity(), "Ошибка формирования запроса к серверу", Toast.LENGTH_LONG).show();
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
            Toast.makeText(getActivity(),
                    "Ошибка получения тела ответа", Toast.LENGTH_LONG).show();
            return null;
        }
        String responseBody = response.body().string();

        JsonElement jsonElement = new Gson().fromJson(responseBody, JsonElement.class);
        if (jsonElement.getAsJsonObject().get("jwtToken") == null) {
            if (jsonElement.getAsJsonObject().get("message") == null) {
                Toast.makeText(getActivity(),
                        "Ошибка получения токена авторизации", Toast.LENGTH_LONG).show();
            }
            else {
                Toast.makeText(getActivity(),
                        "Пользователя с такими данными не существует",
                        Toast.LENGTH_LONG).show();
            }
            return null;
        }
        String jwtToken = jsonElement.getAsJsonObject().get("jwtToken").getAsString();

        // Запись jwtToken'а в SharedPreferences.
        PreferenceManager.getDefaultSharedPreferences(requireActivity())
                .edit().putString("jwtToken", jwtToken).apply();

        return jwtToken;
    }

    private void showProfileDataFragment() {
        requireActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, new ProfileDataFragment())
                .commit();
    }
}