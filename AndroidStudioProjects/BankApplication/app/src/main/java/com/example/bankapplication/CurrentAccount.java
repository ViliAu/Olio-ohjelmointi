package com.example.bankapplication;

public class CurrentAccount extends Account {

    public CurrentAccount(int ID, int ownerId, int bankId, int type, int state, String name, String accountNumber, float balance) {
        super(ID, ownerId, bankId, type, state, name, accountNumber, balance);
    }

}
