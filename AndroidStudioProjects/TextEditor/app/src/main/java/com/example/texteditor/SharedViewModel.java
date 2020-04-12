package com.example.texteditor;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class SharedViewModel extends ViewModel {
    private MutableLiveData<CharSequence> overrideableText = new MutableLiveData<CharSequence>();
    private MutableLiveData<int[]> color = new MutableLiveData<int[]>();
    private MutableLiveData<Integer> size = new MutableLiveData<Integer>();
    private MutableLiveData<Integer> lineAmount = new MutableLiveData<Integer>();
    private MutableLiveData<Boolean> editAccess = new MutableLiveData<Boolean>();

    private MutableLiveData<CharSequence> stringSaver = new MutableLiveData<CharSequence>();

    // Overrideable text
    public void setOverrideableText(CharSequence input) {
        overrideableText.setValue(input);
    }

    public LiveData<CharSequence> getOverrideableText() {
        return overrideableText;
    }

    // String to save
    public void setString(CharSequence s) {
        stringSaver.setValue(s);
    }

    public LiveData<CharSequence> getString() {
        return stringSaver;
    }

    // Text color
    public void setColor(int[] color) {
        this.color.setValue(color);
    }

    public LiveData<int[]> getColor() {
        return color;
    }

    // Text size
    public void setSize(int i) {
        size.setValue(i);
    }

    public LiveData<Integer> getSize() {
        return size;
    }

    // Line amount
    public void setLineAmount(int i) {
        lineAmount.setValue(i);
    }

    public LiveData<Integer> getLineAmount() {
        return lineAmount;
    }

    // Editable
    public void setEditAccess(boolean b) {
        editAccess.setValue(b);
    }

    public LiveData<Boolean> getEditAccess() {
        return editAccess;
    }
}
