package com.example.bankapplication;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.StrictMode;
import android.util.Log;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DataBase {

    private static Connection connection;

    public static ResultSet dataQuery(String query) {
        System.out.println("_LOG: Start executing query");
        return dataBaseQuery(query);
    }

    public static void dataInsert(String input) {
        System.out.println("_LOG: Start executing input");
        dataBaseQuery(input);
    }

    public static int getTableLength(String tableName) {
        return tableLength(tableName);
    }

    /* Database query class, asynchronous
    public static class DatabaseQuery extends AsyncTask<String, String, String> {
        @Override
        protected String doInBackground(String... params) { */
    private static ResultSet dataBaseQuery(String query) {
        String result = "";
        try {
            // Connect to database
            connection = databaseConnection();  //TODO: Keep the connection and reconnect if connection is lost, make this as a async class
            if (connection == null) {
                result = "Couldn't connect to database.";
            }
            else {
                // Execute sql query
                System.out.println("_LOG: Query start: " + query);
                Statement stmt = connection.createStatement();
                ResultSet rs = stmt.executeQuery(query);
                System.out.println("_LOG: Query end ");

                if (rs.next()) {
                    connection.close();
                    return rs;
                }
                else {
                    connection.close();
                    return null;
                }
            }
        } catch (Exception ex) {
            result = ex.getMessage();
        }
        System.out.println("_LOG: " + result);
        return null;
    }

    private static int tableLength(String tableName) {
        String result = "";
        try {
            // Connect to database
            connection = databaseConnection();  //TODO: Keep the connection and reconnect if connection is lost, make this as a async class
            if (connection == null) {
                result = "Couldn't connect to database.";
            }
            else {
                // Execute sql query
                System.out.println("_LOG: Query start: SELECT TOP 1 * FROM " +tableName+ " ORDER BY id DESC");
                Statement stmt = connection.createStatement();
                ResultSet rs = stmt.executeQuery("SELECT TOP 1 * FROM " +tableName+ " ORDER BY id DESC");
                System.out.println("_LOG: Query end");
                if (rs.next()) {
                    return rs.getInt("id");
                }
                else {
                    result = ("Table couldn't be found!");
                }
                connection.close();
            }
        } catch (Exception ex) {
            result = ex.getMessage();
        }
        System.out.println("_LOG: " + result);
        return 0;
    }

    // DATABASE CONNECTION ÄLÄ KOSKE HYVIN TOIMII
    @SuppressLint("NewApi")
    private static Connection databaseConnection() {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        Connection connection = null;
        String ConnectionURL = "";
        try {
            Class.forName("net.sourceforge.jtds.jdbc.Driver").newInstance();
            ConnectionURL = "jdbc:jtds:sqlserver://SQL5047.site4now.net;" +
                    "database=DB_A57EF2_bank;user=DB_A57EF2_bank_admin;password=db_bank11212";
            connection = DriverManager.getConnection(ConnectionURL);
        } catch (SQLException sqle) {
            System.out.println("_LOG: SQLException: " + sqle);
        } catch (ClassNotFoundException ce) {
            System.out.println("_LOG: ClassNotFoundException: " + ce);
        } catch (Exception e) {
            System.out.println("_LOG: Exception: " + e);
        }
        return connection;
    }

}
