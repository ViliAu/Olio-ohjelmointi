package com.example.bottledispenser;

import java.util.ArrayList;
import java.util.Scanner;

import android.content.Context;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class BottleDispenser {

    // Primitives
    private int bottles;
    private float money;

    // Other
    ArrayList<Bottle> bottle_array;
    Context context;

    //UI Elements
    TextView contents;
    TextView textField2;
    Spinner spin;

    private static BottleDispenser bd;
    public static BottleDispenser getInstance(Spinner spin, Context c, TextView txtview, TextView l) {
        if (bd == null) {
            bd = new BottleDispenser(spin, c, txtview, l);
        }
        return bd;
    }

    // Overload for Customer class
    public static BottleDispenser getInstance() {
        return bd;
    }

    public BottleDispenser(Spinner spin, Context c, TextView txtview, TextView l) {
        this.spin = spin;
        contents = txtview;
        context = c;
        bottles = 5;
        money = 0;
        // Initialize the array
        bottle_array = new ArrayList<Bottle>();

        //Add bottles
        bottle_array.add(new Bottle("Pepsi Max", "Pepsi", 0.3d, 0.5f, 1.8f));
        bottle_array.add(new Bottle("Pepsi Max", "Pepsi", 0.3d, 1.5f, 2.2f));
        bottle_array.add(new Bottle("Coca-Cola Zero", "Coca-Cola", 0.3d, 0.5f, 2.0f));
        bottle_array.add(new Bottle("Coca-Cola Zero", "Coca-Cola", 0.3d, 1.5f, 2.5f));
        bottle_array.add(new Bottle("Fanta Zero", "Coca-Cola", 0.3d, 0.5f, 1.95f));
    }

    public void addMoney(float money) {
        this.money += money;
        System.out.println("Klink! Added more money!");
    }

    public void buyBottle() {
        if (bottles > 0 && money > bottle_array.get(0).price) {
            transaction();
        }
        else if (bottles > 0) {
            System.out.println("Add money first!");
        }
        else if (money > bottle_array.get(0).price) {
            System.out.println("Out of bottels!");
        }
    }

    public void returnMoney() {
        System.out.print("Klink klink. Money came out! ");
        System.out.printf("You got %.2f€ back", money);
        System.out.println("");
        money = 0;
    }

    public void listBottles() {
        for (int i = 0; i < bottles; i++) {
            System.out.println(i+1+". Name: "+bottle_array.get(i).getName());
            System.out.println("\tSize: "+bottle_array.get(i).size+"\tPrice: "+bottle_array.get(i).price);
        }
    }
    public void transaction() {
        Bottle b;
        int choice;

        listBottles();
        System.out.print("Your choice: ");
        Scanner scanner = new Scanner(System.in);
        choice = scanner.nextInt();
        b = bottle_array.get(choice-1);

        bottles -= 1;
        bottle_array.remove(choice-1);
        money -= b.price;
        System.out.println("KACHUNK! " + b.getName() + " came out of the dispenser!");
    }
}