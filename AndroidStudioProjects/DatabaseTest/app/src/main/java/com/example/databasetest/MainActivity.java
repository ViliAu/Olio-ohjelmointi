package com.example.databasetest;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.StrictMode;
import android.os.Bundle;
import android.util.Log;

import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import net.sourceforge.jtds.jdbc.*;

public class MainActivity extends AppCompatActivity {

    private Button button;
    private TextView field;
    private EditText et;

    boolean querySuccess = false;
    private Connection con;
    private String bankName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initBtns();
        initTextEditor();
    }

    private void initBtns() {
        field = (TextView)findViewById(R.id.tw_db_text);
        button = (Button)findViewById(R.id.button_db_query);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("_LOG: Button pressed.");
                CheckLogin cL = new CheckLogin();
                cL.execute("");
                System.out.println("_LOG: QUERY: "+ querySuccess);
            }
        });
    }

    public class CheckLogin extends AsyncTask<String, String, String> {
        String z = "";

        @Override
        protected String doInBackground(String... params) {
            try {
                // Connect to database
                con = connectionclass();
                if (con == null) {
                    z = "Couldn't connect to database.";
                }
                else {
                    // Execute sql query
                    System.out.println("_LOG: Query alku");
                    String query = "SELECT bic FROM Pankit WHERE nimi = '"+bankName+"'";
                    Statement stmt = con.createStatement();
                    ResultSet rs = stmt.executeQuery(query);
                    System.out.println("_LOG: Query loppu");
                    
                    if (rs.next()) {
                        querySuccess = true;
                        String s = rs.getString("bic");
                        setText(s);
                        con.close();
                    }
                    else {
                        querySuccess = false;
                        setText("Pankkia ei löytynyt!");
                    }
                }
            }
            catch (Exception ex) {
                querySuccess = false;
                z = ex.getMessage();
            }
            System.out.println("_LOG: "+z);
            return z;
        }
    }

    private void setText(final String s)  {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                field.setText(s);
            }
        });
    }

    @SuppressLint("NewApi")
    public Connection connectionclass() {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        Connection connection = null;
        String ConnectionURL = null;
        try {
            Class.forName("net.sourceforge.jtds.jdbc.Driver").newInstance();//SQL5047.site4now.net
            ConnectionURL = "jdbc:jtds:sqlserver://SQL5047.site4now.net;" +
                    "database=DB_A57EF2_bank;user=DB_A57EF2_bank_admin;password=db_bank11212";
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

    void initTextEditor() {
        et = (EditText)findViewById(R.id.et_bank_name);
        et.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                bankName = et.getText().toString();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }
}
