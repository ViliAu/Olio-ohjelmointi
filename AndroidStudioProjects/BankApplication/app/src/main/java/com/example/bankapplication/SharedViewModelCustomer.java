package com.example.bankapplication;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class SharedViewModelCustomer extends ViewModel {
    private int customerId = 0;

    public int getCustomerId() {
        return customerId;
    }
    public void setCustomerId(int id) {
        customerId = id;
    }

}
