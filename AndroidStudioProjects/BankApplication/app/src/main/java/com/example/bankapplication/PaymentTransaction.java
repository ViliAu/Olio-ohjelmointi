package com.example.bankapplication;

import java.sql.Timestamp;

public class PaymentTransaction {
    private final String accountFrom;
    private final String accountTo;
    private final String bicFrom;
    private final String bicTo;
    private final String message;
    private final float amount;
    private final Timestamp date;
    private final String action;
    private final String targetAccount;

    public PaymentTransaction(String accountFrom, String accountTo, String bicFrom, String bicTo,
                              String message, float amount, Timestamp date, String action, String targetAccount) {
        this.accountFrom = accountFrom;
        this.accountTo = accountTo;
        this.bicFrom = bicFrom;
        this.bicTo = bicTo;
        this.message = message;
        this.amount = amount;
        this.date = date;
        this.action = action;
        this.targetAccount = targetAccount;
    }

    public String getAccountFrom() {
        return accountFrom;
    }

    public String getAccountTo() {
        return accountTo;
    }

    public String getBicFrom() {
        return bicFrom;
    }

    public String getBicTo() {
        return bicTo;
    }

    public String getMessage() {
        return message;
    }

    public float getAmount() {
        return amount;
    }

    public Timestamp getDate() {
        return date;
    }

    public String getAction() {
        return action;
    }

    public String getTargetAccount() {
        return targetAccount;
    }
}
