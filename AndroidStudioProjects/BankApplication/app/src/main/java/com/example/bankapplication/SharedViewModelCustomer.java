package com.example.bankapplication;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;

public class SharedViewModelCustomer extends ViewModel {
    private int customerId = 0;
    private int bankId = 0;
    private Bank bank;
    private ArrayList<Account> accounts = new ArrayList<>();
    private ArrayList<Card> accountCards = new ArrayList<>();
    private Account accountToEdit;
    private Card cardToEdit;

    // Info dialog textview strings
    private String accountFrom, accountTo, bicFrom, bicTo, date, amount, message;

    public int getCustomerId() {
        return customerId;
    }
    public void setCustomerId(int id) {
        customerId = id;
    }

    public int getBankId() {
        return bankId;
    }

    public void setBankId(int bankId) {
        this.bankId = bankId;
    }

    public Bank getBank() {
        return bank;
    }

    public void setBank(Bank bank) {
        this.bank = bank;
    }

    public ArrayList<Account> getAccounts() {
        return accounts;
    }

    // Info dialog textview getters/setters

    public void setAccounts(ArrayList<Account> accounts) {
        this.accounts = accounts;
    }

    public String getAccountFrom() {
        return accountFrom;
    }

    public void setAccountFrom(String accountFrom) {
        this.accountFrom = accountFrom;
    }

    public String getAccountTo() {
        return accountTo;
    }

    public void setAccountTo(String accountTo) {
        this.accountTo = accountTo;
    }

    public String getBicFrom() {
        return bicFrom;
    }

    public void setBicFrom(String bicFrom) {
        this.bicFrom = bicFrom;
    }

    public String getBicTo() {
        return bicTo;
    }

    public void setBicTo(String bicTo) {
        this.bicTo = bicTo;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Account getAccountToEdit() {
        return accountToEdit;
    }

    public void setAccountToEdit(Account accountToEdit) {
        this.accountToEdit = accountToEdit;
    }

    public Card getCardToEdit() {
        return cardToEdit;
    }

    public void setCardToEdit(Card cardToEdit) {
        this.cardToEdit = cardToEdit;
    }

    public ArrayList<Card> getAccountCards() {
        return accountCards;
    }

    public void setAccountCards(ArrayList<Card> accountCards) {
        this.accountCards = accountCards;
    }
}
