package com.example.mapofspotsdrawer.ui.auth.register;

import android.app.DatePickerDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;

import com.example.mapofspotsdrawer.databinding.FragmentRegisterBinding;
import com.example.mapofspotsdrawer.ui.auth.validation.EmailTextWatcher;
import com.example.mapofspotsdrawer.ui.auth.validation.NameTextWatcher;
import com.example.mapofspotsdrawer.ui.auth.validation.PasswordTextWatcher;
import com.example.mapofspotsdrawer.ui.auth.validation.PhoneNumberTextWatcher;

import java.util.Calendar;

public class RegisterFragment extends Fragment {

    FragmentRegisterBinding binding;

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

        return binding.getRoot();
    }
}