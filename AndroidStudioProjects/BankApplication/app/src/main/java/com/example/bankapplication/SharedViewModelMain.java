package com.example.bankapplication;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class SharedViewModelMain extends ViewModel{
    private int bankId = 0;

    public int getBankId() {
        return bankId;
    }

    public void setBankId(int id) {
        bankId = id;
    }

}
