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
    private static String queryCommand = "";

    public static void dataQuery(String query) {
        System.out.println("_LOG: Start executing query");
        queryCommand = query;
        DatabaseQuery dbQ = new DatabaseQuery();
        dbQ.execute(query);
    }

    // Database query class, asynchronous
    public static class DatabaseQuery extends AsyncTask<String, String, String> {
        @Override
        protected String doInBackground(String... params) {
            String result = "";
            try {
                // Connect to database
                connection = databaseConnection();  //TODO: Keep the connection and reconnect if connection is lost
                if (connection == null) {
                    System.out.println("_LOG: Couldn't connect to database.");
                }
                else {
                    // Execute sql query
                    System.out.println("_LOG: Query start: "+params[0]);
                    Statement stmt = connection.createStatement();
                    ResultSet rs = stmt.executeQuery(params[0]);
                    System.out.println("_LOG: Query end");

                    if (rs.next()) {
                        result = rs.getString("nimi");
                        connection.close();
                    }
                    else {
                        result = ("Bank couldn't be found!");
                    }
                }
            }
            catch (Exception ex) {
                System.out.println("_LOG: "+result);
                result = ex.getMessage();
            }
            System.out.println("_LOG: "+result);
            return result;
        }
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
           System.out.println("_LOG: SQLException: "+sqle);
        } catch (ClassNotFoundException ce) {
            System.out.println("_LOG: ClassNotFoundException: "+ce);
        } catch (Exception e) {
            System.out.println("_LOG: Exception: "+e);
        }
        return connection;
    }

}
