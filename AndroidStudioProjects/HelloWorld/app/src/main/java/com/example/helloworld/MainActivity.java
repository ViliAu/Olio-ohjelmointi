package com.example.helloworld;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.lang.reflect.Field;

public class MainActivity extends AppCompatActivity {

    Button changeButton;
    TextView helloField;

    EditText helloEditor;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Setup view
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initButtons();
        initFields();
        initEditors();
    }

    void initButtons() {
        changeButton = (Button)findViewById(R.id.button_hello_world);
        changeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickEvent();
            }
        });
    }

    void onClickEvent() {
        System.out.println("_LOG: Hello World");
        helloField.setText(helloEditor.getText());
    }

    void initFields() {
        helloField = (TextView)findViewById(R.id.field_hello_world);
    }

    void initEditors() {
        helloEditor = (EditText)findViewById(R.id.edit_text_hello_world);
        helloEditor.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                helloField.setText(helloEditor.getText());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


    }
}
