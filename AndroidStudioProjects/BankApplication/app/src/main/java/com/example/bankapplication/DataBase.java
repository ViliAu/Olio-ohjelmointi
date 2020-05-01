package com.example.bankapplication;

import android.annotation.SuppressLint;
import android.os.StrictMode;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

//TODO: Rework database with sql procedures (viikonloppu tekemist vilille(mugelle))
//TODO: Throw exceptions!
public class DataBase {

    private static Connection connection;
    // Database query result
    private static ResultSet rs;

    // Deny instantiation
    private DataBase(){}

    static ResultSet dataQuery(String query) {
        System.out.println("_LOG: Start executing query");
        return dataBaseAccess(query);
    }

    public static void dataInsert(String input) {
        System.out.println("_LOG: Start executing input");
        rs = dataBaseAccess(input);
    }

    public static void dataUpdate(String update) {
        System.out.println("_LOG: Start executing update");
        rs = dataBaseAccess(update);
    }

    public static int getNewId(String tableName) {
        return createNewId(tableName);
    }

    /* Database query class, asynchronous
    public static class DatabaseQuery extends AsyncTask<String, String, String> {
        @Override
        protected String doInBackground(String... params) { */
    private static ResultSet dataBaseAccess(String query) {
        String result;
        try {
            // Connect to database
            //connection = databaseConnection();
            boolean isConnected = getConnection();
            if (!isConnected) {
                result = "Couldn't connect to database.";
            }
            else {
                // Execute sql query
                System.out.println("_LOG: Query start: " + query);
                Statement stmt = connection.createStatement();
                rs = stmt.executeQuery(query);
                System.out.println("_LOG: Query end ");

                if (rs.next()) {
                    //connection.close();
                    return rs;
                }
                else {
                    //connection.close();
                    return null;
                }
            }
        } catch (Exception ex) {
            result = ex.getMessage();
        }
        System.out.println("_LOG: " + result);
        return null;
    }

    private static int createNewId(String tableName) {
        String result;
        try {
            // Connect to database
            //connection = databaseConnection();
            boolean isConnected = getConnection();
            if (!isConnected) {
                result = "Couldn't connect to database.";
            }
            else {
                // Execute sql query
                System.out.println("_LOG: Query start: SELECT TOP 1 * FROM " +tableName+ " ORDER BY id DESC");
                Statement stmt = connection.createStatement();
                ResultSet rs = stmt.executeQuery("SELECT TOP 1 * FROM " +tableName+ " ORDER BY id DESC");
                System.out.println("_LOG: Query end");
                if (rs.next()) {
                    return rs.getInt("id")+1;
                }
                else {
                    result = ("Couldn't create list order!");
                }
                //connection.close();
            }
        } catch (Exception ex) {
            result = ex.getMessage();
        }
        System.out.println("_LOG: " + result);
        return 0;
    }

    // DATABASE CONNECTION ÄLÄ KOSKE HYVIN TOIMII
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
        } catch (ClassNotFoundException ce) {
            System.out.println("_LOG: ClassNotFoundException: " + ce);
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
        }
        catch (SQLException e) {
            System.out.println("_LOG: "+e);
            return false;
        }
    }
    /*
    public static void closeConnection() {
        try {
            connection.close();
            System.out.println("_LOG: Database closed successfully.");
        }
        catch (SQLException e) {
            System.out.println("_LOG: "+e);
        }
    }*/
}
