package com.example.heartdiseaseprediction.Activities.User;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.heartdiseaseprediction.Activities.LoginActivity;
import com.example.heartdiseaseprediction.Models.User;
import com.example.heartdiseaseprediction.R;
import com.example.heartdiseaseprediction.SesionManager.UserSessionManager;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class ProfileFragment extends Fragment {

    TextView username, age, height,weight, gender;

    View rootView;
    ImageView logOut;
    UserSessionManager session;
    User user;
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    FirebaseUser currentUser = mAuth.getCurrentUser();
    String userId = currentUser.getUid();
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Khai báo TextView
        rootView = inflater.inflate(R.layout.fragment_profile, container, false);

        session=new UserSessionManager(getActivity());
        user=session.getUserDetails();

        username=rootView.findViewById(R.id.textView_Username);
        age=rootView.findViewById(R.id.age_value);
        height=rootView.findViewById(R.id.height_value);
        weight=rootView.findViewById(R.id.weight_value);
        gender=rootView.findViewById(R.id.sex_value);

        username.setText(user.getUsername());
        age.setText(user.getAge());
        height.setText(user.getHeight());
        weight.setText(user.getWeight());
        gender.setText(user.getGender());

        TextView textViewUsername = rootView.findViewById(R.id.textView_Username);
        FirebaseAuth auth = FirebaseAuth.getInstance();
        DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference().child("users").child(auth.getCurrentUser().getUid());

        usersRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    // Lấy dữ liệu của trường "username" từ dataSnapshot
                    String username = dataSnapshot.child("username").getValue(String.class);

                    // Hiển thị username lên TextView
                    textViewUsername.setText(username);
                } else {
                    // Không tìm thấy dữ liệu hoặc user không tồn tại
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Xử lý khi có lỗi truy vấn cơ sở dữ liệu
            }
        });
        //Show profile action
        logOut = rootView.findViewById(R.id.LogOutBtn);
        logOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Intent intent=new Intent(getActivity(), LoginActivity.class);
                startActivity(intent);
                getActivity().finish();
                Toast.makeText(getActivity(),"LogOut Successfully",Toast.LENGTH_SHORT).show();
            }
        });
        ImageView UpdateInfo = rootView.findViewById(R.id.updateInfo);
        UpdateInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showOptionsDialog();
            }
        });


        return rootView;
    }
    private void showOptionsDialog() {
        Dialog dialog = new Dialog(getActivity());
        dialog.setContentView(R.layout.dialog_options);

        Button updateInfoButton = dialog.findViewById(R.id.btn_update_information);

        updateInfoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
                DatabaseReference userRef = databaseReference.child("users").child(userId);
           // Handle update information action
                TextInputEditText age=dialog.findViewById(R.id.age_value);
                TextInputEditText weight=dialog.findViewById(R.id.weight_value);
                TextInputEditText height=dialog.findViewById(R.id.height_value);

                String Age=age.getText().toString();
                String Weight=weight.getText().toString();
                String Height=height.getText().toString();

                userRef.child("age").setValue(Age);
                userRef.child("weight").setValue(Weight);
                userRef.child("height").setValue(Height);

                session.createUserLoginSession( userId,user.getEmail(),  user.getPassword(),  user.getUsername(),  Age,  Height,  Weight ,user.getGender());

                dialog.dismiss();
                Reload();
            }
        });



        dialog.show();
    }
    public void Reload(){
        Intent intent = new Intent(getActivity(), getActivity().getClass());
        getActivity().finish();
        getActivity().overridePendingTransition(0, 0);
        startActivity(intent);
        getActivity().overridePendingTransition(0, 0);
    }

}