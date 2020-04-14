package com.example.bankapplication;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class SharedViewModelMain extends ViewModel{
    private MutableLiveData<CharSequence> bankName = new MutableLiveData<CharSequence>();

    public LiveData<CharSequence> getBankName() {
        return bankName;
    }

    public void setBankName(CharSequence s) {
        bankName.setValue(s);
    }

}
