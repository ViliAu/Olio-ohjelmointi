package com.example.bankapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.RelativeLayout;

import com.example.bankapplication.databinding.ActivityAdminBinding;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class AdminActivity extends AppCompatActivity {

    // CUSTOMERS
    private ArrayList<Customer> customers = new ArrayList<>();

    // UI ELEMENTS
    private ActivityAdminBinding binding;
    private RecyclerView recycler;
    private RecyclerView.Adapter recyclerAdapter;
    private RecyclerView.LayoutManager recyclerLayoutManager;
    private ArrayList<AdminCustomersCard> recyclerCardList = new ArrayList<>();
    private FragmentManager fm;
    private FragmentTransaction ft;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAdminBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Initialize view to start fragment
        fm = getSupportFragmentManager();
        if (fm != null) {
            ft = fm.beginTransaction();
            ft.add(R.id.fragment_container, new StartAdminFragment());
            ft.commit();
        }
        initElements();
    }

    void initElements() {
    }
}
