package com.example.bankapplication;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class SharedViewModelMain extends ViewModel{
    private MutableLiveData<CharSequence> bankName = new MutableLiveData<CharSequence>();
    private MutableLiveData<Integer> bankId = new MutableLiveData<Integer>();

    public LiveData<CharSequence> getBankName() {
        return bankName;
    }

    public void setBankName(CharSequence s) {
        bankName.setValue(s);
    }

    public LiveData<Integer> getBankId() {
        return bankId;
    }

    public void setBankId(int id) {
        bankId.setValue(id);
    }

}
