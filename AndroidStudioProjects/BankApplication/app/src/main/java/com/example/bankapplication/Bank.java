package com.example.bankapplication;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Random;

public class Bank {

    private int id;
    private String name;
    private String bic;
    private float interest;
    private TimeManager time;

    // Create bank instance
    private static Bank b;

    public static Bank getInstance() {
        if (b == null) {
            b = new Bank();
        }
        return b;
    }

    private Bank() {
        time = TimeManager.getInstance();
        checkPendingPayments();
        checkFixedTermAccounts();
        checkCards();
    }

    public int getId() {
        return id;
    }

    public void setValues(int id) throws Exception {
        try {
            ResultSet rs = DataBase.dataQuery("SELECT * FROM Pankit WHERE id = " + id);
            this.id = id;
            this.bic = rs.getString("bic");
            this.name = rs.getString("nimi");
            this.interest = rs.getFloat("interest");
        } catch (Exception e) {
            throw e;
        }
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
        ResultSet rs = DataBase.dataQuery("SELECT * FROM accounts WHERE address = '" + prefix + "' ");
        if (rs != null)
            generateAccountNumber(bankId);
        return prefix;
    }

    public void createAccountRequest(int type, int ownerId, String accName, float creditLimit, Date dueDate) throws Exception {
        // Generate an account number
        String accNumber = generateAccountNumber(id);
        // Create interest payment if needed
        if (type == 3 || type == 4) {
            try {
                createNewInterestPayment(accNumber, type);
            } catch (Exception e) {
                throw e;
            }
        }
        float bankInterest = interest;
        if (type == 4)
            bankInterest *= 3;
        // Insert the account in the database
        DataBase.dataInsert("INSERT INTO accounts VALUES (" + DataBase.getNewId("accounts") + ", " + ownerId +
                ", " + id + ", '" + accNumber + "', " + 0 + ", " + type + ", " + 1 + ", '" + accName + "', " + creditLimit + ", '" + dueDate + "', " + bankInterest + ") ");
    }

    // Used to transfer money between two accounts. Returns the result as a string
    public String transferMoney(String accountFrom, String accountTo, float amount, Date dueDate, boolean isReoccurring, String message) {
        // Check if the transaction happens other time than today
        if (!dueDate.before(new Date(time.today()))) {
            DataBase.dataInsert("INSERT INTO pending_transactions VALUES ("
                    + DataBase.getNewId("pending_transactions") +
                    ", '" + accountFrom + "', '" + accountTo + "', " + amount + ", '" + dueDate + "', '" + isReoccurring + "', '" + message + "', '" + false + "') ");
            return "Transfer due date set.";
        }
        // Check if the transaction is recurring
        if (isReoccurring) {
            // Set payment a month ahead
            DataBase.dataInsert("INSERT INTO pending_transactions VALUES ("
                    + DataBase.getNewId("pending_transactions") +
                    ", '" + accountFrom + "', '" + accountTo + "', " + amount + ", '" + new Date(time.getDateAdvancedByMonth(dueDate.getTime())) +
                    "', '" + true + "', '" + message + "') ");
        }
        try {
            ResultSet account1 = DataBase.dataQuery("SELECT * FROM accounts WHERE address = '" + accountFrom + "' ");
            // Check if account has enough money
            if (account1.getFloat("money_amount") + account1.getFloat("credit_limit") < amount) {
                return "Not enough money!";
            }
        } catch (SQLException e) {
            System.out.println("_LOG: " + e);
            return "Something went wrong.";
        }
        DataBase.dataQuery("EXEC transfer_money @account_from = '" + accountFrom + "', @account_to = '" + accountTo + "', @amount = " + amount);
        createTransactionHistory(accountFrom, accountTo, amount, message, dueDate, "Transaction", "", "");
        return "Money transferred.";
    }

    //TODO: Rework this shit when database throws exceptions
    private String payInterest(String accountFrom, String accountTo, float amount, Date dueDate, String message) {
        // Get values
        ResultSet rs;
        String bicFrom;
        String bicTo;
        float sum;
        try {
            // Get bank bic
            rs = DataBase.dataQuery("SELECT * FROM Pankit WHERE nimi = '" + accountFrom + "'");
            bicFrom = getBicById(rs.getInt("id"));

            // Get account bic and money amount
            rs = DataBase.dataQuery("SELECT * FROM accounts WHERE address = '" + accountTo + "'");
            bicTo = getBicById(rs.getInt("bank_id"));
            sum = rs.getFloat("money_amount");
        } catch (Exception e) {
            System.out.println("_LOG: " + e);
            return "Something went wrong.";
        }
        // Calculate payable interest
        sum *= (amount * 0.01f);

        // Pay interest and update payment to next month
        DataBase.dataUpdate("EXEC add_money @account_number = '" + accountTo + "', @amount = " + sum);
        DataBase.dataUpdate("UPDATE pending_transactions SET due_date = '" + new Date(time.getDateAdvancedByMonth(dueDate.getTime())) + "' WHERE id = " + id);

        // Create transaction history
        DataBase.dataInsert("INSERT INTO transaction_history VALUES (" + DataBase.getNewId("transaction_history") +
                ", '" + accountFrom + "', '" + accountTo + "', '" + bicFrom + "', '" + bicTo + "', " + sum + ", '" + message + "', '" + dueDate + "', '" + "Monthly interest" + "')");
        return "Interest paid.";
    }

    // Check if there's any pending transactions which due date has expired
    public void checkPendingPayments() {
        try {
            ResultSet rs = DataBase.dataQuery("EXEC check_payments");
            if (rs != null) {
                do {
                    // Check if payment is an interest
                    String result = "";
                    if (rs.getBoolean("interest")) {
                        result = payInterest(rs.getString("account_from"),
                                rs.getString("account_to"), rs.getFloat("amount"),
                                rs.getDate("due_date"),
                                rs.getString("message"));
                    } else {
                        result = transferMoney(rs.getString("account_from"),
                                rs.getString("account_to"), rs.getFloat("amount"),
                                rs.getDate("due_date"),
                                rs.getBoolean("reoccuring"), rs.getString("message"));
                    }

                    // Delete pending payment if it is successful and not recurring
                    if (result.equals("Money transferred.")) {
                        DataBase.dataUpdate("DELETE FROM pending_transactions WHERE id = " + rs.getInt("id"));
                    }
                    // If the payment fails and is reoccurring, remove the recurrence form the failed payment
                    else if (rs.getBoolean("reoccuring") && result.equals("Interest paid.")) {
                        DataBase.dataUpdate("UPDATE accounts SET reoccuring = 'false' WHERE id = " + rs.getInt("id"));
                    }
                    // Iterate the payment if it's reoccurring so that every month gets paid
                    if (rs.getBoolean("reoccuring")) {
                        checkPendingPayments();
                        break;
                    }
                } while (rs.next());
            }
        } catch (SQLException e) {
            System.out.println("_LOG: " + e);
        }
    }

    // Write a new entry in transaction history
    private void createTransactionHistory(String accountFrom, String accountTo, float amount, String message, Date date, String action, String bicF, String bicT) {
        // Get bank bic codes
        ResultSet rs;
        String bicFrom = bicF;
        String bicTo = bicT;
        try {
            if (bicFrom.equals("")) {
                rs = DataBase.dataQuery("SELECT * FROM accounts WHERE address = '" + accountFrom + "'");
                bicFrom = getBicById(rs.getInt("bank_id"));
            }
            if (bicTo.equals("")) {
                rs = DataBase.dataQuery("SELECT * FROM accounts WHERE address = '" + accountTo + "'");
                bicTo = getBicById(rs.getInt("bank_id"));
            }
        } catch (SQLException e) {
            System.out.println("_LOG: " + e);
        }
        DataBase.dataInsert("INSERT INTO transaction_history VALUES (" + DataBase.getNewId("transaction_history") +
                ", '" + accountFrom + "', '" + accountTo + "', '" + bicFrom + "', '" + bicTo + "', " + amount + ", '" + message + "', '" + time.getDateTime(date.getTime()) + "', '" + action + "')");
    }

    // Check if there's any pending accounts that have expired and set their type to savings acc
    private void checkFixedTermAccounts() {
        try {
            ResultSet rs;
            rs = DataBase.dataQuery("EXEC check_expired_accounts");
            if (rs != null) {
                do {
                    DataBase.dataUpdate("UPDATE accounts SET type = 3, interest /= 3 WHERE id = " + rs.getInt("id"));
                } while (rs.next());
            }
        } catch (SQLException e) {
            System.out.println("_LOG: " + e);
        }
    }

    public void updateAccount(int id, String accountName, int accountType, float creditLimit, String accNumber, int state) throws Exception {
        DataBase.dataUpdate("UPDATE accounts SET name = '" + accountName +
                "', type = " + accountType + ", credit_limit = " + creditLimit + ", state = "+state+" WHERE id = " + id);
        try {
            createNewInterestPayment(accNumber, accountType);
        }
        catch (Exception e) {
            throw e;
        }
    }

    private void createNewInterestPayment(String accNumber, int type) throws Exception {
        // Check if there's already an interest payment
        System.out.println("_LOG: "+type);
        try {
            ResultSet rs = DataBase.dataQuery("SELECT * FROM pending_transactions WHERE account_to = '"+accNumber+"' AND interest = 'true'");
            if (rs != null) {
                if (type == 3 || type == 4) {
                    return;
                }
                else {
                    DataBase.dataUpdate("DELETE FROM pending_transactions WHERE account_to = '" + accNumber + "' AND interest = 'true'");
                    return;
                }
            }
        }
        catch (Exception e) {
            throw e;
        }

        // Create pending interest payment if the account is a savings account or fixed term
        if (type == 3 || type == 4) {
            // Calculate interest and message based on the account type (Savings = bank interest, Fixed Term = bank interest * 3)
            float bankInterest = 0f;
            bankInterest = (type == 3) ? interest : interest * 3;
            String accountString = (type == 3) ? "Savings account interest pay" : "Fixed term account interest pay";

               /* Because interests are handled differently than normal transactions, instead of
               money amount we input the interest of the bank. */
            DataBase.dataInsert("INSERT INTO pending_transactions VALUES ("
                    + DataBase.getNewId("pending_transactions") +
                    ", '" + name + "', '" + accNumber + "', " + bankInterest + ", '" + new Date(time.getDateAdvancedByMonth(time.today())) +
                    "', '" + true + "', '" + accountString + "', '" + true + "') ");
        }
    }

    // Withdrawing or depositing
    public void cardAction(int action, String accountNumber, float amount, int cardId) throws Exception{
        try {
            if (action == 0) {
                DataBase.dataUpdate("EXEC add_money @account_number = '" + accountNumber + "', @amount = " + amount);
                createTransactionHistory("ATM", accountNumber, amount, "Cash deposit.", new Date(time.today()), "Deposit", this.bic, "");
            }
            else if (action == 1) {
                DataBase.dataUpdate("EXEC add_money @account_number = '" + accountNumber + "', @amount = " + (-amount));
                DataBase.dataUpdate("UPDATE cards SET withdrawn += "+amount+" WHERE id = "+cardId);
                createTransactionHistory(accountNumber, "ATM", amount, "Cash withdraw.", new Date(time.today()), "Withdraw", "", this.bic);
            }
        }
        catch (Exception e) {
            System.out.println("_LOG: "+e);
        }
    }

    public void cardPayment(String accountNumber, float amount, int cardId, String to) throws Exception{
        try {
            DataBase.dataUpdate("EXEC add_money @account_number = '" + accountNumber + "', @amount = " + (-amount));
            DataBase.dataUpdate("UPDATE cards SET paid += "+amount+" WHERE id = "+cardId);
            createTransactionHistory(accountNumber, to, amount, "Card payment to "+to, new Date(time.today()), "Card payment", "", this.bic);
        }
        catch (Exception e) {
            System.out.println("_LOG: "+e);
        }
    }

    private void checkCards() {
        DataBase.dataUpdate("EXEC reset_cards");
    }
}