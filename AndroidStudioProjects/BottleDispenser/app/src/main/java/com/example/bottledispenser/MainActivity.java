package com.example.bottledispenser;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

import android.content.Context;
import android.view.View;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Button;

import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    //Primitives
    private int moneyToAdd = 1;
    private float money;

    //Others
    private BottleDispenser bd;
    private Context context;

    //UI Elements
    private TextView action;
    private TextView customerMoney;
    private TextView machineMoney;
    private Button btnAddMoney;
    private Button btnPrintReceipt;
    private Button btnReturnMoney;
    private Button btnBuyBottle;
    private SeekBar seekBar;
    private Spinner spinner;
    private Toast toast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Setup view
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = getApplicationContext();

        money = 10;
        initElements();

        bd = BottleDispenser.getInstance(spinner, context, action);
        updateMoneyTexts();
        seekBar.setProgress(1);
    }

    private void addMoneyToDispenser() {
        if (money == 0) {
            toast = Toast.makeText(context, "You don't have any money", Toast.LENGTH_LONG);
            toast.show();
            return;
        }

        if (money - moneyToAdd < 0) {
            toast = Toast.makeText(context, "Not enough money. Adding the rest", Toast.LENGTH_LONG);
            toast.show();
            bd.addMoney(money);
            money = 0;
        }
        else {
            money -= moneyToAdd;
            bd.addMoney(moneyToAdd);
        }
        updateMoneyTexts();
        seekBar.setProgress(1);
    }

    private void returnMoneyFromDispenser() {
        money += bd.returnMoney();
        updateMoneyTexts();
    }

    private void printReceipt() {
        toast = Toast.makeText(context, bd.printReceipt(), Toast.LENGTH_LONG);
        toast.show();
    }

    private void updateMoneyTexts() {
        customerMoney.setText(String.format(Locale.getDefault(),"Money left: %.2f€", money));
        machineMoney.setText(String.format(Locale.getDefault(),"Money added: %.2f€", bd.getMoney()));
    }

    /* Implements UI Element functionality */
    private void initElements() {
        initTextViews();
        initButtons();
        initSeekBar();
        initSpinner();
    }

    private void initTextViews() {
        action = (TextView)findViewById(R.id.textview_activity);
        customerMoney = (TextView)findViewById(R.id.textview_money_left);
        machineMoney = (TextView)findViewById(R.id.textview_money);
    }

    private void initButtons() {
        btnAddMoney = (Button)findViewById(R.id.button_add_money);
        btnAddMoney.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addMoneyToDispenser();
            }
        });

        btnReturnMoney = (Button)findViewById(R.id.button_return_money);
        btnReturnMoney.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                returnMoneyFromDispenser();
            }
        });

        btnBuyBottle = (Button)findViewById(R.id.button_buy_bottle);
        btnBuyBottle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bd.buyBottle();
                updateMoneyTexts();
            }
        });
        btnPrintReceipt = (Button)findViewById(R.id.button_print_receipt);
        btnPrintReceipt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                printReceipt();
            }
        });
    }

    private void initSeekBar() {
        seekBar = (SeekBar)findViewById(R.id.seekbar_money_amount);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                moneyToAdd = seekBar.getProgress();
                toast = Toast.makeText(context, String.valueOf(moneyToAdd), Toast.LENGTH_SHORT);
                toast.show();
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
