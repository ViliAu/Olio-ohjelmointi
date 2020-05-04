package com.example.bankapplication;

public class AccountListElement {
    private final String name, accountNumber;
    private final int id, type, state;
    private final float balance;

    public AccountListElement(String name, String accountNumber, int id, int type,
                               int state, float balance) {
        this.name = name;
        this.accountNumber = accountNumber;
        this.id = id;
        this.type = type;
        this.state = state;
        this.balance = balance;
    }


    @Override
    public String toString() {
        return accountNumber + "\n State: "+getStateString();
    }

    public String getStateString() {
        switch(this.state) {
            case 1:
                return "Pending";
            case 2:
                return "Normal";
            case 3:
                return "Disabled";
            case 4:
                return "Payment disabled";
            default:
                return "???";
        }
    }

    public String getName() {
        return name;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public int getId() {
        return id;
    }

    public int getType() {
        return type;
    }

    public float getBalance() {
        return balance;
    }
}
