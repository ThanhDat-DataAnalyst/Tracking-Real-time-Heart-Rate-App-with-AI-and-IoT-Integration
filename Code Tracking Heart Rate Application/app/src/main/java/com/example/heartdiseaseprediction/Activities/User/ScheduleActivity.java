package com.example.heartdiseaseprediction.Activities.User;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.heartdiseaseprediction.Models.Appointment;
import com.example.heartdiseaseprediction.R;
import com.example.heartdiseaseprediction.SesionManager.UserSessionManager;
import com.google.android.material.textfield.TextInputLayout;
import java.util.Calendar;
import com.example.heartdiseaseprediction.Models.User;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ScheduleActivity extends AppCompatActivity {

    TextView Date;
    TextInputLayout note;
    ImageView Back_btn;
    CheckBox checkBox;
    Button MakeApointment;
    int year, month, day;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference serviceRef = database.getReference("service");
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    FirebaseUser currentUser = mAuth.getCurrentUser();
    String userId = currentUser.getUid();
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule);

        Back_btn=findViewById(R.id.ButtonBack);
        Back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =new Intent(getApplicationContext(), MainScreen.class);
                startActivity(intent);
            }
        });
        //lay biến service đã chọn
        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        String selectedService = sharedPreferences.getString("selectedService", "");

        // add item vô spinner
        List<String> doctorList = new ArrayList<>();
        // Thực hiện truy vấn Firebase Database để lấy danh sách bác sĩ phụ trách dịch vụ đã chọn
        serviceRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot serviceSnapshot : dataSnapshot.getChildren()) {
                    String serviceValue = serviceSnapshot.getValue(String.class);

                    if (serviceValue != null && serviceValue.trim().equals(selectedService.trim())) {
                        // Nếu giá trị khớp, lấy tên của bác sĩ và thêm vào danh sách
                        String doctorName = serviceSnapshot.getKey();
                        doctorList.add(doctorName);
                        Log.d("doctorName", doctorName);
                    }
                }
                if (!doctorList.isEmpty()) {
                    // Nếu có, tạo và đặt ArrayAdapter cho Spinner
                    ArrayAdapter<String> doctorAdapter = new ArrayAdapter<>(ScheduleActivity.this, android.R.layout.simple_spinner_item, doctorList);
                    doctorAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    Spinner doctorSpinner = findViewById(R.id.Doctor);
                    doctorSpinner.setAdapter(doctorAdapter);
                } else {
                    // Nếu không có bác sĩ nào được tìm thấy, ghi log
                    Log.d("No doctors found", "No doctors found");
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        Date= findViewById(R.id.date);
        ImageButton imageButton = findViewById(R.id.imageButton2);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Lấy ngày hiện tại
                Calendar calendar = Calendar.getInstance();
                year = calendar.get(Calendar.YEAR);
                month = calendar.get(Calendar.MONTH);
                day = calendar.get(Calendar.DAY_OF_MONTH);

                // Tạo DatePickerDialog
                DatePickerDialog datePickerDialog = new DatePickerDialog(ScheduleActivity.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int selectedYear,
                                                  int selectedMonth, int selectedDay) {
                                // Lưu giá trị ngày tháng năm được chọn
                                year = selectedYear;
                                month = selectedMonth;
                                day = selectedDay;

                                // Hiển thị ngày tháng năm đã chọn
                                Toast.makeText(ScheduleActivity.this, "Selected Date: " + day + "/" + (month + 1) + "/" + year, Toast.LENGTH_SHORT).show();
                                // Hiển thị ngày tháng năm đã chọn trong TextView
                                String selectedDate = day + "/" + (month + 1) + "/" + year;
                                Date.setText(selectedDate);
                            }
                        }, year, month, day);

                // Hiển thị DatePickerDialog
                datePickerDialog.show();
            }
        });

        MakeApointment=findViewById(R.id.makeApointment);
        MakeApointment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    TextView Date=findViewById(R.id.date);
                    Spinner Doctor=findViewById(R.id.Doctor);
                    TextInputEditText Note=findViewById(R.id.note);
                    SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
                    String selectedService = sharedPreferences.getString("selectedService", "Heart check service");

                    String date= Date.getText().toString();
                    String doctor=Doctor.getSelectedItem().toString();
                    String note = Note.getText().toString();
                    // Lấy tham chiếu đến Firebase Database
                    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
                    // Tạo một ID ngẫu nhiên cho mỗi lịch hẹn
                    FirebaseAuth auth = FirebaseAuth.getInstance();
                    UserSessionManager session = new UserSessionManager(getApplicationContext());
                    User currentUser = session.getUserDetails();
                    User userA= new User(currentUser.getUserID(), currentUser.getEmail(), currentUser.getPassword(), currentUser.getUsername(), currentUser.getAge(), currentUser.getHeight(), currentUser.getWeight(), currentUser.getGender());
                    Log.d("UserInfo",userA.getUserID()+ " "+userA.getGender()+" "+userA.getAge()+" "+userA.getWeight()+" "+userA.getHeight());
                    String appointmentId = databaseReference.child("users").child(auth.getCurrentUser().getUid()).child("appointments").push().getKey();
                    String userid_status =userId+"_incoming";
                    Appointment appointment= new Appointment(date, doctor,note,"incoming",selectedService,userA,userid_status);
                    Log.d("selectedService",selectedService);
                    // Lưu vào Appointment trong users
                    databaseReference.child("appointments").child(appointmentId).setValue(appointment)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Toast.makeText(ScheduleActivity.this, "Schedule Successfully!", Toast.LENGTH_SHORT).show();

                                    Intent mainIntent = new Intent(getApplicationContext(), MainScreen.class);
                                    startActivity(mainIntent);
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.e("TAG", "Error adding appointment: " + e.getMessage());
                                    Toast.makeText(ScheduleActivity.this, "Schedule Fail!", Toast.LENGTH_SHORT).show();
                                }
                            });


            }
        });

    }
//    private Boolean CheckAvalable(){
//        if (isFieldsFilled()) {
//            Toast.makeText(ScheduleActivity.this, "Scheduled successfully", Toast.LENGTH_SHORT).show();
//            return true;
//        } else {
//            Toast.makeText(ScheduleActivity.this, "You need to fill it out completely", Toast.LENGTH_SHORT).show();
//            return false;
//        }
//    }
//    private boolean isFieldsFilled() {
//        note = findViewById(R.id.note);
//        checkBox = findViewById(R.id.confirm);
//        TextInputLayout noteLayout = findViewById(R.id.note);
//        TextView date = findViewById(R.id.date);
//        // Lấy EditText từ mỗi TextInputLayout
//        EditText noteEditText = noteLayout.getEditText();
//
//        // Kiểm tra xem TextInputLayout, TextView và CheckBox đã được điền đầy đủ và đánh dấu hay không
//        boolean isNoteFilled = noteEditText != null && !TextUtils.isEmpty(noteEditText.getText());
//        boolean isDateFilled = !TextUtils.isEmpty(date.getText());
//        boolean isChecked = checkBox.isChecked();
//
//// Kiểm tra xem tất cả các điều kiện đã đáp ứng
//        if (isNoteFilled && isDateFilled && isChecked) {
//            return true;
//        } else {
//            return false;
//        }
//    }


}
