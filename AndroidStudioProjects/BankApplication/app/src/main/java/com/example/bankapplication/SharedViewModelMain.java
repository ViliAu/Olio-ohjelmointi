package com.example.bankapplication;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class SharedViewModelMain extends ViewModel{
    private MutableLiveData<CharSequence> bankName = new MutableLiveData<CharSequence>();
    private int bankId = 0;

    public LiveData<CharSequence> getBankName() {
        return bankName;
    }

    public void setBankName(CharSequence s) {
        bankName.setValue(s);
    }

    public int getBankId() {
        return bankId;
    }

    public void setBankId(int id) {
        bankId = id;
    }

}
