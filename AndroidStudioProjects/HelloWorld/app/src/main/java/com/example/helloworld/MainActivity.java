package com.example.helloworld;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Environment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.lang.reflect.Field;
import java.sql.SQLOutput;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    Button changeButton, saveButton, loadButton;
    TextView textField;
    EditText helloEditor, fileName;

    Context c;
    FileHandler filukka;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Setup view
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        c = getApplicationContext();
        filukka = new FileHandler(c);
        initButtons();
        initFields();
        initEditors();
    }

    void initButtons() {
        changeButton = (Button)findViewById(R.id.button_hello_world);
        changeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeText();
            }
        });

        saveButton = (Button)findViewById(R.id.button_save);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveText();
            }
        });

        loadButton = (Button)findViewById(R.id.button_load);
        loadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadText();
            }
        });
    }

    void changeText() {
        System.out.println("_LOG: Hello World");
        textField.setText(helloEditor.getText());
    }

    void saveText() {
        if (filukka != null)
            filukka.writeFile(fileName.getText().toString(), textField.getText().toString());
        System.out.println("_LOG:" + filukka);
    }

    void loadText() {
        ArrayList<String> fileLines = new ArrayList<String>();
        String contents = "";
        fileLines = filukka.readFile(fileName.getText().toString());
        for (String s : fileLines) {
            contents += s;
        }
        textField.setText(contents);
    }

    void initFields() {
        textField = (TextView)findViewById(R.id.field_hello_world);
    }

    void initEditors() {
        helloEditor = (EditText)findViewById(R.id.edit_text_hello_world);
        helloEditor.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                textField.setText(helloEditor.getText());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        fileName = (EditText)findViewById(R.id.edit_text_filename);

    }
}
