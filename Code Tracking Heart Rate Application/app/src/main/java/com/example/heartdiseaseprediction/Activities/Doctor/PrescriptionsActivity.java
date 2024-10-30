package com.example.heartdiseaseprediction.Activities.Doctor;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.heartdiseaseprediction.Activities.User.AppointmentFragment;
import com.example.heartdiseaseprediction.Models.Medicine;
import com.example.heartdiseaseprediction.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class PrescriptionsActivity extends AppCompatActivity {

    ImageView addDrug,removeDrug, Back_btn;
    Spinner Drugname;
    TextView nameDrug,routine,amount,doctorname;
    Button Finish;
    List<Medicine> Drugs=new ArrayList<>();

    // Tạo một danh sách để lưu trữ các itemView đã tạo
    List<View> itemViews = new ArrayList<>();
    SharedPreferences sharedPreferences;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prescriptions);
        sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        String AppoimentKey = sharedPreferences.getString("AppointmentKey", "");
        String DoctorName = sharedPreferences.getString("Doctor", "");
        List<String> medicineList = new ArrayList<>();
        doctorname=findViewById(R.id.Doctorname);
        doctorname.setText(DoctorName);


        Back_btn=findViewById(R.id.ButtonBack);
        Back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =new Intent(getApplicationContext(), MedicalExaminationActivity.class);
                startActivity(intent);
            }
        });

        addDrug=findViewById(R.id.addDrug);
        addDrug.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ScrollView scrollView = findViewById(R.id.scrollViewdrug);
                LinearLayout linearLayout = findViewById(R.id.layoutContent);

                LayoutInflater inflater = LayoutInflater.from(PrescriptionsActivity.this);
                View itemView = inflater.inflate(R.layout.item_drug, null);

                linearLayout.addView(itemView);

                Spinner Drugname = itemView.findViewById(R.id.drugname);

                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("medicines");
                databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        List<String> medicineList = new ArrayList<>();
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            String medicineName = snapshot.child("name").getValue(String.class);
                            medicineList.add(medicineName);
                        }

                        ArrayAdapter<String> adapter = new ArrayAdapter<>(PrescriptionsActivity.this, android.R.layout.simple_spinner_item, medicineList);
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        Drugname.setAdapter(adapter);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Log.e("Firebase", "Error fetching medicine data", databaseError.toException());
                    }
                });

                // Thêm itemView vào danh sách để lưu trữ
                itemViews.add(itemView);
                // Xóa itemView khỏi danh sách và khỏi linearLayout khi nhấn vào removeDrug
                removeDrug=itemView.findViewById(R.id.removedrug);
                removeDrug.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (!itemViews.isEmpty()) {
                            // Lấy index của itemView được chọn
                            int indexToRemove = itemViews.indexOf(itemView);

                            // Kiểm tra nếu itemView tồn tại trong danh sách
                            if (indexToRemove != -1) {
                                // Xóa itemView khỏi danh sách và khỏi linearLayout
                                itemViews.remove(indexToRemove);
                                linearLayout.removeView(itemView);
                            }
                        }
                    }
                });
            }
        });




        Finish=findViewById(R.id.finish);
        Finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Khởi tạo một tham chiếu tới Firebase Realtime Database
                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();

                // Tạo một tham chiếu đến nút "medicines" trong nút "appointments" với ID là appointmentID
                DatabaseReference medicinesRef = databaseReference.child("appointments").child(AppoimentKey).child("medicines");

                // Xác định ScrollView trong layout XML của bạn và thiết lập ID cho nó là scrollView
                ScrollView scrollView = findViewById(R.id.scrollViewdrug);

                // Get the LinearLayout inside the ScrollView
                LinearLayout linearLayout = (LinearLayout) scrollView.getChildAt(0);

                for (int i = 0; i < linearLayout.getChildCount(); i++) {
                    View item = linearLayout.getChildAt(i);

                    // Lấy các thành phần con của mỗi item
                    Spinner drugNameSpinner = item.findViewById(R.id.drugname);
                    EditText routineEditText = item.findViewById(R.id.routine);
                    EditText amountEditText = item.findViewById(R.id.amount);

                    // Lấy giá trị từ các thành phần con
                    String drugName = drugNameSpinner.getSelectedItem().toString();
                    String routine = routineEditText.getText().toString();
                    String amount = amountEditText.getText().toString();

                    // Tạo một đối tượng Medicine từ các giá trị trên
                    Medicine medicine = new Medicine(drugName, routine, amount);

                    // Đặt ID cho thuốc (ví dụ: tạo một ID duy nhất cho mỗi loại thuốc)
                    String medicineID = medicinesRef.push().getKey();

                    // Thêm đối tượng Medicine vào Firebase Realtime Database
                    if (medicineID != null) {
                        medicinesRef.child(medicineID).setValue(medicine);
                    }
                }

                Toast.makeText(getApplicationContext(), "Successfully!!", Toast.LENGTH_SHORT).show();
                SendtoMainAdminActivity();
            }

        });

    }

    public void SendtoMainAdminActivity(){
        Intent intent=new Intent(getApplicationContext(), AdminMainScreen.class);
        startActivity(intent);
        finish();

    }
}
