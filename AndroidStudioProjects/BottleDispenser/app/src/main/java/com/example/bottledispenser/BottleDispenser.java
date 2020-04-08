package com.example.bottledispenser;

import java.util.ArrayList;
import java.util.Locale;

import android.content.Context;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

public class BottleDispenser {

    // Primitives
    private float money;

    // Other
    private ArrayList<Bottle> bottleList;
    private ArrayAdapter<Bottle> bottleAdapter;
    private Context context;
    private FileHandler fh;

    //UI Elements
    private TextView contents;
    private Spinner spinner;

    private static BottleDispenser bd;
    public static BottleDispenser getInstance(Spinner spin, Context c, TextView contents) {
        if (bd == null) {
            bd = new BottleDispenser(spin, c, contents);
        }
        return bd;
    }

    private BottleDispenser(Spinner spin, Context c, TextView txtview) {
        fh = new FileHandler(c);
        this.spinner = spin;
        contents = txtview;
        context = c;
        money = 0;
        // Initialize the array
        bottleList = new ArrayList<Bottle>(5);
        addBottles();

        bottleAdapter = new ArrayAdapter<Bottle>(context, R.layout.spinner_entry, bottleList);
        bottleAdapter.setDropDownViewResource(R.layout.spinner_entry);
        spin.setAdapter(bottleAdapter);
    }

    private void addBottles() {
        bottleList.add(new Bottle("Pepsi Max", "Pepsi", 0.3d, 0.5f, 1.8f));
        bottleList.add(new Bottle("Pepsi Max", "Pepsi", 0.3d, 1.5f, 2.2f));
        bottleList.add(new Bottle("Coca-Cola Zero", "Coca-Cola", 0.3d, 0.5f, 2.0f));
        bottleList.add(new Bottle("Coca-Cola Zero", "Coca-Cola", 0.3d, 1.5f, 2.5f));
        bottleList.add(new Bottle("Fanta Zero", "Coca-Cola", 0.3d, 0.5f, 1.95f));
    }

    public void addMoney(float money) {
        this.money += money;
        contents.setText(String.format(Locale.getDefault(),"Klink! Added more money!\n Amount: %.2f€", money));
    }

    public void buyBottle() {
        Bottle b = bottleAdapter.getItem(spinner.getSelectedItemPosition());
        if (b == null) {
            contents.setText("No bottles left to buy.");
            return;
        }
        else if (money < b.price) {
            contents.setText("Not enough money. Insert more.");
            return;
        }

        money -= b.price;
        bottleList.remove(b);
        fh.writeFile("receipt.txt", String.format(Locale.getDefault(), "RECEIPT:\n\nPRICE: %.2f€\nMANUFACTURER: %s\nNAME: %s\nSIZE: %.2fl\nENERGY: %.2fkCal",
                b.price, b.getManufacturer(), b.getName(), b.size, b.getEnergy()));
        bottleAdapter.notifyDataSetChanged();
        contents.setText("KACHUNK! " + b.getName() + " came out of the dispenser!");
    }

    public float returnMoney() {
        if (money == 0) {
            contents.setText("No money left in the dispenser!");
            return 0;
        }
        contents.setText(String.format(Locale.getDefault(),"Klink klink. Money came out!\nYou got %.2f€ back\n", money));
        float returnMoney = money;
        money = 0;
        return returnMoney;
    }

    public String printReceipt() {
        ArrayList<String> fc = new ArrayList<String>();
        String cont ="";
        fc = fh.readFile("receipt.txt");
        if (fc.size() == 0)
            cont = "No receipt found.";
        else {
            for (String s : fc)
                cont += s + "\n";
        }
        return cont;
    }

    public float getMoney() {
        return money;
    }
}