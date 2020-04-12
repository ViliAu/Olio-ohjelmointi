package com.example.databasetest;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.StrictMode;
import android.os.Bundle;
import android.util.Log;

import android.widget.Button;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import net.sourceforge.jtds.jdbc.*;

public class MainActivity extends AppCompatActivity {

    Button button;

    Connection con;
    String un,pass,db,ip;
    String usernam,passwordd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initBtns();
    }

    private void initBtns() {
        button = findViewById(R.id.button_db_query);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("_LOG: Button pressed.");
                CheckLogin cL = new CheckLogin();
                cL.execute("");
            }
        });
    }

    public class CheckLogin extends AsyncTask<String, String, String> {
        String z = "";
        Boolean isSuccess = false;

        @Override
        protected void onPostExecute(String r) {
            System.out.println("_LOG: aa");
        }

        @Override
        protected String doInBackground(String... params) {
            try {
                // Connect to database
                con = connectionclass();
                if (con == null) {
                    z = "_LOG: couldn't connect to database.";
                }
                else {
                    // Execute sql query
                    String query = "SELECT * FROM login";
                    Statement stmt = con.createStatement();
                    ResultSet rs = stmt.executeQuery(query);
                    if (rs.next()) {
                        z = "Login successful";
                        isSuccess = true;
                        con.close();
                    }
                    else {
                        z = "Invalid Credentials!";
                        isSuccess = false;
                    }
                }
            }
            catch (Exception ex) {
                isSuccess = false;
                z = ex.getMessage();
            }
            return z;
        }
    }

    @SuppressLint("NewApi")
    public Connection connectionclass() {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        Connection connection = null;
        String ConnectionURL = null;
        try {
            Class.forName("net.sourceforge.jtds.jdbc.Driver").newInstance();//SQL5047.site4now.net
            ConnectionURL = "jdbc:jtds:sqlserver://SQL5047.site4now.net;database=DB_A57EF2_bank;" +
                    "user=DB_A57EF2_bank_admin;password=db_bank11212";
            connection = DriverManager.getConnection(ConnectionURL);
        } catch (SQLException se) {
            Log.e("error here 1 : ", se.getMessage());
        } catch (ClassNotFoundException e) {
            Log.e("error here 2 : ", e.getMessage());
        } catch (Exception e) {
            Log.e("error here 3 : ", e.getMessage());
        }
        return connection;
    }
}
