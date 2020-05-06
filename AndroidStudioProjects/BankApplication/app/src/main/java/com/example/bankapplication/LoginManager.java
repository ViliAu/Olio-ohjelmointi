package com.example.bankapplication;

public class LoginManager {
    private DataManager data;
    public LoginManager() {
        data = DataManager.getInstance();
    }
    /* Returns result based on action
        1 = Username not found
        2 = Password incorrect
        3 = Customer pending
        4 = Load customer activity
        5 = Customer disabled
        6 = Admin
        7 = User not found
     */

    public int[] login(String name, String pass, int bankId) {
        int[] i = new int[2];
        Customer c = null;
        try {
            c = data.getSingleCustomer(bankId, name);
        }
        catch (Exception e) {
            System.err.println("_LOG: "+e);
        }

        try {
            if (c == null) {
                i[0] = 1;
                return i;
            }
            String salt = c.getSalt();
            if (!Hasher.testPassword(pass, c.getPassword(), salt)) {
                i[0] = 2;
                return i;
            }
            // Get acc type
            int accountType = c.getType();
            // Pending
            if (accountType == 1) {
                i[0] = 3;
                return i;
            }
            // Normal
            else if (accountType == 2) {
                i[0] = 4;
                i[1] = c.getId();
                return i;
            }
            // Disabled
            else if (accountType == 3) {
                i[0] = 5;
                return i;
            }
            // Admin
            else if (accountType == 0) {
                i[0] = 6;
                return i;
            }
        }
        catch (Exception e) {
            i[0] = 7;
            System.err.println("_LOG: "+e);
            return i;
        }
        i[0] = 7;
        return i;
    }
}
