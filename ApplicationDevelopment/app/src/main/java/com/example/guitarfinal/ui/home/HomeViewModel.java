package com.example.guitarfinal.ui.home;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class HomeViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public HomeViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is home page.\nThis will show wether the device is connected or not.\nNeed more information.");
    }

    public LiveData<String> getText() {
        return mText;
    }
}