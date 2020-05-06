package com.example.bankapplication;

public class BankData {
    private final int id;
    private final float interest;
    private final String bic, name;

    public BankData(int id, float interest, String bic, String name) {
        this.id = id;
        this.interest = interest;
        this.bic = bic;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public float getInterest() {
        return interest;
    }

    public String getBic() {
        return bic;
    }

    public String getName() {
        return name;
    }
}
