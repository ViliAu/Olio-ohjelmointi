package com.example.bankapplication;

public class Customer {
    // ACCOUNT TYPES: 0 = admin, 1 = pending 2 = normal 3 = (deleted)
    private int ID;
    private int type;
    private  int bankId;
    private String userName, name, address, zipcode, phoneNumber, socialid;

    public Customer(int id, int type, String userName, String name, String address, String  zipcode, String phoneNumber, String socialid, int bankId) {
        this.ID = id;
        this.type = type;
        this.userName = userName;
        this.name = name;
        this.address = address;
        this.zipcode = zipcode;
        this.phoneNumber = phoneNumber;
        this.socialid = socialid;
        this.bankId = bankId;
    }

    public int getId() {
        return this.ID;
    }
    public int getType() {
        return this.type;
    }
    public String getAccountName() {
        return this.userName;
    }
    public String getName() {
        return this.name;
    }
    public String getAddress() {
        return this.address;
    }
    public String getTypeString() {
        switch (type) {
            case 1:
                return "Pending";
            case 2:
                return "Normal";
            case 3:
                return "Disabled";
            default:
                return "ERROR";
        }
    }
    public String getBankString() {
        switch (bankId) {
            case 1:
                return "Snorkkeli";
            case 2:
                return "Op-poika";
            case 3:
                return "Syppi";
            case 4:
                return "Roskapankki";
            default:
                return "ERROR";
        }
    }

    public String getAccTypeString() {
        switch (type) {
            case 1:
                return "Pending";
            case 2:
                return "Normal";
            case 3:
                return "Disabled";
            default:
                return "ERROR";
        }
    }
}
