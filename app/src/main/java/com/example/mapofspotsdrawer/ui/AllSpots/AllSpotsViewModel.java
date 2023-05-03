package com.example.mapofspotsdrawer.ui.AllSpots;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class AllSpotsViewModel extends ViewModel {

    private final MutableLiveData<String> mText;

    public AllSpotsViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is home fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}