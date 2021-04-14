package com.example.guitarfinal.ui.popupmenu;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class PopViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public PopViewModel() {
       // mText = new MutableLiveData<>();
       // mText.setValue("This is home page.\nThis will show wether the device is connected or not.\nNeed more information.");
    }

    public LiveData<String> getText() {
        return mText;
    }
}