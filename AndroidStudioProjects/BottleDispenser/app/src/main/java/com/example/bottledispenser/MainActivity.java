package com.example.bottledispenser;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

import android.content.Context;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {
    //Primitives
    private int moneyToAdd;
    private int money;

    //Others
    private BottleDispenser bd;
    private Context context;

    //UI Elements
    private TextView action;
    private Button btnAddMoney;
    private Button btnPrintReceipt;
    private SeekBar seekBar;
    private Spinner spinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Setup view
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = getApplicationContext();

        money = 10;
        initElements();

        bd = BottleDispenser.getInstance(spinner, context, action, action);
    }

    private void addMoneyToDispenser() {
        if (money - moneyToAdd < 0) {
            Toast toast = Toast.makeText(this, "Not enough money!", Toast.LENGTH_SHORT);
            toast.show();
            return;
        }
        else {
            money -= moneyToAdd;
            bd.addMoney(moneyToAdd);
        }
    }

    private void initElements() {
        initTextViews();
        initButtons();
        initSeekBar();
        initSpinner();
    }

    private void initTextViews() {
        action = (TextView)findViewById(R.id.textview_activity);
    }

    private void initButtons() {
        btnAddMoney = (Button)findViewById(R.id.button_add_money);
        btnAddMoney.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addMoneyToDispenser();
            }
        });

        btnPrintReceipt = (Button)findViewById(R.id.button_add_money);
    }

    private void initSeekBar() {
        seekBar = (SeekBar)findViewById(R.id.seekbar_money_amount);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                moneyToAdd = seekBar.getProgress();
                Toast toast = Toast.makeText(context, String.valueOf(moneyToAdd), Toast.LENGTH_SHORT);
                toast.show();
                System.out.println("_LOG: "+moneyToAdd);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    private void initSpinner() {
        spinner = (Spinner)findViewById(R.id.spinner_bottles);
    }
}
