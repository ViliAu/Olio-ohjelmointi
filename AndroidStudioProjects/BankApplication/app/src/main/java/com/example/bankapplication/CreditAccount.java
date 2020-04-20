package com.example.bankapplication;

public class CreditAccount extends Account {
    private float creditLimit;

    public CreditAccount(int ID, int ownerId, int bankId, int type, int state, String name, String accountNumber, float balance, float creditLimit) {
        super(ID, ownerId, bankId, type, state, name, accountNumber, balance);
        this.creditLimit = creditLimit;
    }


    public float getCreditLimit() {
        return creditLimit;
    }

    public void setCreditLimit(float creditLimit) {
        this.creditLimit = creditLimit;
    }
}
