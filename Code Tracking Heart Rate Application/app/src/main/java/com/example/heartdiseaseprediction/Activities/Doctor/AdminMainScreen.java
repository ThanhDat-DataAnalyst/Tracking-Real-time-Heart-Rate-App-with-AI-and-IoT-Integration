package com.example.heartdiseaseprediction.Activities.Doctor;

import androidx.appcompat.app.AppCompatActivity;
import java.util.Map;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.heartdiseaseprediction.Activities.User.DetailApointmentActivity;
import com.example.heartdiseaseprediction.SesionManager.DoctorSessionManager;
import com.example.heartdiseaseprediction.Activities.LoginActivity;
import com.example.heartdiseaseprediction.Models.Appointment;
import com.example.heartdiseaseprediction.Models.Doctor;
import com.example.heartdiseaseprediction.R;
import com.example.heartdiseaseprediction.SesionManager.AppointmentSessionManager;
import com.example.heartdiseaseprediction.SesionManager.UserSessionManager;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;


public class AdminMainScreen extends AppCompatActivity {

    TextView username, heartRateTextView;
    ;
    ImageButton LogOutButton;
    ImageView showMoreChartButton;
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    FirebaseUser currentUser = mAuth.getCurrentUser();
    String userId = currentUser.getUid();

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_main_sceen);

        DoctorSessionManager session = new DoctorSessionManager(getApplicationContext());
        // Lấy thông tin Doctor từ session và lưu vào biến doctor
        Doctor doctor = session.getDoctorDetails();
        Log.d("doctor",doctor.getUsername());
        UserSessionManager sessionUser=new UserSessionManager(getApplicationContext());
        //username = findViewById(R.id.name);
        //heartRateTextView = findViewById(R.id.heart_rate_value);
        //getdata();
        LogOutButton = findViewById(R.id.LogOutBtn);
        LogOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intent);
                finish();
                Toast.makeText(AdminMainScreen.this, "LogOut Successfully", Toast.LENGTH_SHORT).show();
            }
        });


        // Lấy dữ liệu từ Firebase
        //getdata();
        //them item vô scrollview history
        ScrollView scrollView = findViewById(R.id.scrollItemIncoming);
        LinearLayout linearLayout = new LinearLayout(this);
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        scrollView.addView(linearLayout);
        DatabaseReference appointmentsRef = FirebaseDatabase.getInstance().getReference().child("appointments");


        // Tạo truy vấn để lấy tất cả các cuộc hẹn có userID và status mong muốn
        Query appointmentsQuery = appointmentsRef.orderByChild("status").equalTo("incoming");

        appointmentsQuery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot appointmentSnapshot : dataSnapshot.getChildren()) {
                    // Lấy dữ liệu từ dataSnapshot
                    String nameDoctor = appointmentSnapshot.child("doctor").getValue(String.class);

                    if (doctor.getUsername().equals(nameDoctor)) {
                        String nameStr = appointmentSnapshot.child("user").child("username").getValue(String.class);
                        Log.d("usernameU",nameStr);
                        String service = appointmentSnapshot.child("service").getValue(String.class);
                        String date=appointmentSnapshot.child("date").getValue(String.class);
                        String IDAppointment =appointmentSnapshot.getKey();


                        // Tạo view mới từ layout XML
                        LayoutInflater inflater = LayoutInflater.from(AdminMainScreen.this);
                        View itemView = inflater.inflate(R.layout.item_user, null);
                        // Đặt dữ liệu vào các TextView trong itemView
                        TextView Name = itemView.findViewById(R.id.name);
                        Name.setText(service);
                        TextView UserName = itemView.findViewById(R.id.username);
                        UserName.setText(nameStr);
                        TextView AppointmentID = itemView.findViewById(R.id.IDAppointment);
                        AppointmentID.setText(IDAppointment);

                        TextView Date = itemView.findViewById(R.id.date);
                        Date.setText(date);

                        showMoreChartButton = itemView.findViewById(R.id.show_more_chart);
                        // Lắng nghe sự kiện click cho nút showMoreChartButton
                        showMoreChartButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                TextView Name = itemView.findViewById(R.id.username);
                                String username = Name.getText().toString();
                                SaveSessionUser(username);
                                SendToChartActivity();
                            }
                        });
                        LinearLayout Layout=itemView.findViewById(R.id.appointmentInfo);
                        Layout.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                TextView AppointmentID = itemView.findViewById(R.id.IDAppointment);
                                TextView Name = itemView.findViewById(R.id.username);
                                String username = Name.getText().toString();
                                SaveSessionUser(username);
                                SaveSessionAppointment(IDAppointment);
                                SendToPreCheckupActivity();
                                SaveKeyAppointment(AppointmentID.getText().toString());
                            }
                        });


                        linearLayout.addView(itemView);
                    }

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
        //them item vô scrollview incoming
        ScrollView scrollView1 = findViewById(R.id.scrollItemHistory);
        LinearLayout linearLayout1 = new LinearLayout(this);
        linearLayout1.setOrientation(LinearLayout.VERTICAL);
        scrollView1.addView(linearLayout1);
        // Tạo truy vấn để lấy tất cả các cuộc hẹn có userID và status mong muốn
        // Tạo truy vấn để lấy tất cả các cuộc hẹn có userID và status mong muốn
        Query appointmentsQueryEnd = appointmentsRef.orderByChild("status").equalTo("finish");

        appointmentsQueryEnd.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot appointmentSnapshot : dataSnapshot.getChildren()) {
                    // Lấy dữ liệu từ dataSnapshot
                    String nameDoctor = appointmentSnapshot.child("doctor").getValue(String.class);

                    if (doctor.getUsername().equals(nameDoctor)) {
                        String nameStr = appointmentSnapshot.child("user").child("username").getValue(String.class);
                        Log.d("usernameU",nameStr);
                        String service = appointmentSnapshot.child("service").getValue(String.class);
                        String date=appointmentSnapshot.child("date").getValue(String.class);
                        String IDAppointment =appointmentSnapshot.getKey();
                        // Tạo view mới từ layout XML
                        LayoutInflater inflater = LayoutInflater.from(AdminMainScreen.this);
                        View itemView = inflater.inflate(R.layout.item_user, null);
                        // Đặt dữ liệu vào các TextView trong itemView
                        TextView Name = itemView.findViewById(R.id.name);
                        Name.setText(service);
                        TextView UserName = itemView.findViewById(R.id.username);
                        UserName.setText(nameStr);
                        TextView AppointmentID = itemView.findViewById(R.id.IDAppointment);
                        AppointmentID.setText(IDAppointment);

                        TextView Date = itemView.findViewById(R.id.date);
                        Date.setText(date);

                        showMoreChartButton = itemView.findViewById(R.id.show_more_chart);
                        // Lắng nghe sự kiện click cho nút showMoreChartButton
                        showMoreChartButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                TextView Name = itemView.findViewById(R.id.username);
                                String username = Name.getText().toString();
                                SaveSessionUser(username);
                                SendToChartActivity();
                            }
                        });
                        LinearLayout linearLayout2=itemView.findViewById(R.id.appointmentInfo);
                        linearLayout2.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                TextView Name = itemView.findViewById(R.id.username);
                                String username = Name.getText().toString();
                                SaveSessionUser(username);
                                SaveSessionAppointment(IDAppointment);
                                SaveKeyAppointment(AppointmentID.getText().toString());
                                SendToDetailActivity();
                            }
                        });


                        linearLayout1.addView(itemView);
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

    }
    private String getLatestHeartbeat(Map<String, String> cardiacInfo) {
        long latestTimeMillis = Long.MIN_VALUE;
        String latestHeartbeat = null;

        for (Map.Entry<String, String> entry : cardiacInfo.entrySet()) {
            String timeString = entry.getKey();
            String heartbeat = entry.getValue();

            // Chuyển đổi thời gian từ định dạng chuỗi sang millis
            long timeMillis = Long.parseLong(timeString);

            // Nếu thời gian của cặp này lớn hơn thời gian mới nhất đã biết
            if (timeMillis > latestTimeMillis) {
                // Cập nhật thời gian mới nhất và giá trị nhịp tim mới nhất
                latestTimeMillis = timeMillis;
                latestHeartbeat = heartbeat;
            }
        }

        // Trả về nhịp tim mới nhất
        return latestHeartbeat;
    }
//    private void getdata() {
//        DatabaseReference databaseRef = FirebaseDatabase.getInstance().getReference().child("users");
//
//        databaseRef.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                LinearLayout patientListLayout = findViewById(R.id.patientListLayout);
//                patientListLayout.removeAllViews(); // Clear previous views
//
//                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
//                    User user = snapshot.getValue(User.class);
//                    if (user != null) {
//                        View itemView = LayoutInflater.from(AdminMainScreen.this).inflate(R.layout.item_user, null);
//                        TextView nameTextView = itemView.findViewById(R.id.name);
//                        TextView heartRateTextView = itemView.findViewById(R.id.heart_rate_value);
//                        nameTextView.setText(user.getUsername());
//                        // Set heart rate based on user's data
//                        String latestHeartbeat = getLatestHeartbeat(user.getCardiacInfo());
//                        heartRateTextView.setText("Heart beat: " + latestHeartbeat + " bpm");
//                        patientListLayout.addView(itemView);
//                    }
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//                Log.d("TAG", "Database Error: " + databaseError.getMessage());
//            }
//        });
//    }
    private void SaveSessionUser(String usernameStr){
        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference().child("users");
        Query userQuery = userRef.orderByChild("username").equalTo(usernameStr);
        Log.d("Username:",usernameStr);
        userQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Kiểm tra xem có dữ liệu trả về không
                if (dataSnapshot.exists()) {
                    // Lấy dữ liệu từ dataSnapshot
                    DataSnapshot userSnapshot = dataSnapshot.getChildren().iterator().next();
                    String Uid =userSnapshot.getKey();
                    String password =userSnapshot.child("password").getValue(String.class);
                    String email = userSnapshot.child("email").getValue(String.class);
                    String age = userSnapshot.child("age").getValue(String.class);
                    String height = userSnapshot.child("height").getValue(String.class);
                    String weight = userSnapshot.child("weight").getValue(String.class);
                    String gender = userSnapshot.child("gender").getValue(String.class);
                    // Lưu thông tin user vào UserSession
                    UserSessionManager userSession = new UserSessionManager(getApplicationContext());
                    userSession.createUserLoginSession(Uid, email, password, usernameStr, age, height, weight, gender);
                    // Chuyển đến màn hình ChartActivity

                } else {
                    // Không tìm thấy user
                    Toast.makeText(getApplicationContext(), "User not found", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Xử lý khi có lỗi xảy ra
                Toast.makeText(getApplicationContext(), "Error: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void SaveSessionAppointment(String IDAppointment){
        DatabaseReference appointmentsRef = FirebaseDatabase.getInstance().getReference().child("appointments");
        Query appointmentsQuery = appointmentsRef.orderByKey().equalTo(IDAppointment);
        appointmentsQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Kiểm tra xem có dữ liệu trả về không
                if (dataSnapshot.exists()) {
                    // Lấy dữ liệu từ dataSnapshot
                    DataSnapshot Snapshot = dataSnapshot.getChildren().iterator().next();
                    String service =Snapshot.child("service").getValue(String.class);
                    String date =Snapshot.child("date").getValue(String.class);
                    AppointmentSessionManager Session = new AppointmentSessionManager(getApplicationContext());
                    Session.createAppointmentSession(new Appointment(date,service));

                } else {
                    Toast.makeText(getApplicationContext(), "Appoiment not found", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Xử lý khi có lỗi xảy ra
                Toast.makeText(getApplicationContext(), "Error: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
    public void SendToChartActivity(){
        Intent intent=new Intent(getApplicationContext(),ChartActivity.class);
        startActivity(intent);
        finish();
    }
    public void SendToPreCheckupActivity(){
        Intent intent=new Intent(getApplicationContext(), PreCheckUpProcessActivity.class);
        startActivity(intent);
        finish();
    }
    public void SendToDetailActivity(){
        Intent intent=new Intent(getApplicationContext(), AdminDetailAppointmentActivity.class);
        startActivity(intent);
        finish();
    }
    public void SaveKeyAppointment(String AppoimentID){
        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("AppointmentKey", AppoimentID);
        editor.apply();
    }
}