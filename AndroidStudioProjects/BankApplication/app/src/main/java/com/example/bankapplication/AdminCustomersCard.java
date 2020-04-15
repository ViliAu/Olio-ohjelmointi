package com.example.bankapplication;

public class AdminCustomersCard {
    private int image;
    private String username;
    private String id;
    private String bank;
    private String owner;
    private String accType;

    public AdminCustomersCard(int imageResource, String username, String id, String bank, String name, String accType) {
        image = imageResource;
        this.username = username;
        this.id = id;
        this.bank = bank;
        this.owner = name;
        this.accType = accType;
    }

    public int getImageResource() {
        return image;
    }

    public String getUsername() {
        return username;
    }

    public String getId() {
        return id;
    }

    public String getBank() {
        return bank;
    }

    public String getOwner() {
        return owner;
    }

    public String getAccType() {
        return accType;
    }
}
