package com.example.heartdiseaseprediction.Activities.User;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.heartdiseaseprediction.Models.Appointment;
import com.example.heartdiseaseprediction.Models.User;
import com.example.heartdiseaseprediction.R;
import com.example.heartdiseaseprediction.SesionManager.AppointmentSessionManager;
import com.example.heartdiseaseprediction.SesionManager.UserSessionManager;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class DetailApointmentActivity extends AppCompatActivity{
    LinearLayout Prescriptions;
    TextView weight, height, userInfo,servicename, date, symptom, medicalhistory;
    TextInputEditText diagnostic;
    ImageView Back_btn;
    AppointmentSessionManager appointmentSessionManager;
    UserSessionManager userSessionManager;
    User user;
    Appointment appointment;
    SharedPreferences sharedPreferences;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_apointment);

        userSessionManager= new UserSessionManager(getApplicationContext());;
        appointmentSessionManager =new AppointmentSessionManager(getApplicationContext());
        userInfo=findViewById(R.id.userInfo);
        weight=findViewById(R.id.weight);
        height=findViewById(R.id.height);
        servicename=findViewById(R.id.service);
        date=findViewById(R.id.date);
        symptom=findViewById(R.id.symptom);
        medicalhistory=findViewById((R.id.medicalHistory));
        diagnostic=findViewById(R.id.diagnostic);

        sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        user=userSessionManager.getUserDetails();
        appointment=appointmentSessionManager.getAppointmentDetails();

        weight.setText(user.getWeight());
        height.setText(user.getHeight());
        userInfo.setText(user.getUsername()+" - "+user.getAge()+" - "+user.getGender());
        servicename.setText(appointment.getService());
        date.setText(appointment.getDate());

        Prescriptions=findViewById(R.id.Recept);
        Prescriptions.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ReceiptActivity.class);
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

        String appoinmentID= sharedPreferences.getString("AppointmentKey", "");
        Log.d("AppointmentID",appoinmentID);
        // Trong trường hợp sử dụng Realtime Database
        DatabaseReference appointmentsRef = FirebaseDatabase.getInstance().getReference("appointments");
        appointmentsRef.child(appoinmentID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    symptom.setText(dataSnapshot.child("Sypptom").getValue(String.class));
                    medicalhistory.setText(dataSnapshot.child("MedicalHistory").getValue(String.class));
                    diagnostic.setText(dataSnapshot.child("Diagnostic").getValue(String.class));

                    SharedPreferences sharedPreferences = getSharedPreferences("DoctorPrefs", MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("DoctorName",dataSnapshot.child("doctor").getValue(String.class) );
                    editor.apply();
                } else {
                    // Không có dữ liệu tương ứng với appointmentID
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Xử lý khi truy vấn bị hủy bỏ
            }
        });

    }
}
