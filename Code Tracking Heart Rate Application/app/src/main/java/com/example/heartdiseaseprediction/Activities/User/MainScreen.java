package com.example.heartdiseaseprediction.Activities.User;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.heartdiseaseprediction.R;
import com.example.heartdiseaseprediction.databinding.ActivityMainScreenBinding;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;


public class MainScreen extends AppCompatActivity
{

    ActivityMainScreenBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= ActivityMainScreenBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        replaceFragment(new HomeFragment());

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);

        bottomNavigationView.setSelectedItemId(R.id.home);

        bottomNavigationView.setOnItemSelectedListener(menuItem -> {
            int id = menuItem.getItemId();
            if (id == R.id.home){
                replaceFragment(new HomeFragment());
            } else if (id == R.id.chart) {
                replaceFragment(new ChartFragment());

            } else if (id == R.id.history) {
                replaceFragment(new AppointmentFragment());
            } else if (id == R.id.user) {
                replaceFragment(new ProfileFragment());
            }
            return true;
        });




    }
    public void replaceFragment(Fragment fragment){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction=fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.flFragment,fragment);
        fragmentTransaction.commit();
    }


}