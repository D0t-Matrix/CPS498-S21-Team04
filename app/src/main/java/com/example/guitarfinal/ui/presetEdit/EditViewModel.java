package com.example.guitarfinal.ui.presetEdit;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class EditViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public EditViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is home page.\nThis will show wether the device is connected or not.\nNeed more information.");
    }

    public LiveData<String> getText() {
        return mText;
    }
}