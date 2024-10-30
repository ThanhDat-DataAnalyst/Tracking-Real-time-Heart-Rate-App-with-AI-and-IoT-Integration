package com.example.heartdiseaseprediction.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.heartdiseaseprediction.Activities.User.MainScreen;
import com.example.heartdiseaseprediction.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


import androidx.appcompat.app.AppCompatActivity;

public class SignUpInformationActivity extends AppCompatActivity {
    Spinner Gender;
    EditText Weight, Height,Age;
    ImageView Finish;
    // Lấy ID của người dùng đang đăng nhập từ Firebase Authentication
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    FirebaseUser currentUser = mAuth.getCurrentUser();
    String userId = currentUser.getUid();
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup_information);
        Spinner spinner = findViewById(R.id.gender);
        String[] services = {"Male","Female"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, services);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        Gender=findViewById(R.id.gender);
        Weight=findViewById(R.id.weight);
        Height=findViewById(R.id.height);
        Finish=findViewById(R.id.finish);
        Age=findViewById(R.id.age);
        Finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String gender=Gender.getSelectedItem().toString();
                String weight=Weight.getText().toString();
                String height=Height.getText().toString();
                String age=Age.getText().toString();
                // Thêm giá trị gender, weight và height vào nút "users" cho người dùng đang đăng nhập trong Firebase Realtime Database
                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference usersRef = database.getReference("users").child(userId);

                usersRef.child("gender").setValue(gender);
                usersRef.child("weight").setValue(weight);
                usersRef.child("height").setValue(height);
                usersRef.child("age").setValue(age);

                Toast.makeText(SignUpInformationActivity.this, "Sign Up Successfully!", Toast.LENGTH_SHORT).show();
                Intent MainIntent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(MainIntent);
            }
        });


    }

}
