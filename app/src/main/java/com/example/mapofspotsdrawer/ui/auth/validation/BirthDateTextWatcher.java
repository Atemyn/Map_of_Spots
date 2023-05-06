package com.example.mapofspotsdrawer.ui.auth.validation;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import java.time.LocalDate;
import java.util.Calendar;

public class BirthDateTextWatcher implements TextWatcher {

    private final EditText et_birth_date;

    public BirthDateTextWatcher(EditText et_birth_date) {
        super();
        this.et_birth_date = et_birth_date;
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        String[] stringDate = et_birth_date.getText().toString().split("\\.");
        LocalDate birthDate = LocalDate.of(Integer.parseInt(stringDate[2]),
                Integer.parseInt(stringDate[1]), Integer.parseInt(stringDate[0]));

        if (et_birth_date.getText().toString().isEmpty()) {
            et_birth_date.setError("Введите дату рождения");
        }
        else if (birthDate.isAfter(LocalDate.now())) {
            et_birth_date.setError("Введена неверная дата рождения");
        }
        else {
            et_birth_date.setError(null);
        }
    }

    @Override
    public void afterTextChanged(Editable editable) {

    }
}
