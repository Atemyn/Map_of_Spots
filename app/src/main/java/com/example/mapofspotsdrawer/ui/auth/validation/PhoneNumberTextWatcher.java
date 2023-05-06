package com.example.mapofspotsdrawer.ui.auth.validation;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

public class PhoneNumberTextWatcher implements TextWatcher {

    private final EditText et_phone_number;

    public PhoneNumberTextWatcher(EditText et_phone_number) {
        super();
        this.et_phone_number = et_phone_number;
    }


    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        String phone_number = et_phone_number.getText().toString();
        if (phone_number.isEmpty())  {
            et_phone_number.setError("Введите номер телефона");
        } else {
            et_phone_number.setError(null);
        }
    }

    @Override
    public void afterTextChanged(Editable editable) {

    }
}
