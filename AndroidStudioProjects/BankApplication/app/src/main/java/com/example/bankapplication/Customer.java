package com.example.bankapplication;

public class Customer {
    // ACCOUNT TYPES: 0 = admin, 1 = pending 2 = normal 3 = (deleted)
    private final int ID;
    private final int type;
    private final int bankId;
    private final String userName, name, address, zipcode, phoneNumber, socialid, salt, password;

    // Used for login check
    public Customer(int id, int type, String userName, String name, String address, String  zipcode, String phoneNumber, String socialid, int bankId, String salt, String password) {
        this.ID = id;
        this.type = type;
        this.userName = userName;
        this.name = name;
        this.address = address;
        this.zipcode = zipcode;
        this.phoneNumber = phoneNumber;
        this.socialid = socialid;
        this.bankId = bankId;
        this.salt = salt;
        this.password = password;
    }

    public Customer(int id, int type, String userName, String name, String address, String  zipcode, String phoneNumber, String socialid, int bankId, String salt) {
        this.ID = id;
        this.type = type;
        this.userName = userName;
        this.name = name;
        this.address = address;
        this.zipcode = zipcode;
        this.phoneNumber = phoneNumber;
        this.socialid = socialid;
        this.bankId = bankId;
        this.salt = salt;
        this.password ="";
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

    public String getZipcode() {
        return zipcode;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getSocialid() {
        return socialid;
    }

    public String getSalt() {
        return salt;
    }

    public String getPassword() {
        return password;
    }
}
