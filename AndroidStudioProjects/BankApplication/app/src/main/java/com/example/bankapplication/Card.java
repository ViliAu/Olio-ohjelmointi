package com.example.bankapplication;

// COUNTRY LIMITS: 1 = FINLAND, 2 = NORDIC COUNTRIES, 3 = EUROPE, 4 = WHOLE WORLD.
public class Card {
    private final String ownerAccount, number, name;
    private final float payLimit, withdrawLimit, paid, withdrawn;
    private final int countryLimit, state, id;

    public Card(String ownerAccount, String number, float payLimit, float withdrawLimit, int countryLimit, int state, String name, float paid, float withdrawn, int id) {
        this.ownerAccount = ownerAccount;
        this.number = number;
        this.payLimit = payLimit;
        this.withdrawLimit = withdrawLimit;
        this.countryLimit = countryLimit;
        this.state = state;
        this.name = name;
        this.paid = paid;
        this.withdrawn = withdrawn;
        this.id = id;
    }

    public String getOwnerAccount() {
        return ownerAccount;
    }

    public String getNumber() {
        return number;
    }

    public String getNumberFormatted() {
        String formatted = "";
        int i = 0;
        for (Character c : getNumber().toCharArray()) {
            i++;
            formatted += c;
            if (i % 4 == 0) {
                formatted += " ";
            }
        }
        return formatted;
    }

    public float getPayLimit() {
        return payLimit;
    }

    public float getWithdrawLimit() {
        return withdrawLimit;
    }

    public int getCountryLimit() {
        return countryLimit;
    }

    public int getState() {
        return state;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return getName() + ", "+getNumberFormatted();
    }

    public float getPaid() {
        return paid;
    }

    public float getWithdrawn() {
        return withdrawn;
    }

    public int getId() {
        return id;
    }
}
