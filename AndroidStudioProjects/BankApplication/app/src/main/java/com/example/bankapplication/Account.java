package com.example.bankapplication;

//id, owner_id,	bank_id, address, money_amount, type, state
// ACC TYPES: 1 = current account, 2 = credit account, 3 = savings account, 4 = fixed term account
// ACC STATES: 1 = pending, 2 = active, 3 = disabled, 4 = can't pay
public abstract class Account {

    protected String name, accountNumber;
    protected float balance;
    protected int ID, ownerId, bankId, type, state;

    public Account(int ID, int ownerId, int bankId, int type, int state, String name, String accountNumber, float balance) {
        this.ID = ID;
        this.ownerId = ownerId;
        this.bankId = bankId;
        this.type = type;
        this.state = state;
        this.balance = balance;
        this.name = name;
        this.accountNumber = accountNumber;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public float getBalance() {
        return balance;
    }

    public void setBalance(float balance) {
        this.balance = balance;
    }

    public int getID() {
        return ID;
    }

    public int getOwnerId() {
        return ownerId;
    }

    public int getBankId() {
        return bankId;
    }

    public int getType() {
        return type;
    }

    public int getState() {
        return state;
    }
}
