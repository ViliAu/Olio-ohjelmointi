package com.example.bankapplication;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class SharedViewModelAdmin extends ViewModel {
    private int customerId = 0;

    public void setCustomerId(int id) {
        customerId = id;
    }

    public int getCustomerId() {
        return customerId;
    }
}
