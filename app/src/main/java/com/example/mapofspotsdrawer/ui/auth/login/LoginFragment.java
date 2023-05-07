package com.example.mapofspotsdrawer.ui.auth.login;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.mapofspotsdrawer.api.AuthAPI;
import com.example.mapofspotsdrawer.databinding.FragmentLoginBinding;
import com.example.mapofspotsdrawer.retrofit.RetrofitService;
import com.example.mapofspotsdrawer.ui.auth.validation.AuthValidator;
import com.example.mapofspotsdrawer.ui.auth.validation.EmailTextWatcher;
import com.example.mapofspotsdrawer.ui.auth.validation.PasswordTextWatcher;

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

    private JSONObject createJSONObject() throws JSONException {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("email", binding.etEmail.getText().toString());
        jsonObject.put("password", binding.etPassword.getText().toString());

        return jsonObject;
    }
}