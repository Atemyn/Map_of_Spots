package com.example.mapofspotsdrawer.ui.auth.validation;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

public class PasswordTextWatcher implements TextWatcher {
    private final EditText et_password;

    public PasswordTextWatcher(EditText et_password) {
        super();
        this.et_password = et_password;
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        String password = et_password.getText().toString();
        if (password.isEmpty())  {
            et_password.setError("Введите пароль");
        } else if (password.length() < 6) {
            et_password.setError("Пароль должен быть длиной не менее 6 символов");
        } else if (password.length() > 50) {
            et_password.setError("Пароль должен быть длиной не более 50 символов");
        } else {
            et_password.setError(null);
        }
    }

    @Override
    public void afterTextChanged(Editable editable) {

    }
}
