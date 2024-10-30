package com.example.heartdiseaseprediction.Activities.Doctor;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import java.util.Map;
import java.util.HashMap;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.heartdiseaseprediction.Models.Appointment;
import com.example.heartdiseaseprediction.Models.User;
import com.example.heartdiseaseprediction.R;
import com.example.heartdiseaseprediction.SesionManager.AppointmentSessionManager;
import com.example.heartdiseaseprediction.SesionManager.UserSessionManager;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MedicalExaminationActivity extends AppCompatActivity {
    TextView userInfo, weight,height,service, date;
    EditText symptom, medicalhistory;
    TextInputEditText diagnostic;
    ImageView prescriptions;
    UserSessionManager session;
    User user;
    AppointmentSessionManager appointmentSession;
    Appointment appointment;
    SharedPreferences sharedPreferences;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medical_examination);
        appointmentSession=new AppointmentSessionManager((getApplicationContext()));
        session=new UserSessionManager(getApplicationContext());
        appointment=appointmentSession.getAppointmentDetails();
        user=session.getUserDetails();
        sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        String temperater = sharedPreferences.getString("temperater", "");
        String blood_pressure = sharedPreferences.getString("blood_pressure", "");
        String AppointmentKey = sharedPreferences.getString("AppointmentKey", "");

        userInfo=findViewById(R.id.userInfo);
        weight=findViewById(R.id.weight);
        height=findViewById(R.id.height);
        service=findViewById(R.id.service);
        date=findViewById(R.id.date);
        symptom=findViewById(R.id.symptom);
        medicalhistory=findViewById(R.id.medicalHistory);
        diagnostic=findViewById(R.id.diagnostic);

        userInfo.setText(user.getUsername()+" - "+user.getAge()+ " - "+user.getGender());
        weight.setText(user.getWeight());
        height.setText(user.getHeight());
        service.setText(appointment.getService());
        date.setText(appointment.getDate());

        prescriptions=findViewById(R.id.prescriptions);
        prescriptions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(CheckConstraint()==true) {
                    String Sypptom = symptom.getText().toString();
                    String MedicalHistory = medicalhistory.getText().toString();
                    String Diagnostic = diagnostic.getText().toString();

                DatabaseReference appointmentsRef = FirebaseDatabase.getInstance().getReference().child("appointments").child(AppointmentKey);

                // Tạo một HashMap để lưu trữ các giá trị dữ liệu mới
                Map<String, Object> updateValues = new HashMap<>();
                updateValues.put("temperater", temperater);
                updateValues.put("blood_pressure", blood_pressure);
                updateValues.put("Sypptom", Sypptom);
                updateValues.put("MedicalHistory", MedicalHistory);
                updateValues.put("Diagnostic", Diagnostic);
                updateValues.put("status", "finish");
                // Cập nhật dữ liệu cho nút có khóa là AppoimentKey trong bảng "appointments"
                appointmentsRef.updateChildren(updateValues)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                // Xử lý khi cập nhật thành công
                                Log.d("SuccessFUll", "Dữ liệu đã được cập nhật thành công");
                                SendtoPrescriptionsActivity();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                // Xử lý khi cập nhật thất bại
                                Log.e("SuccessFUll", "Lỗi khi cập nhật dữ liệu", e);
                            }
                        });

                }
            }
        });
    }
    public void SendtoPrescriptionsActivity(){
        Intent intent=new Intent(getApplicationContext(), PrescriptionsActivity.class);
        startActivity(intent);
        finish();

    }
    public boolean CheckConstraint(){
        symptom=findViewById(R.id.symptom);
        medicalhistory=findViewById(R.id.medicalHistory);
        diagnostic=findViewById(R.id.diagnostic);
        if(TextUtils.isEmpty(symptom.getText().toString())){
            Toast.makeText(getApplicationContext(), "Please enter symptom", Toast.LENGTH_SHORT).show();
            return false;
        } else if (TextUtils.isEmpty(medicalhistory.getText().toString())) {
            Toast.makeText(getApplicationContext(), "Please enter medical history", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(diagnostic.getText().toString())) {
            Toast.makeText(getApplicationContext(), "Please enter diagnostic", Toast.LENGTH_SHORT).show();
        }
        return true;

    }
}
