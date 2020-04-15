package com.example.bankapplication;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class SharedViewModelAdmin extends ViewModel {
    private MutableLiveData<Integer> customerId = new MutableLiveData<>();

    public void setCustomerId(int id) {
        customerId.setValue(id);
    }

    public LiveData<Integer> getCustomerId() {
        return customerId;
    }
}
