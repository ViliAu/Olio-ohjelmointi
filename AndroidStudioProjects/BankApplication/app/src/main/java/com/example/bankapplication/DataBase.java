package com.example.bankapplication;

import android.annotation.SuppressLint;
import android.os.StrictMode;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DataBase {

    private static Connection connection;
    // Database query result
    private static ResultSet rs;
    //private DataBaseAccess access;

    // Deny instantiation
    private DataBase() {
    }

    static ResultSet dataQuery(String query) throws Exception {
        return fetchData(query);
    }

    public static void dataInsert(String input) throws Exception {
        dataBaseUpdate(input);
    }

    public static void dataUpdate(String update) throws Exception {
        dataBaseUpdate(update);
    }

    public static int getNewId(String tableName) throws Exception {
        ResultSet rs = fetchData("SELECT TOP 1 * FROM " + tableName + " ORDER BY id DESC");
        if (rs == null)
            return 0;
        else
            return rs.getInt("id");
    }

    private static ResultSet fetchData(String query) throws Exception {
        String result;
        // Connect to database
        boolean isConnected = getConnection();
        if (!isConnected) {
            result = "Couldn't connect to database.";
            throw new Exception();
        } else {
            // Execute sql query
            System.out.println("_LOG: Query start: " + query);
            Statement stmt = connection.createStatement();
            ResultSet rs;
            //try {
                rs = stmt.executeQuery(query);
            /*}
            catch (Exception e) {
                System.err.println("_LOG: "+e);
                //throw e;
            }*/
            if (rs.next()) {
                return rs;
            } else {
                result = "Couldn't find any matches!";
            }
        }
        System.out.println("_LOG: " + result);
        return null;
    }

    private static void dataBaseUpdate(String query) throws Exception {
        String result = "";
        // Connect to database
        boolean isConnected = getConnection();
        if (!isConnected) {
            result = "Couldn't connect to database.";
            throw new Exception();
        } else {
            // Execute sql query
            System.out.println("_LOG: Query start: " + query);
            Statement stmt = connection.createStatement();
            stmt.executeUpdate(query);
        }
        System.out.println("_LOG: " + result);
    }

    /*
    private static int createNewId(String tableName) throws Exception {
        String result;
        // Connect to database
        boolean isConnected = getConnection();
        if (!isConnected) {
            result = "Couldn't connect to database.";
        } else {
            // Execute sql query
            System.out.println("_LOG: Query start: SELECT TOP 1 * FROM " + tableName + " ORDER BY id DESC");
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT TOP 1 * FROM " + tableName + " ORDER BY id DESC");
            System.out.println("_LOG: Query end");
            if (rs.next()) {
                return rs.getInt("id") + 1;
            } else {
                result = ("List is empty or couldn't create list order");
            }
        }
        System.out.println("_LOG: " + result);
        return 0;
    }*/

    @SuppressLint("NewApi")
    private static void connect() {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        Connection con = null;
        String ConnectionURL;
        try {
            Class.forName("net.sourceforge.jtds.jdbc.Driver").newInstance();
            ConnectionURL = "jdbc:jtds:sqlserver://SQL5047.site4now.net;" +
                    "database=DB_A57EF2_bank;user=DB_A57EF2_bank_admin;password=db_bank11212";
            con = DriverManager.getConnection(ConnectionURL);
        } catch (SQLException sqle) {
            System.out.println("_LOG: SQLException: " + sqle);
        } catch (Exception e) {
            System.out.println("_LOG: Exception: " + e);
        }
        connection = con;
    }

    public static boolean getConnection() {
        try {
            if (connection == null || connection.isClosed())
                connect();
            return connection != null;
        } catch (SQLException e) {
            System.out.println("_LOG: " + e);
            return false;
        }
    }
}
