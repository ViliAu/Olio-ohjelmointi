package com.example.bankapplication;

import android.content.Context;
import android.provider.ContactsContract;
import android.widget.Toast;

import java.sql.Array;
import java.sql.Date;
import java.sql.ResultSet;
import java.util.ArrayList;

// This class handles all the queries to database and also checks if there's an ongoing query
public class DataManager {
    private ResultSet rs;

    public static DataManager dm = null;

    public static DataManager getInstance() {
        if (dm == null) {
            dm = new DataManager();
        }
        return dm;
    }

    public int getNewId(String table) throws Exception{
        return DataBase.getNewId(table);
    }

    public ArrayList<AccountListElement> loadAccounts(int id) throws Exception {
        ArrayList<AccountListElement> accList = new ArrayList<>();
        ResultSet rs = DataBase.dataQuery("SELECT * FROM accounts WHERE owner_id = " + id + " ORDER BY due_date DESC");
        if (rs != null) {
            do {
                accList.add(new AccountListElement(rs.getString("name"),
                        rs.getString("address"), rs.getInt("id"),
                        rs.getInt("type"), rs.getInt("state"),
                        rs.getFloat("money_amount")));
            } while (rs.next());
        }
        return accList;
    }

    public ArrayList<Account> getCustomerAccounts(int customerId, int bankId) throws Exception {
        ArrayList<Account> accList = new ArrayList<>();
        ResultSet rs = DataBase.dataQuery("SELECT * FROM accounts WHERE owner_id = " + customerId + " ORDER BY money_amount DESC");
        if (rs != null) {
            if (rs.getInt("state") == 2 || rs.getInt("state") == 4) {
                do {
                    switch (rs.getInt("type")) {
                        case 1:
                            accList.add(new CurrentAccount(rs.getInt("id"),
                                    customerId, bankId, 1,
                                    rs.getInt("state"), rs.getString("name"),
                                    rs.getString("address"), rs.getFloat("money_amount")));
                            break;
                        case 2:
                            accList.add(new CreditAccount(rs.getInt("id"),
                                    customerId, bankId, 2,
                                    rs.getInt("state"), rs.getString("name"),
                                    rs.getString("address"), rs.getFloat("money_amount"), rs.getFloat("credit_limit")));
                            break;
                        case 3:
                            accList.add(new SavingsAccount(rs.getInt("id"),
                                    customerId, bankId, 3,
                                    rs.getInt("state"), rs.getString("name"),
                                    rs.getString("address"), rs.getFloat("money_amount"), rs.getFloat("interest")));
                            break;
                        case 4:
                            accList.add(new FixedTermAccount(rs.getInt("id"),
                                    customerId, bankId, 4,
                                    rs.getInt("state"), rs.getString("name"),
                                    rs.getString("address"), rs.getFloat("money_amount"), rs.getDate("due_date"), rs.getFloat("interest")));
                            break;
                    }
                } while (rs.next());
            }
        }
        return accList;
    }

    public Account getAccountByNumber(String accNumber) throws Exception {
        ResultSet rs = DataBase.dataQuery("SELECT * FROM ACCOUNTS WHERE address = '" + accNumber + "' ");
        return new CurrentAccount(
                rs.getInt("id"), rs.getInt("owner_id"),
                rs.getInt("bank_id"), rs.getInt("type"), rs.getInt("state"),
                rs.getString("name"), rs.getString("address"),
                rs.getFloat("money_amount"));
    }

    public int getBankIdByAccount(String accountNumber) throws Exception {
        ResultSet rs = DataBase.dataQuery("SELECT * FROM accounts WHERE address = '" + accountNumber + "'");
        return rs.getInt("bank_id");
    }

    public int getBankIdByBankName(String name) throws Exception {
        ResultSet rs = DataBase.dataQuery("SELECT * FROM accounts WHERE name = '" + name + "'");
        return rs.getInt("id");
    }

    public float getAccountMoneyAmount(String accountNumber) throws Exception {
        ResultSet rs = DataBase.dataQuery("EXEC get_account @account = '" + accountNumber + "' ");
        return rs.getFloat("money_amount");
    }

    public void createTransactionHistory(String accountFrom, String accountTo, String bicF, String bicT, float amount, String message, String date, String action) throws Exception {
        DataBase.dataInsert(
                "INSERT INTO transaction_history VALUES (" + DataBase.getNewId("transaction_history") +
                        ", '" + accountFrom + "', '" + accountTo + "', '" + bicF + "', '" + bicT + "', " + amount + ", '" + message +
                        "', '" + date + "', '" + action + "')");
    }

    public void createCardRequest(Account acc, String number) throws Exception {
        DataBase.dataInsert("INSERT INTO cards VALUES (" + DataBase.getNewId("cards") +
                ", '" + acc.getAccountNumber() + "', " + 100 + ", " + 100 + ", " + 1 + ", '" + number + "', " + 1 + ", 'Bank card' ) ");
    }

    public boolean hasPendingCard(String accNumber) throws Exception {
        DataBase.dataQuery("SELECT * FROM cards WHERE owner_account = '" + accNumber + "' AND state = 1");
        return (rs == null);
    }

    public void payInterest(String account, float amount, Date date, int id) throws Exception {
        try {
            DataBase.dataUpdate("EXEC pay_interest @account = '" + account + "', @amount = " + amount + ", @date = '" + date + "', @id =" + id);
        } catch (Exception e) {
            throw e;
        }
    }

    public ArrayList<Customer> getCustomersForAdminView(String searchPattern, String searchWord) throws Exception {
        ArrayList<Customer> searchedCustomers = new ArrayList<>();
        ResultSet rs;
        if (searchPattern.equals("id") || searchPattern.equals("state") || searchPattern.equals("bank_id"))
            rs = DataBase.dataQuery("SELECT * FROM customers WHERE " + searchPattern + " = " + searchWord + " AND NOT id = 1 ORDER BY id ASC");
        else
            rs = DataBase.dataQuery("SELECT * FROM customers WHERE " + searchPattern + " LIKE '%" + searchWord + "%' AND NOT id = 1 ORDER BY id ASC");
        if (rs != null) {
            do {
                searchedCustomers.add(new Customer(rs.getInt("id"), rs.getInt("state"),
                        rs.getString("username"), rs.getString("name"), rs.getString("address"),
                        rs.getString("zipcode"), rs.getString("phonenumber"), rs.getString("socialid"),
                        rs.getInt("bank_id"), rs.getString("salt")));
            } while (rs.next());
        }
        return searchedCustomers;
    }

    public Customer getSingleCustomer(int id) throws Exception {
        ResultSet rs = DataBase.dataQuery("SELECT * FROM customers WHERE id = " + id);
        return new Customer(rs.getInt("id"), rs.getInt("state"),
                rs.getString("username"), rs.getString("name"), rs.getString("address"),
                rs.getString("zipcode"), rs.getString("phonenumber"), rs.getString("socialid"),
                rs.getInt("bank_id"), rs.getString("salt"));
    }

    // Used for login check
    public Customer getSingleCustomer(int bankId, String name) throws Exception {
        ResultSet rs = DataBase.dataQuery("SELECT * FROM customers WHERE username = '" + name + "' AND (bank_id = " + bankId + " OR bank_id = 0)");
        return new Customer(rs.getInt("id"), rs.getInt("state"),
                rs.getString("username"), rs.getString("name"), rs.getString("address"),
                rs.getString("zipcode"), rs.getString("phonenumber"), rs.getString("socialid"),
                rs.getInt("bank_id"), rs.getString("salt"), rs.getString("password"));
    }

    public void updateCustomerInfo(String name, String phoneNumber, String hashed, String address, String zipcode, String socialid, int id) throws Exception {
        DataBase.dataUpdate("UPDATE customers SET name = '" + name + "', phonenumber = '" + phoneNumber + "'," +
                " password = '" + hashed + "', address = '" + address + "'," +
                " zipcode = '" + zipcode + "', socialid = '" + socialid + "' " +
                "WHERE id = " + id);
    }

    public void updateState(String table, int state, int id) throws Exception {
        DataBase.dataUpdate("UPDATE " + table + " SET state = " + state + " WHERE id = " + id);
    }

    public void updateState(String table, int state, String accNum) throws Exception {
        DataBase.dataUpdate("UPDATE " + table + " SET state = " + state + " WHERE owner_account = '" + accNum + "' ");
    }

    public boolean exists(String table, String accountNumber) throws Exception {
        try {
            ResultSet rs = DataBase.dataQuery("SELECT * FROM " + table + " WHERE address = '" + accountNumber + "' ");
            return (rs == null);
        } catch (Exception e) {
            System.err.println("_LOG: " + e);
            throw e;
        }
    }

    public void resetCards() throws Exception {
        DataBase.dataUpdate("EXEC reset_cards");
    }

    public void checkFixedTermAccounts() throws Exception {
        DataBase.dataUpdate("EXEC check_expired_accounts");
    }

    public void updateCardSettings(String name, float payAmount, float withdrawAmount, int countryLimit, int id) throws Exception {
        DataBase.dataUpdate("UPDATE cards SET name = '" + name + "', pay_limit = " + payAmount +
                ", withdraw_limit = " + withdrawAmount + ", country_limit = " + countryLimit +
                " WHERE id = " + id);
    }

    // Used in recycler view
    public ArrayList<PendingPayment> getAccountPendingPayments(String accountNumber) throws Exception {
        ArrayList<PendingPayment> pendings = new ArrayList<>();
        ResultSet rs = DataBase.dataQuery("SELECT * FROM pending_transactions WHERE account_from = '" + accountNumber + "' OR account_to = '" + accountNumber + "' ORDER BY due_date DESC");
        if (rs != null) {
            do {
                pendings.add(new PendingPayment(rs.getString("account_from"), rs.getString("account_to"),
                        rs.getString("message"), rs.getDate("due_date"),
                        rs.getFloat("amount"), rs.getBoolean("recurring"),
                        rs.getInt("id"), rs.getBoolean("interest"),
                        accountNumber));
            } while (rs.next());
        }
        return pendings;
    }

    public void deletePayment(int id) throws Exception {
        DataBase.dataUpdate("DELETE FROM pending_transactions WHERE id = " + id);
    }

    public ArrayList<PaymentTransaction> getAccountPaymentHistory(String accountNumber) throws Exception {
        ArrayList<PaymentTransaction> transactions = new ArrayList<>();
        ResultSet rs;
        rs = DataBase.dataQuery("SELECT * FROM transaction_history WHERE account_from = '" + accountNumber + "' OR account_to = '" + accountNumber + "' ORDER BY date DESC");
        if (rs != null) {
            do {
                transactions.add(new PaymentTransaction(
                        rs.getString("account_from"), rs.getString("account_to"), rs.getString("bic_from"),
                        rs.getString("bic_to"), rs.getString("message"),
                        rs.getFloat("amount"), rs.getTimestamp("date"),
                        rs.getString("action"), accountNumber
                ));
            } while (rs.next());
        }
        return transactions;
    }

    public boolean customerAlreadyExists(int bankId, String userName) throws Exception {
        ResultSet rs = DataBase.dataQuery("SELECT * FROM customers WHERE bank_id = " + bankId + " AND username = '" + userName + "' ");
        return (rs != null);
    }

    public void createCustomerRequest(String userName, String name, String phoneNumber, String hashPass, int bankId, String address, String zipcode, String socialid, String salt) throws Exception {
        DataBase.dataInsert("INSERT INTO customers VALUES (" + (DataBase.getNewId("customers")) +
                ", '" + userName + "', '" + name + "', '" + phoneNumber + "', '" +
                hashPass + "', " + bankId + ", '" + address + "', '" +
                zipcode + "', '" + socialid + "', " + 1 + ", '" + salt + "')");
    }

    public boolean hasEnoughMoney(String accountFrom, float amount) throws Exception {
        ResultSet rs = DataBase.dataQuery("SELECT * FROM accounts WHERE address = '" + accountFrom + "' ");
        return (rs.getFloat("money_amount") + rs.getFloat("credit_limit") > amount);
    }

    public ArrayList<PendingPayment> getPendingPayments() throws Exception {
        ArrayList<PendingPayment> payments = new ArrayList<>();
        ResultSet rs = DataBase.dataQuery("EXEC check_payments");
        if (rs != null) {
            do {
                payments.add(new PendingPayment(rs.getString("account_from"), rs.getString("account_to"),
                        rs.getString("message"), rs.getDate("due_date"),
                        rs.getFloat("amount"), rs.getBoolean("recurring"),
                        rs.getInt("id"), rs.getBoolean("interest")));
            } while (rs.next());
        }
        return payments;
    }

    public void createNewPendingTransaction(PendingPayment p) throws Exception {
        DataBase.dataInsert("INSERT INTO pending_transactions VALUES ("
                + DataBase.getNewId("pending_transactions") +
                ", '" + p.getAccountFrom() + "', '" + p.getAccountTo() + "', " + p.getAmount() +
                ", '" + p.getDate() + "', '" + p.isReoccurring() + "', '" + p.getMessage() + "', '"
                + p.isInterest() + "') ");
    }

    public ArrayList<Card> getAccountCards(Account acc) throws Exception {
        ArrayList<Card> custCards = new ArrayList<>();
        ResultSet rs = DataBase.dataQuery("SELECT * FROM cards WHERE owner_account = '" + acc.getAccountNumber() + "' AND NOT (state = 1 OR state = 3)");
        if (rs != null) {
            do {
                custCards.add(new Card
                        (rs.getString("owner_account"), rs.getString("number"),
                                rs.getFloat("pay_limit"), rs.getFloat("withdraw_limit"),
                                rs.getInt("country_limit"), rs.getInt("state"),
                                rs.getString("name"), rs.getFloat("paid"),
                                rs.getFloat("withdrawn"), rs.getInt("id")));
                } while (rs.next());
        }
        return custCards;
    }
}