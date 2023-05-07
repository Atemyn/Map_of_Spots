package com.example.mapofspotsdrawer.ui.auth.register;

import android.app.DatePickerDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.preference.PreferenceManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.example.mapofspotsdrawer.R;
import com.example.mapofspotsdrawer.api.AuthAPI;
import com.example.mapofspotsdrawer.databinding.FragmentRegisterBinding;
import com.example.mapofspotsdrawer.retrofit.RetrofitService;
import com.example.mapofspotsdrawer.ui.auth.AuthFragment;
import com.example.mapofspotsdrawer.ui.auth.validation.AuthValidator;
import com.example.mapofspotsdrawer.ui.auth.validation.BirthDateTextWatcher;
import com.example.mapofspotsdrawer.ui.auth.validation.EmailTextWatcher;
import com.example.mapofspotsdrawer.ui.auth.validation.NameTextWatcher;
import com.example.mapofspotsdrawer.ui.auth.validation.PasswordTextWatcher;
import com.example.mapofspotsdrawer.ui.auth.validation.PhoneNumberTextWatcher;
import com.google.gson.Gson;
import com.google.gson.JsonElement;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.Objects;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterFragment extends Fragment {

    FragmentRegisterBinding binding;

    private RetrofitService retrofitService;
    private Calendar date;

    public RegisterFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentRegisterBinding.inflate(inflater, container, false);

        date = Calendar.getInstance();

        // Установка TextWatcher'ов для валидации полей.
        binding.etName.addTextChangedListener(new NameTextWatcher(binding.etName));
        binding.etEmail.addTextChangedListener(new EmailTextWatcher(binding.etEmail));
        binding.etPhoneNumber.addTextChangedListener(new PhoneNumberTextWatcher(binding.etPhoneNumber));
        binding.etBirthDate.addTextChangedListener(new BirthDateTextWatcher(binding.etBirthDate));
        binding.etPassword.addTextChangedListener(new PasswordTextWatcher(binding.etPassword));
        binding.etRepassword.addTextChangedListener(new PasswordTextWatcher(binding.etRepassword));
        View.OnClickListener editTextOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(),
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                                date.set(year, month, dayOfMonth);
                                month++;
                                String textDay = dayOfMonth < 10 ? "0" + dayOfMonth : String.valueOf(dayOfMonth);
                                String textMonth = month < 10 ? "0" + month : String.valueOf(month);
                                String textDate = textDay + "." + textMonth + "." + year;
                                EditText currentEditText = (EditText) v;
                                currentEditText.setText(textDate);
                            }
                        }, date.get(Calendar.YEAR), date.get(Calendar.MONTH), date.get(Calendar.DAY_OF_MONTH));
                datePickerDialog.show();
            }
        };

        binding.etBirthDate.setOnClickListener(editTextOnClickListener);

        retrofitService = new RetrofitService(getString(R.string.server_url));

        return binding.getRoot();
    }

    private void registerUser() {
        AuthValidator authValidator = new AuthValidator(binding.etName, binding.etEmail,
                binding.etPhoneNumber, binding.etBirthDate,
                binding.etPassword, binding.etRepassword);

        if(!authValidator.isRegistrationDataValid()) {
            Toast.makeText(getActivity(), "Введенные данные неверны", Toast.LENGTH_LONG).show();
            return;
        }

        try {
            RequestBody requestBody =
                    RequestBody.create(MediaType.parse("application/json"),
                            createJSONObject().toString());

            // Создание API для совершения запроса к серверу.
            AuthAPI authAPI = retrofitService.getRetrofit().create(AuthAPI.class);

            authAPI.registerUser(requestBody)
                    .enqueue(new Callback<ResponseBody>() {
                        @Override
                        public void onResponse(@NonNull Call<ResponseBody> call,
                                               @NonNull Response<ResponseBody> response) {
                            if (response.isSuccessful()) {
                                try {
                                    if (response.body() == null) {
                                        Toast.makeText(getActivity(),
                                                "Ошибка получения тела ответа", Toast.LENGTH_LONG).show();
                                        return;
                                    }
                                    String responseBody = response.body().string();

                                    JsonElement jsonElement = new Gson().fromJson(responseBody, JsonElement.class);
                                    String jwtToken = jsonElement.getAsJsonObject().get("jwtToken").getAsString();

                                    // Запись jwtToken'а в SharedPreferences.
                                    PreferenceManager.getDefaultSharedPreferences(requireActivity())
                                            .edit().putString("jwtToken", jwtToken).apply();

                                } catch (IOException e) {
                                    Toast.makeText(getActivity(),
                                            "Ошибка получения тела ответа", Toast.LENGTH_LONG).show();
                                }
                            } else {
                                Toast.makeText(getActivity(),
                                        "Ошибка обработки запроса на сервере", Toast.LENGTH_LONG).show();
                            }
                        }

                        @Override
                        public void onFailure(@NonNull Call<ResponseBody> call,
                                              @NonNull Throwable t) {
                            Toast.makeText(getActivity(), "Ошибка обращения к серверу", Toast.LENGTH_LONG).show();
                        }
                    });
        }
        catch (JSONException e) {
            Toast.makeText(getActivity(), "Ошибка формирования запроса к серверу", Toast.LENGTH_LONG).show();
        }
    }

    private JSONObject createJSONObject() throws JSONException {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("name", binding.etName.getText().toString());
        jsonObject.put("email", binding.etEmail.getText().toString());
        jsonObject.put("phoneNumber", binding.etPhoneNumber.getText().toString());
        jsonObject.put("birthday", binding.etBirthDate.getText().toString().replaceAll("\\.", "-"));
        jsonObject.put("password", binding.etPassword.getText().toString());

        return jsonObject;
    }
}