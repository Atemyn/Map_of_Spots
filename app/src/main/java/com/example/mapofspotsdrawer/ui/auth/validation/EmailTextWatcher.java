package com.example.mapofspotsdrawer.ui.auth.validation;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import java.util.regex.Pattern;

public class EmailTextWatcher implements TextWatcher {

    private final EditText et_email;

    public EmailTextWatcher(EditText et_email) {
        super();
        this.et_email = et_email;
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        Pattern emailPattern =
                Pattern.compile("\\w+([\\.-]?\\w+)*@\\w+([\\.-]?\\w+)*\\.\\w{2,4}");
        String email = et_email.getText().toString();
        if (!emailPattern.matcher(email).matches()) {
            et_email.setError("E-mail не является валидным");
        } else if (email.isEmpty())  {
            et_email.setError("Введите e-mail");
        } else if (email.length() < 5) {
            et_email.setError("E-mail должен быть длиной не менее 5 символов");
        } else if (email.length() > 50) {
            et_email.setError("E-mail должен быть длиной не более 50 символов");
        } else {
            et_email.setError(null);
        }
    }

    @Override
    public void afterTextChanged(Editable editable) {

    }
}
