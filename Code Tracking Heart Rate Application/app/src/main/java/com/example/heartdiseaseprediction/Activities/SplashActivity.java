package com.example.heartdiseaseprediction.Activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.example.heartdiseaseprediction.Activities.Doctor.AdminMainScreen;
import com.example.heartdiseaseprediction.Activities.User.MainScreen;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SplashActivity extends AppCompatActivity {

    int SPLASH_TIME_OUT=3000;
    SharedPreferences sharedPreferences;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // Kiểm tra xem người dùng đã đăng nhập chưa
                FirebaseAuth mAuth = FirebaseAuth.getInstance();
                FirebaseUser currentUser = mAuth.getCurrentUser();
                if (currentUser != null) {
                    checkUserRoleAndRedirect();
                } else {
                    Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                    startActivity(intent);
                    finish();
                }
                checkUserRoleAndRedirect();
            }
        },SPLASH_TIME_OUT);

    }

    private void checkUserRoleAndRedirect() {
        sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        String userRole = sharedPreferences.getString("userRole", "");
        Log.d("userRole",userRole);
        if (userRole.startsWith("Doctor")) {
            // Chuyển đến AdminMainScreen
            Intent intent = new Intent(getApplicationContext(), AdminMainScreen.class);
            startActivity(intent);
            finish();
        } else  {
            // Chuyển đến MainScreen
            Intent intent = new Intent(getApplicationContext(), MainScreen.class);
            startActivity(intent);
            finish();
        }
    }
}
