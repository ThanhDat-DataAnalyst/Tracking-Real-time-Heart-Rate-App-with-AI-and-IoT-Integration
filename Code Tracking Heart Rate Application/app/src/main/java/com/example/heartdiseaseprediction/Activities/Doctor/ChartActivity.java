package com.example.heartdiseaseprediction.Activities.Doctor;
import androidx.appcompat.app.AppCompatActivity;


import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;


import com.example.heartdiseaseprediction.Models.User;
import com.example.heartdiseaseprediction.R;
import com.example.heartdiseaseprediction.SesionManager.UserSessionManager;
import com.github.mikephil.charting.charts.BarChart;

import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;
import java.util.concurrent.TimeUnit;

import com.github.mikephil.charting.utils.ColorTemplate;

public class ChartActivity extends AppCompatActivity{
    TextView Date;
    ImageView Back_btn;
    ImageView imageButton;
    int year, month, day;
    String dateSelected;
    // variable for our bar chart
    BarChart barChart;

    // variable for our bar data.
    BarData barData;

    // variable for our bar data set.
    BarDataSet barDataSet;
    // array list for storing entries.
    ArrayList barEntriesArrayList;
    ImageButton btn_ReturnHome;
    private TimeUnit TimeUnit;
    UserSessionManager session;
    User user;
    ImageView MakeApointment;
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    FirebaseUser currentUser = mAuth.getCurrentUser();
    String userId;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chart);

        Back_btn=findViewById(R.id.ButtonBack);
        Back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =new Intent(getApplicationContext(), AdminMainScreen.class);
                startActivity(intent);
            }
        });
        session=new UserSessionManager(getApplicationContext());
        user=session.getUserDetails();
        userId=user.getUserID();
        Log.d("userId",userId);
        // initializing variable for bar chart.
        barChart = findViewById(R.id.bar_chart);
        CallBarChart();


        Calendar calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);
        dateSelected = String.format("%04d%02d%02d", year, month + 1, day);

        Date= findViewById(R.id.date);
        String selectedDate = day + "/" + (month + 1) + "/" + year;
        Date.setText(selectedDate);

        imageButton = findViewById(R.id.dateicon);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                // Tạo DatePickerDialog
                DatePickerDialog datePickerDialog = new DatePickerDialog(ChartActivity.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int selectedYear,
                                                  int selectedMonth, int selectedDay) {
                                // Lưu giá trị ngày tháng năm được chọn
                                year = selectedYear;
                                month = selectedMonth;
                                day = selectedDay;

                                // Hiển thị ngày tháng năm đã chọn
                                Toast.makeText(getApplicationContext(), "Selected Date: " + day + "/" + (month + 1) + "/" + year, Toast.LENGTH_SHORT).show();
                                // Hiển thị ngày tháng năm đã chọn trong TextView
                                String selectedDate = day + "/" + (month + 1) + "/" + year;
                                dateSelected = String.format("%04d%02d%02d", year, month + 1, day);

                                Log.d("dateSelected",dateSelected);
                                Date.setText(selectedDate);
                            }
                        }, year, month, day);

                // Hiển thị DatePickerDialog
                datePickerDialog.show();
                barEntriesArrayList.clear();
                CallBarChart();
            }
        });

    }
    private void getBarEntries() {
        // creating a new array list
        barEntriesArrayList = new ArrayList<>();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("users");

// Lấy dữ liệu từ node "cardiacInfo"
        myRef.child(userId).child("cardiacInfo").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Lấy dữ liệu từ DataSnapshot
                for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
                    String key = childSnapshot.getKey();
                    if (key != null && key.length() >= 8) {
                        String keyDate = key.substring(0, 8);
                        if (keyDate.equals(dateSelected)) {

                            String value = childSnapshot.getValue(String.class);
                            float floatValue = Float.parseFloat(value);
                            int intValue = Math.round(floatValue);
                            // Trích xuất giá trị giờ từ chuỗi timestamp
                            String hourString = key.substring(8, 10);
                            // Chuyển đổi giá trị giờ từ chuỗi sang kiểu số nguyên
                            int hour = Integer.parseInt(hourString);
                            System.out.println("Hour: " + hour + ", Heat beat: " + intValue);
                            barEntriesArrayList.add(new BarEntry(hour, intValue));
                            barChart.invalidate();
                        }
                    }

                }
                setupBarChart();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e("FirebaseError", "Firebase database error: " + databaseError.getMessage());
            }

        });

    }
    private void setupBarChart() {
        barDataSet = new BarDataSet(barEntriesArrayList, "Heart Beat");
        barData = new BarData(barDataSet);
        barChart.setData(barData);
        barDataSet.setColors(ColorTemplate.MATERIAL_COLORS);
        barDataSet.setValueTextColor(Color.BLACK);
        barDataSet.setValueTextSize(16f);
        barDataSet.setColors(Color.argb(225,50, 151, 168));
        barChart.getDescription().setEnabled(false);
    }
    public void CallBarChart(){

        getBarEntries();


        // creating a new bar data set.
        barDataSet = new BarDataSet(barEntriesArrayList, "Geeks for Geeks");

        // creating a new bar data and
        // passing our bar data set.
        barData = new BarData(barDataSet);

        // below line is to set data
        // to our bar chart.
        barChart.setData(barData);

        // adding color to our bar data set.
        barDataSet.setColors(ColorTemplate.MATERIAL_COLORS);

        // setting text color.
        barDataSet.setValueTextColor(Color.BLACK);

        // setting text size
        barDataSet.setValueTextSize(16f);
        barChart.getDescription().setEnabled(false);
    }
}