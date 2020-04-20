package com.example.bankapplication;

public class FixedTermAccount extends Account {

    public FixedTermAccount(int ID, int ownerId, int bankId, int type, int state, String name, String accountNumber, float balance) {
        super(ID, ownerId, bankId, type, state, name, accountNumber, balance);
    }
}
