//|---------------------------------------------------------------------------|
//|    CLASS    : HomeViewModel.java by Alex Gennero                          |
//|                                                                           |
//|    PURPOSE  : Setting the text on the homepage.                           |
//|                                                                           |
//|---------------------------------------------------------------------------|

package com.example.guitarfinal.ui.home;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class HomeViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public HomeViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("Guitar Effects Looper");
    }

    public LiveData<String> getText() {
        return mText;
    }
}