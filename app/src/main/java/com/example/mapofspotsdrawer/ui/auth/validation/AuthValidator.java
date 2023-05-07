package com.example.mapofspotsdrawer.ui.auth.validation;

import android.widget.EditText;

import java.time.LocalDate;
import java.util.regex.Pattern;

public class AuthValidator {
    private final EditText et_name;

    private final EditText et_email;

    private final EditText et_phone_number;

    private final EditText et_birth_date;

    private final EditText et_password;

    private final EditText et_repassword;

    public AuthValidator(EditText et_name, EditText et_email,
                         EditText et_phone_number, EditText et_birth_date,
                         EditText et_password, EditText et_repassword) {
        this.et_name = et_name;
        this.et_email = et_email;
        this.et_phone_number = et_phone_number;
        this.et_birth_date = et_birth_date;
        this.et_password = et_password;
        this.et_repassword = et_repassword;
    }

    public AuthValidator(EditText et_email, EditText et_password) {
        this.et_email = et_email;
        this.et_password = et_password;
        this.et_name = null;
        this.et_phone_number = null;
        this.et_birth_date = null;
        this.et_repassword = null;

    }

    public boolean validateRegistration() {
        if (et_name == null || et_email == null ||
                et_phone_number == null || et_birth_date == null ||
                et_password == null || et_repassword == null) {
            return false;
        }

        return (isNameCorrect() && isEmailCorrect() && isPhoneNumberCorrect()
                && isBirthDateCorrect() && isPasswordCorrect(this.et_password)
                && isPasswordCorrect(this.et_repassword));
    }

    public boolean validateAuthorization() {
        if (et_email == null || et_password == null) {
            return false;
        }

        return (isEmailCorrect() && isPasswordCorrect(this.et_password));
    }

    private boolean isNameCorrect() {
        assert et_name != null;
        String name = et_name.getText().toString();

        return !name.isEmpty() && name.length() >= 2 && name.length() <= 30;
    }

    private boolean isEmailCorrect() {
        Pattern emailPattern =
                Pattern.compile("\\w+([\\.-]?\\w+)*@\\w+([\\.-]?\\w+)*\\.\\w{2,4}");
        String email = et_email.getText().toString();

        return emailPattern.matcher(email).matches()
                && email.length() >= 5 && email.length() <= 50;
    }

    private boolean isPhoneNumberCorrect() {
        assert et_phone_number != null;
        String phone_number = et_phone_number.getText().toString();

        return !phone_number.isEmpty();
    }

    private boolean isBirthDateCorrect() {
        assert et_birth_date != null;
        String[] stringDate = et_birth_date.getText().toString().split("\\.");
        LocalDate birthDate = LocalDate.of(Integer.parseInt(stringDate[2]),
                Integer.parseInt(stringDate[1]), Integer.parseInt(stringDate[0]));

        return !et_birth_date.getText().toString().isEmpty()
                && !birthDate.isAfter(LocalDate.now());
    }

    private boolean isPasswordCorrect(EditText et_password) {
        String password = et_password.getText().toString();

        return !password.isEmpty() &&
                password.length() >= 6 && password.length() <= 50;
    }

}
