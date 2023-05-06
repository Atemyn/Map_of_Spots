package com.example.mapofspotsdrawer.ui.auth.validation;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

public class NameTextWatcher implements TextWatcher {

    private final EditText et_name;

    public NameTextWatcher(EditText et_name) {
        super();
        this.et_name = et_name;
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        String name = et_name.getText().toString();
        if (name.isEmpty())  {
            et_name.setError("Введите логин");
        } else if (name.length() < 2) {
            et_name.setError("Логин должен быть длиной не менее 2 символов");
        } else if (name.length() > 30) {
            et_name.setError("Логин должен быть длиной не более 30 символов");
        } else {
            et_name.setError(null);
        }
    }

    @Override
    public void afterTextChanged(Editable editable) {

    }
}
