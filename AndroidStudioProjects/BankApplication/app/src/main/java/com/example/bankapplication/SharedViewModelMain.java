package com.example.bankapplication;

import androidx.lifecycle.ViewModel;

public class SharedViewModelMain extends ViewModel{
    private int bankId = 0;
    private int customerId = 0;

    public int getBankId() {
        return bankId;
    }

    public void setBankId(int id) {
        bankId = id;
    }

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }
}
