package com.example.guitarfinal.ui.notifications;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class NotificationsViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public NotificationsViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is device settings page.\n We will add device info after we incorperate bluetooth.");
    }

    public LiveData<String> getText() {
        return mText;
    }
}