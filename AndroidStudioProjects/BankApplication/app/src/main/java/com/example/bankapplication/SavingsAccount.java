package com.example.bankapplication;

public class SavingsAccount extends Account {
    private float interest;
    public SavingsAccount(int ID, int ownerId, int bankId, int type, int state, String name, String accountNumber, float balance, float interest) {
        super(ID, ownerId, bankId, type, state, name, accountNumber, balance);
        this.interest = interest;
    }

    public float getInterest() {
        return interest;
    }

    public void setInterest(float interest) {
        this.interest = interest;
    }
}
