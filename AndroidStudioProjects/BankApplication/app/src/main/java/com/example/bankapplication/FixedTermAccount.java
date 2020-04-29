package com.example.bankapplication;

import java.sql.Date;

public class FixedTermAccount extends SavingsAccount {
    private final Date dueDate;
    public FixedTermAccount(int ID, int ownerId, int bankId, int type, int state, String name, String accountNumber, float balance, Date dueDate, float interest) {
        super(ID, ownerId, bankId, type, state, name, accountNumber, balance, interest);
        this.dueDate = dueDate;
    }

    public Date getDueDate() {
        return dueDate;
    }
}
