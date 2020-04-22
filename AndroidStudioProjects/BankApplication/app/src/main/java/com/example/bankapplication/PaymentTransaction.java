package com.example.bankapplication;

public class PaymentTransaction {
    String accountFrom;
    String accountTo;
    String bicFrom;
    String bicTo;
    String message;
    float amount;

    public PaymentTransaction(String accountFrom, String accountTo, String bicFrom, String bicTo, String message, float amount) {
        this.accountFrom = accountFrom;
        this.accountTo = accountTo;
        this.bicFrom = bicFrom;
        this.bicTo = bicTo;
        this.message = message;
        this.amount = amount;
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
}
