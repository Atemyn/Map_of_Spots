package com.example.mapofspotsdrawer.ui.create_spot.validation;

import android.text.Editable;
import android.view.View;
import android.widget.EditText;

public class EditTextOnFocusChangeListener implements View.OnFocusChangeListener {
    private final AllFieldsValidator allFieldsValidator;

    public EditTextOnFocusChangeListener(AllFieldsValidator allFieldsValidator) {
        this.allFieldsValidator = allFieldsValidator;
    }

    @Override
    public void onFocusChange(View view, boolean b) {
        if (!b) {
            EditText editText = (EditText) view;
            Editable text = editText.getText();
            if (text == null || text.toString().isEmpty()) {
                editText.setError("Данное поле не может быть пустым");
            }
            allFieldsValidator.validateFields();
        }
    }
}
