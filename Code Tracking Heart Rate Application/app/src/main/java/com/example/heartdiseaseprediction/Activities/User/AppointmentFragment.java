package com.example.heartdiseaseprediction.Activities.User;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.heartdiseaseprediction.Models.Appointment;
import com.example.heartdiseaseprediction.R;
import com.example.heartdiseaseprediction.SesionManager.AppointmentSessionManager;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class AppointmentFragment extends Fragment {

    CardView upcoming,history;
    ImageView MakeApointment;
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    FirebaseUser currentUser = mAuth.getCurrentUser();
    String userId = currentUser.getUid();
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    TextView service,date;
    DatabaseReference appointmentsRef;
    View rootView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_appointment, container, false);

        upcoming=rootView.findViewById(R.id.upcoming);
        upcoming.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), DetailApointmentActivity.class);
                startActivity(intent);
                getActivity().finish();
            }
        });
        history=rootView.findViewById(R.id.History);
        upcoming.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), DetailApointmentActivity.class);
                startActivity(intent);
                getActivity().finish();
            }
        });
        MakeApointment =rootView.findViewById(R.id.makeApointment);
        MakeApointment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ChooseServiceActivity.class);
                startActivity(intent);
                getActivity().finish();
            }
        });

        //them item vô scrollview history
        ScrollView scrollView = rootView.findViewById(R.id.scrollItemHistory);
        LinearLayout linearLayout = new LinearLayout(getActivity());
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        scrollView.addView(linearLayout);
        DatabaseReference appointmentsRef = FirebaseDatabase.getInstance().getReference().child("appointments");


        // Tạo truy vấn để lấy tất cả các cuộc hẹn có userID và status mong muốn
        Query appointmentsQuery = appointmentsRef.orderByChild("status").equalTo( "finish");

        appointmentsQuery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot appointmentSnapshot : dataSnapshot.getChildren()) {
                    if(appointmentSnapshot.child("user").child("userID").getValue(String.class).equals(userId)){

                        // Lấy dữ liệu từ dataSnapshot
                        String Service = appointmentSnapshot.child("service").getValue(String.class);
                        String Date = appointmentSnapshot.child("date").getValue(String.class);
                        String IDAppointment =appointmentSnapshot.getKey();
                        // Tạo view mới từ layout XML
                        if(getActivity()!=null){
                            LayoutInflater inflater = LayoutInflater.from(getActivity());
                        }

                        View itemView = inflater.inflate(R.layout.item_appointment_history, null);


                        // Đặt dữ liệu vào các TextView trong itemView
                        TextView serviceTextView = itemView.findViewById(R.id.servicename);
                        serviceTextView.setText(Service);

                        TextView dateTextView = itemView.findViewById(R.id.date);
                        dateTextView.setText(Date);

                        linearLayout.addView(itemView);
                        itemView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                SharedPreferences sharedPreferences = getContext().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                editor.putString("DateApoimentSelect", Date);
                                editor.putString("NameApoimentSelect", Service);
                                SaveKeyAppointment(IDAppointment);
                                SaveSessionAppointment(IDAppointment);
                                SendToDetailAppoiment();

                            }
                        });
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
        //them item vô scrollview incoming
        ScrollView scrollView1 = rootView.findViewById(R.id.scrollItemIncoming);
        LinearLayout linearLayout1 = new LinearLayout(getActivity());
        linearLayout1.setOrientation(LinearLayout.VERTICAL);

        scrollView1.addView(linearLayout1);
        // Tạo truy vấn để lấy tất cả các cuộc hẹn có userID và status mong muốn
        Query appointmentsQueryend = appointmentsRef.orderByChild("status").equalTo("incoming");;
        appointmentsQueryend.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot appointmentSnapshot : dataSnapshot.getChildren()) {
                    if (appointmentSnapshot.child("user").child("userID").getValue(String.class).equals(userId)) {
                        // Lấy dữ liệu từ dataSnapshot
                        String Service = appointmentSnapshot.child("service").getValue(String.class);
                        String Date = appointmentSnapshot.child("date").getValue(String.class);
                        String IDAppointment =appointmentSnapshot.getKey();
                        // Tạo view mới từ layout XML
                        if(getActivity()!=null){
                            LayoutInflater inflater = LayoutInflater.from(getActivity());
                        }

                        View itemView = inflater.inflate(R.layout.item_appointment_incoming, null);


                        // Đặt dữ liệu vào các TextView trong itemView
                        TextView serviceTextView = itemView.findViewById(R.id.Serive);
                        serviceTextView.setText(Service);

                        TextView dateTextView = itemView.findViewById(R.id.dateservice);
                        dateTextView.setText(Date);

                        TextView IDAppoiment =itemView.findViewById(R.id.IDAppointment);
                        IDAppoiment.setText(IDAppointment);
                        linearLayout1.addView(itemView);
                        itemView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                TextView IDAppoiment =itemView.findViewById(R.id.IDAppointment);
                                String ID= IDAppoiment.getText().toString();
                                SharedPreferences sharedPreferences = getContext().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                editor.putString("DateApoimentSelect", Date);
                                editor.putString("NameApoimentSelect", Service);
                                editor.putString("AppointmentID", ID);
                                SaveKeyAppointment(IDAppointment);
                                SaveSessionAppointment(ID);
                                SendToDetailAppoiment();

                            }
                        });
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
        return rootView;
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
                    AppointmentSessionManager Session = new AppointmentSessionManager(getActivity());
                    Session.createAppointmentSession(new Appointment(date,service));

                } else {
                    Toast.makeText(getActivity(), "Appoiment not found", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Xử lý khi có lỗi xảy ra
                Toast.makeText(getActivity(), "Error: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void SendToDetailAppoiment(){
        Intent intent = new Intent(getActivity(), DetailApointmentActivity.class);
        startActivity(intent);
        getActivity().finish();
    }
    public void SaveKeyAppointment(String AppoimentID){
        SharedPreferences sharedPreferences = getContext().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("AppointmentKey", AppoimentID);
        editor.apply();
    }

}