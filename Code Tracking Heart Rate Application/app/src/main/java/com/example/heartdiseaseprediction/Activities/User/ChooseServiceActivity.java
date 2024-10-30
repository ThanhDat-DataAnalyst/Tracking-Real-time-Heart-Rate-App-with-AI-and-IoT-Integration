package com.example.heartdiseaseprediction.Activities.User;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;

import com.example.heartdiseaseprediction.R;


public class ChooseServiceActivity extends AppCompatActivity {
    ImageView schedule;
    ImageButton btn_ReturnHome;
    ImageView Back_btn;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chooce_service);
        schedule=findViewById(R.id.schedule);
        schedule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ScheduleActivity.class);
                startActivity(intent);
                finish();
            }
        });
        Back_btn=findViewById(R.id.ButtonBack);
        Back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =new Intent(getApplicationContext(), MainScreen.class);
                startActivity(intent);
            }
        });
//


        Spinner spinner = findViewById(R.id.ChooseService);
        String[] services = {"Cardiology services", "Heart health programs", "Cardiovascular diagnostics",
                "Interventional cardiology", "Electrophysiology services", "Cardiac rehabilitation",
                "Heart surgery", "Heart transplant services", "Cardiothoracic surgery", "Pediatric cardiology services"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, services);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                String selectedService = (String) parentView.getItemAtPosition(position);

                SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("selectedService", selectedService);
                editor.apply();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // Xử lý khi không có mục nào được chọn, nếu cần thiết
            }
        });
    }
}
