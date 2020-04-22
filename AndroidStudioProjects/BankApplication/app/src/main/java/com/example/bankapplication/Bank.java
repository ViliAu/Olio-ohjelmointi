package com.example.bankapplication;

import android.content.Context;
import android.widget.Toast;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.Random;

public class Bank {

    private int id;
    private float interest;
    private String bic;
    private TimeManager tm;

    // Create bank instance
    private static Bank b;
    public static Bank getInstance() {
        if (b == null) {
            b = new Bank();
        }
        return b;
    }

    private Bank() {
        tm = TimeManager.getInstance();
        checkPendingPayments();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    private String getBicById(int id) {
        switch (id) {
            case 1:
                return "SNORKKELI";
            case 2:
                return "POJUTIN";
            case 3:
                return "SYNTISYPPI";
            case 4:
                return "ROSKIS";
            default:
                return "ERROR";
        }
    }
    private String generateAccountNumber(int bankId) {
        String prefix = "";
        switch (bankId) {
            case 1:
                prefix = "FI10";
                break;
            case 2:
                prefix = "FI20";
                break;
            case 3:
                prefix = "FI30";
                break;
            case 4:
                prefix = "FI40";
                break;
        }
        // Generate random number sequence
        for (int i = 0; i < 14; i++) {
            Random rand = new Random();
            prefix += String.valueOf(rand.nextInt(10));
        }
        // Check database for same account number
        ResultSet rs = DataBase.dataQuery("SELECT * FROM accounts WHERE account_number = '"+prefix+"' ");
        if (rs != null)
            generateAccountNumber(bankId);
        return prefix;
    }

    public void createAccountRequest(int type, int ownerId, String accName, float creditLimit, Date dueDate) {
        String accNumber = generateAccountNumber(id);
        DataBase.dataInsert("INSERT INTO accounts VALUES ("+DataBase.getNewId("accounts")+", "+ownerId+
                ", "+id+", '"+accNumber+"', "+0+", "+type+", "+1+", '"+accName+"', "+creditLimit+", '"+dueDate+"') ");
    }

    // Used to transfer money between two accounts. Returns the result as a string
    public String transferMoney(String accountFrom, String accountTo, float amount, Date dueDate, boolean isReoccurring, String message) {
        // Check if the transaction happens other time than today
        TimeManager time = new TimeManager();
        if (!dueDate.before(new Date(time.today()))) {
            DataBase.dataInsert("INSERT INTO pending_transactions VALUES ("
                    +DataBase.getNewId("pending_transactions")+
                    ", '"+accountFrom+"', '"+accountTo+"', "+amount+", '"+dueDate+"', '"+isReoccurring+"', '"+message+"') ");
            return "Transfer due date set.";
        }
        // Check if the transaction is recurring
        if (isReoccurring) {
            // Set payment a month ahead
            DataBase.dataInsert("INSERT INTO pending_transactions VALUES ("
                    +DataBase.getNewId("pending_transactions")+
                    ", '"+accountFrom+"', '"+accountTo+"', "+amount+", '"+new Date(time.getDateAdvancedByMonth(dueDate.getTime()))+
                    "', '"+isReoccurring+"') ");
        }
        try {
            ResultSet account1 = DataBase.dataQuery("SELECT * FROM accounts WHERE address = '" + accountFrom + "' ");
            // Check if account has enough money
            if (account1.getFloat("money_amount")+account1.getFloat("credit_limit") < amount) {
                return "Not enough money!";
            }
        }
        catch (SQLException e ) {
            System.out.println("_LOG: "+e);
            return "Something went wrong.";
        }
        DataBase.dataQuery("EXEC transfer_money @account_from = '"+accountFrom+"', @account_to = '"+accountTo+"', @amount = "+amount);
        createTransactionHistory(accountFrom, accountTo, amount, message, dueDate);
        return "Money transferred.";
    }

    // Check if there's any pending transactions which due date has expired
    public void checkPendingPayments() {
        try {
            ResultSet rs = DataBase.dataQuery("EXEC check_payments");
            if (rs != null) {
                do {
                    String result = "";
                    result = transferMoney(rs.getString("account_from"),
                            rs.getString("account_to"), rs.getFloat("amount"),
                            rs.getDate("due_date"),
                            rs.getBoolean("reoccuring"), rs.getString("message"));
                    // Delete pending payment if it is successful and not recurring
                    if (result.equals("Money transferred.") && !rs.getBoolean("reoccuring")) {
                        DataBase.dataUpdate("DELETE FROM pending_transactions WHERE id = "+rs.getInt("id"));
                    }
                } while (rs.next());
            }
        }
        catch (SQLException e) {
            System.out.println("_LOG: "+ e);
        }
    }

    // Write a new entry in transaction history
    private void createTransactionHistory(String accountFrom, String accountTo, float amount, String message, Date date) {
        // Get bank bic codes
        ResultSet rs;
        String bicFrom = "";
        String bicTo = "";
        try {
            rs = DataBase.dataQuery("SELECT * FROM accounts WHERE address = '"+accountFrom+"'");
            bicFrom = getBicById(rs.getInt("bank_id"));
            rs = DataBase.dataQuery("SELECT * FROM accounts WHERE address = '"+accountTo+"'");
            bicTo = getBicById(rs.getInt("bank_id"));
        }
        catch (SQLException e) {
            System.out.println("_LOG: "+e);
        }
        DataBase.dataInsert("INSERT INTO transaction_history VALUES ("+DataBase.getNewId("transaction_history")+
                ", '"+accountFrom+"', '"+accountTo+"', '"+bicFrom+"', '"+bicTo+"', "+amount+", '"+message+"', '"+date+"')");
    }
}
