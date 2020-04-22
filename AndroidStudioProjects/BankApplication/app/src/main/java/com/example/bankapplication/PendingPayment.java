package com.example.bankapplication;

import java.sql.Date;

public class PendingPayment {
    private final String accountFrom;
    private final String message;
    private final Date dueDate;
    private final float amount;
    private final boolean reoccurring;
    private final int id;

    public PendingPayment(String accountFrom, String message, Date dueDate, float amount, boolean reoccurring, int id) {
        this.accountFrom = accountFrom;
        this.message = message;
        this.dueDate = dueDate;
        this.amount = amount;
        this.reoccurring = reoccurring;
        this.id = id;
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

    public boolean isReoccurring() {
        return reoccurring;
    }

    public int getId() {
        return id;
    }
}
