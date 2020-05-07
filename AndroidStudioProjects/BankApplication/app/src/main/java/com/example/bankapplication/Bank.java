package com.example.bankapplication;

import android.content.Context;
import android.widget.Toast;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.ExecutorCompletionService;

public class Bank {

    private int id;
    private String name;
    private String bic;
    private float interest;
    private TimeManager time;
    private DataManager data;

    // Create bank instance
    private static Bank b;

    public static Bank getInstance() {
        if (b == null) {
            b = new Bank();
        }
        return b;
    }

    private Bank() {
        data = DataManager.getInstance();
        time = TimeManager.getInstance();
        checkPendingPayments();
        checkFixedTermAccounts();
        checkCards();
    }

    public int getId() {
        return id;
    }

    public void setValues(int id) throws Exception {
        BankData bank = data.getBankById(id);
        this.id = bank.getId();
        this.bic = bank.getBic();
        this.name = bank.getName();
        this.interest = bank.getInterest();
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

    private String generateAccountNumber(int bankId) throws Exception {
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
        try {
            if (data.exists("accounts", prefix))
                generateAccountNumber(bankId);
            return prefix;
        } catch (Exception e) {
            throw e;
        }
    }

    public void createAccountRequest(int type, int ownerId, String accName, float creditLimit, Date dueDate) throws Exception {
        // Generate an account number
        String accNumber = generateAccountNumber(id);
        // Create interest payment if needed
        if (type == 3 || type == 4)
            createNewInterestPayment(accNumber, type);

        float bankInterest = interest;
        if (type == 4)
            bankInterest *= 3;
        // Insert the account in the database
        data.createAccountRequest(ownerId, id, accNumber, type, accName, creditLimit, dueDate, bankInterest);
    }

    // Used to transfer money between two accounts.
    public void transferMoney(PendingPayment p) throws Exception {
        // Check if the transaction happens in the future (due_date)
        if (!p.getDate().before(new Date(time.today()))) {
            data.createNewPendingTransaction(new PendingPayment(p.getAccountFrom(), p.getAccountTo(),
                    p.getMessage(), p.getDate(), p.getAmount(), 0, 0, false));
            return;
        }

        // Check if the transaction is recurring
        if (p.isReoccurring() == 2) {
            // Set payment a month ahead
            data.createNewPendingTransaction(new PendingPayment(p.getAccountFrom(), p.getAccountTo(),
                    p.getMessage(), new Date(time.getDateAdvancedByMonth(p.getDate().getTime())),
                    p.getAmount(), 2, 0, false));
        } else if (p.isReoccurring() == 1) {
            // Set payment a week ahead
            data.createNewPendingTransaction(new PendingPayment(p.getAccountFrom(), p.getAccountTo(),
                    p.getMessage(), new Date(time.getDateAdvancedByWeek(p.getDate().getTime())),
                    p.getAmount(), 1, 0, false));
        }

        data.transferMoney(p);
        createTransactionHistory(p.getAccountFrom(), p.getAccountTo(), p.getAmount(), p.getMessage(), p.getDate(), "Transaction", "", "");
    }

    private void payInterest(PendingPayment p) throws Exception {
        // Get values
        String bicFrom;
        String bicTo;
        float sum;

        // Get bank bic
        bicFrom = getBicById(data.getBankIdByBankName(p.getAccountFrom()));

        // Get account bic and money amount
        bicTo = getBicById(data.getBankIdByAccount(p.getAccountTo()));
        sum = data.getAccountMoneyAmount(p.getAccountTo());

        // Calculate payable interest and pay interest
        sum *= (p.getAmount() * 0.01f);
        data.payInterest(p.getAccountTo(), sum, new Date(time.getDateAdvancedByMonth(p.getDate().getTime())), id,
                new Date(time.getDateAdvancedByMonth(p.getDate().getTime())));

        // Create transaction history
        data.createTransactionHistory(p.getAccountFrom(), p.getAccountTo(), bicFrom, bicTo, sum,
                p.getMessage(), time.getDateTime(p.getDate().getTime()), "Monthly interest");
    }

    // Check if there's any pending transactions which due date has expired
    public void checkPendingPayments() {
        ArrayList<PendingPayment> payments;
        try {
            payments = data.getPendingPayments();
            for (PendingPayment p : payments) {
                // Check if payment is an interest
                if (p.isInterest()) {
                    payInterest(p);
                } else {
                    if (data.hasEnoughMoney(p.getAccountFrom(), p.getAmount())) {
                        transferMoney(p);
                        data.deletePayment(p.getId());
                    } else {
                        if (p.isReoccurring() != 0) {
                            data.changeRecurrenceToNone(p.getId());
                        }
                    }
                }
                // Iterate the payment if it's recurring so that every month gets paid
                if (p.isReoccurring() != 0) {
                    checkPendingPayments();
                    break;
                }
            }
        } catch (Exception e) {
            System.err.println("_LOG: " + e);
        }
    }

    // Write a new entry in transaction history
    private void createTransactionHistory(String accountFrom, String accountTo,
                                          float amount, String message, Date date, String action, String bicF, String bicT) {
        // Get bank bic codes
        String bicFrom = bicF;
        String bicTo = bicT;
        try {
            if (bicFrom.equals("")) {
                bicFrom = getBicById(data.getBankIdByAccount(accountFrom));
            }
            if (bicTo.equals("")) {
                bicTo = getBicById(data.getBankIdByAccount(accountTo));
            }
        } catch (Exception e) {
            System.out.println("_LOG: " + e);
        }
        try {
            data.createTransactionHistory(accountFrom, accountTo, bicFrom, bicTo, amount, message,
                    time.getDateTime(date.getTime()), action);
        } catch (Exception e) {
            System.err.println("_LOG: " + e);
        }
    }

    // Check if there's any pending accounts that have expired and set their type to savings acc
    private void checkFixedTermAccounts() {
        try {
            data.checkFixedTermAccounts();
        } catch (Exception e) {
            System.err.println("_LOG: " + e);
        }
    }

    public void updateAccount(int id, String accountName, int accountType,
                              float creditLimit, String accNumber, int state) throws Exception {
        data.updateAccountInfo(id, accountName, accountType, creditLimit, accNumber, state);
        createNewInterestPayment(accNumber, accountType);
    }

    private void createNewInterestPayment(String accNumber, int type) throws Exception {
        // Check if there's already an interest payment
        if (data.existsInterest(accNumber)) {
            if (type == 3 || type == 4) {
                return;
            } else {
                data.deleteInterest(accNumber);
                return;
            }
        }

        // Create pending interest payment if the account is a savings account or fixed term
        if (type == 3 || type == 4) {
            // Calculate interest and message based on the account type (Savings = bank interest, Fixed Term = bank interest * 3)
            float bankInterest = 0f;
            bankInterest = (type == 3) ? interest : interest * 3;
            String accountString = (type == 3) ? "Savings account interest pay" : "Fixed term account interest pay";

               /* Because interests are handled differently than normal transactions, instead of
               money amount we input the interest of the bank. */
            data.createNewPendingTransaction(new PendingPayment(name, accNumber, accountString,
                    new Date(time.getDateAdvancedByMonth(time.today())), bankInterest, 2, 0, true));
        }
    }

    // Withdrawing or depositing
    public void cardAction(int action, String accountNumber, float amount, int cardId) throws
            Exception {
        if (action == 0) {
            data.addMoney(accountNumber, amount);
            createTransactionHistory("ATM", accountNumber, amount, "Cash deposit.", new Date(time.today()), "Deposit", this.bic, "");
        } else if (action == 1) {
            data.addMoney(accountNumber, -amount);
            data.updateCardDailies("withdrawn", amount, cardId);
            createTransactionHistory(accountNumber, "ATM", amount, "Cash withdraw.", new Date(time.today()), "Withdraw", "", this.bic);
        }
    }

    public void cardPayment(String accountNumber, float amount, int cardId, String to) throws
            Exception {
        data.addMoney(accountNumber, -amount);
        data.updateCardDailies("paid", amount, cardId);
        createTransactionHistory(accountNumber, to, amount, "Card payment to " + to, new Date(time.today()), "Card payment", "", this.bic);
    }

    private void checkCards() {
        try {
            data.resetCards();
        } catch (Exception e) {
            System.err.println("_LOG: " + e);
        }
    }
}