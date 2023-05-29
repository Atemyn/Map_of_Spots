package com.example.mapofspotsdrawer.ui.create_spot.validation;

import android.view.View;
import android.widget.AdapterView;

public class ListViewOnItemClickListener implements AdapterView.OnItemClickListener {
    AllFieldsValidator allFieldsValidator;

    public ListViewOnItemClickListener(AllFieldsValidator allFieldsValidator) {
        this.allFieldsValidator = allFieldsValidator;
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        allFieldsValidator.validateFields();
    }
}
