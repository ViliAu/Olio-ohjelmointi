package com.example.bankapplication;

import java.sql.Date;

public class PendingPayment {
    private final String accountFrom, accountTo;
    private final String message;
    private final Date dueDate;
    private final float amount;
    private final boolean interest;
    private final int id, reoccurring;
    private final String targetAccount;

    public PendingPayment(String accountFrom, String accountTo, String message, Date dueDate, float amount, int reoccurring, int id, boolean interest) {
        this.accountFrom = accountFrom;
        this.accountTo = accountTo;
        this.message = message;
        this.dueDate = dueDate;
        this.amount = amount;
        this.reoccurring = reoccurring;
        this.id = id;
        this.interest = interest;
        this.targetAccount = "";
    }

    // Used in recycler view
    public PendingPayment(String accountFrom, String accountTo, String message, Date dueDate, float amount, int reoccurring, int id, boolean interest, String targetAccount) {
        this.accountFrom = accountFrom;
        this.accountTo = accountTo;
        this.message = message;
        this.dueDate = dueDate;
        this.amount = amount;
        this.reoccurring = reoccurring;
        this.id = id;
        this.interest = interest;
        this.targetAccount = targetAccount;
    }

    public String getAccountFrom() {
        return accountFrom;
    }

    public String getMessage() {
        return message;
    }

    public Date getDate() {
        return dueDate;
    }

    public float getAmount() {
        return amount;
    }

    public int isReoccurring() {
        return reoccurring;
    }

    public int getId() {
        return id;
    }

    public String getAccountTo() {
        return accountTo;
    }

    public boolean isInterest() {
        return interest;
    }

    public String getTargetAccount() {
        return targetAccount;
    }
}
