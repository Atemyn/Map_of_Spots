package com.example.mapofspotsdrawer.ui.create_spot.validation;

import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.example.mapofspotsdrawer.map.YandexMapManager;
import com.example.mapofspotsdrawer.ui.create_spot.CreateSpotInfoViewModel;


public class AllFieldsValidator {
    private final Button addSpotButton;
    private final EditText spotName;
    private final EditText spotDescription;
    private final ListView spotTypesListView;
    private final ListView sportTypesListView;
    private final ListView spaceTypeListView;
    CreateSpotInfoViewModel viewModel;

    private final String noImageUrl;

    public AllFieldsValidator(Button addSpotButton, EditText spotName,
                              EditText spotDescription, ListView spotTypesListView,
                              ListView sportTypesListView, ListView spaceTypeListView,
                              CreateSpotInfoViewModel viewModel, String noImageUrl) {
        this.addSpotButton = addSpotButton;
        this.spotName = spotName;
        this.spotDescription = spotDescription;
        this.spotTypesListView = spotTypesListView;
        this.sportTypesListView = sportTypesListView;
        this.spaceTypeListView = spaceTypeListView;
        this.viewModel = viewModel;
        this.noImageUrl = noImageUrl;
    }

    public void validateFields() {
        addSpotButton.setEnabled(!areViewsNull() && spotName.getText() != null &&
                !spotName.getText().toString().isEmpty() && spotDescription.getText() != null &&
                !spotDescription.getText().toString().isEmpty() &&
                spotTypesListView.getCheckedItemCount() != 0 &&
                sportTypesListView.getCheckedItemCount() != 0 &&
                spaceTypeListView.getCheckedItemCount() != 0 && (viewModel.getImagesUrls().size() > 1 ||
                viewModel.getImagesUrls().size() == 1 && !viewModel.getImagesUrls().get(0).equals(noImageUrl)));
    }

    private boolean areViewsNull() {
        return addSpotButton == null || spotName == null ||
                spotDescription == null || spotTypesListView == null ||
                sportTypesListView == null || spaceTypeListView == null ||
                viewModel == null || viewModel.getImagesUrls() == null ||
                YandexMapManager.getInstance().getSingleMapObject() == null;
    }
}
