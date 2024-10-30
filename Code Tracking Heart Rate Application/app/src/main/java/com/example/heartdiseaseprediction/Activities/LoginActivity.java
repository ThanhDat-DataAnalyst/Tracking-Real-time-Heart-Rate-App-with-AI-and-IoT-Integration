package com.example.heartdiseaseprediction.Activities;

import androidx.appcompat.app.AppCompatActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.heartdiseaseprediction.Activities.Doctor.AdminMainScreen;
import com.example.heartdiseaseprediction.Activities.User.MainScreen;
import com.example.heartdiseaseprediction.Models.User;
import com.example.heartdiseaseprediction.R;
import com.example.heartdiseaseprediction.SesionManager.DoctorSessionManager;
import com.example.heartdiseaseprediction.SesionManager.UserSessionManager;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
public class LoginActivity extends AppCompatActivity {
    /*
        This activity handles the login process to help user enter the application.
    */
    TextInputEditText userName;
    TextInputEditText password;
    Button login, forgotPassword;
    TextView register;

    FirebaseUser currentUser;//used to store current user of account
    FirebaseAuth mAuth;//Used for firebase authentication
    ProgressDialog loadingBar;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference usersRef = database.getReference("users");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initializeUI();
    }
    /*
        To intialize all UI and other components.
     */
    protected void initializeUI(){
        userName =  findViewById(R.id.txtUserNameLogin);
        password = findViewById(R.id.passPassLogin);
        login =  findViewById(R.id.ButtonLogin);
        register = findViewById(R.id.ButtonSignup);
        //forgotPassword = (Button) findViewById(R.id.forgotpass);
        mAuth = FirebaseAuth.getInstance();
        loadingBar = new ProgressDialog(this);
        currentUser = mAuth.getCurrentUser();
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AllowUserToLogin();
            }
        });
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendUserToRegister();
            }
        });
        //if user forgets the password then to reset it
//        forgotPassword.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                resetPasswordUser();
//            }
//        });
//        if (getSupportActionBar() != null) {
//            getSupportActionBar().hide();
//        }
    }
    /*
        Handles the password reset operation.
     */
//    private void resetPasswordUser() {
//        String email = userName.getText().toString().trim();
//        if(TextUtils.isEmpty(email))
//        {
//            Toast.makeText(LoginActivity.this,"Please enter your email id",Toast.LENGTH_SHORT).show();
//        }
//        else
//        {
//            FirebaseAuth.getInstance().sendPasswordResetEmail(email)
//                    .addOnCompleteListener(new OnCompleteListener<Void>() {
//                        @Override
//                        public void onComplete(@NonNull Task<Void> task) {
//                            if (task.isSuccessful()) {
//                                Toast.makeText(LoginActivity.this, "Reset Email sent", Toast.LENGTH_SHORT).show();
//                            }
//                        }
//                    });
//        }
//    }
    /*
        To send user to the registration page.
     */
    private void sendUserToRegister() {
        //When user wants to create a new account send user to Register Activity
        Intent registerIntent = new Intent(LoginActivity.this,RegisterActivity.class);
        startActivity(registerIntent);
    }

    /*
        Handle the login process.
     */
    private void AllowUserToLogin() {
        String email = userName.getText().toString().trim();
        String pwd = password.getText().toString().trim(); // Thêm trim() để loại bỏ khoảng trắng

        // Kiểm tra cả email và mật khẩu cùng một lúc
        if (TextUtils.isEmpty(email) || TextUtils.isEmpty(pwd)) {
            if (TextUtils.isEmpty(email)) {
                Toast.makeText(LoginActivity.this, "Please enter email id", Toast.LENGTH_SHORT).show();
            }
            if (TextUtils.isEmpty(pwd)) {
                Toast.makeText(LoginActivity.this, "Please enter password", Toast.LENGTH_SHORT).show();
            }
            // Dừng hàm nếu thiếu thông tin
            return;
        }

        loadingBar.setTitle("Sign In");
        loadingBar.setMessage("Please wait, because good things always take time");
        loadingBar.setCanceledOnTouchOutside(false); // Ngăn không cho người dùng tắt loadingBar khi chạm ngoài
        loadingBar.show();


//        mAuth.signInWithEmailAndPassword(email, pwd)
//                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
//                    @Override
//                    public void onComplete(@NonNull Task<AuthResult> task) {
//                        if (task.isSuccessful()) {
//                            sendToMainActivity();
//                            Toast.makeText(LoginActivity.this, "Welcome", Toast.LENGTH_SHORT).show();
//                        } else {
//                            String msg = task.getException().getMessage(); // Sử dụng getMessage() để có thông điệp lỗi ngắn gọn hơn
//                            Log.e("Error Login", "Login Error: " + msg);
//                            Toast.makeText(LoginActivity.this, "Error: " + msg, Toast.LENGTH_SHORT).show();
//                        }
//                        loadingBar.dismiss();
//                    }
//                });
        usersRef.orderByChild("username").equalTo(userName.getText().toString().trim()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot userSnapshot: dataSnapshot.getChildren()) {
                        // Giả sử username là duy nhất
                        User user = userSnapshot.getValue(User.class);
                        String emailForLogin = user.getEmail();

                        // Tiến hành đăng nhập bằng email và password
                        loginWithEmail(emailForLogin, password.getText().toString().trim());
                        break;
                    }
                } else {
                    // Xử lý trường hợp không tìm thấy username
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
    private void loginWithEmail(String email, String password) {
        FirebaseAuth mAuth = FirebaseAuth.getInstance();

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            FirebaseAuth auth = FirebaseAuth.getInstance();
                            FirebaseUser user = auth.getCurrentUser();

                            if (user != null) {
                                // Đã có người dùng đăng nhập
                                DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference().child("users").child(user.getUid());
                                usersRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        if (dataSnapshot.exists()) {
                                            // Lấy dữ liệu của người dùng từ Firebase
                                            String username = dataSnapshot.child("username").getValue(String.class);
                                            String email=dataSnapshot.child("email").getValue(String.class);
                                            String password=dataSnapshot.child("password").getValue(String.class);
                                            SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
                                            SharedPreferences.Editor editor = sharedPreferences.edit();
                                            editor.putString("userRole", username);
                                            editor.apply();
                                            // Kiểm tra xem username có phải là "admin" hay không
                                            if (username.startsWith("Doctor")) {
                                                // Chuyển hướng đến trang admin
                                                DoctorSessionManager session = new DoctorSessionManager(getApplicationContext());
                                                session.createDoctorLoginSession(username,password);
                                                sendToMainActivityAdmin();
                                            } else {
                                                FirebaseUser currentUser = mAuth.getCurrentUser();
                                                String userID = currentUser.getUid();
                                                String gender=dataSnapshot.child("gender").getValue(String.class);
                                                String age=dataSnapshot.child("age").getValue(String.class);
                                                String weight=dataSnapshot.child("weight").getValue(String.class);
                                                String height=dataSnapshot.child("height").getValue(String.class);
                                                UserSessionManager session = new UserSessionManager(getApplicationContext());
                                                session.createUserLoginSession( userID, email,  password,  username,  age,  height,  weight ,gender);
                                                // Chuyển hướng đến trang người dùng thông thường
                                                sendToMainActivity();
                                            }
                                        } else {
                                            // Không tìm thấy thông tin người dùng trong cơ sở dữ liệu
                                            // Xử lý theo nhu cầu của bạn
                                        }
                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {
                                        // Xử lý khi có lỗi truy vấn cơ sở dữ liệu
                                    }
                                });
                            } else {
                                // Người dùng chưa đăng nhập
                                // Xử lý theo nhu cầu của bạn
                            }

                           Toast.makeText(LoginActivity.this, "Welcome", Toast.LENGTH_SHORT).show();
                        } else {
                            String msg = task.getException().getMessage(); // Sử dụng getMessage() để có thông điệp lỗi ngắn gọn hơn
                            Log.e("Error Login", "Login Error: " + msg);
                            Toast.makeText(LoginActivity.this, "Error: " + msg, Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    protected void onStart() {
        //Check if user has already signed in if yes send to mainActivity
        //This to avoid signing in everytime you open the app.
        super.onStart();
        if(currentUser!=null)
        {
            sendToMainActivity();
        }
    }
    /*
        After successfull validation of username and password send user to the main activity.
     */
    private void sendToMainActivity() {
        //This is to send user to MainActivity
        Intent  MainIntent = new Intent(LoginActivity.this, MainScreen.class);
        startActivity(MainIntent);
    }
    private void sendToMainActivityAdmin() {
        //This is to send user to MainActivity
        Intent  MainIntent = new Intent(LoginActivity.this, AdminMainScreen.class);
        startActivity(MainIntent);
    }
}
