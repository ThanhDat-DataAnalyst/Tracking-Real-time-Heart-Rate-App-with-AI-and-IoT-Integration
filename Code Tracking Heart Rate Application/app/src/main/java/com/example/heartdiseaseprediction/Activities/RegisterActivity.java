package com.example.heartdiseaseprediction.Activities;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.heartdiseaseprediction.Models.User;
import com.example.heartdiseaseprediction.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterActivity extends AppCompatActivity {

    protected FirebaseAuth mFirebaseAuth = FirebaseAuth.getInstance();
    /*
          This activity handles the process of registration of new users.
       */
    EditText userName, password, password1,Email;
    TextView AccountExists;
    Button register;
    private FirebaseAuth mAuth;//Used for firebase authentication
    private ProgressDialog loadingBar;//Used to show the progress of the registration process
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference usersRef = database.getReference("users");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        initializeUI();
    }

    /*
        To intialize the UI and other components.
     */
    private void initializeUI() {
        mAuth = FirebaseAuth.getInstance();
        userName = (EditText) findViewById(R.id.usernameEdittext);
        Email=(EditText) findViewById(R.id.emailEditText);
        password = (EditText) findViewById(R.id.passEditText);
        password1 = (EditText) findViewById(R.id.repassEditText);
        register = (Button) findViewById(R.id.ButtonSignup);
        AccountExists = (TextView) findViewById(R.id.ButtonLogin);
        loadingBar = new ProgressDialog(this);
        //When user has  an account already he should be sent to login activity.
        AccountExists.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendUserToLoginActivity();
            }
        });
        //When user clicks on register create a new account for user
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createNewAccount();
            }
        });

        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
    }

    /*
        This method creates new account for new users.
     */
    private void createNewAccount() {
        String email = Email.getText().toString().trim();
        String pwd = password.getText().toString().trim();
        String cmp = password1.getText().toString().trim();
        if (TextUtils.isEmpty(email)) {
            Toast.makeText(RegisterActivity.this, "Please enter email id", Toast.LENGTH_SHORT).show();
        }
        if (TextUtils.isEmpty(pwd)) {
            Toast.makeText(RegisterActivity.this, "Please enter password", Toast.LENGTH_SHORT).show();
        }
        if (!pwd.equals(cmp)) {
            Toast.makeText(RegisterActivity.this, "Please Check the Conform Password!", Toast.LENGTH_SHORT).show();
        } else {
            //When both email and password are available create a new accountToast.makeText(RegisterActivity.this,"Please enter password",Toast.LENGTH_SHORT).show();
            //Show the progress on Progress Dialog
            loadingBar.setTitle("Creating New Account");
            loadingBar.setMessage("Please wait, we are creating new Account");
            loadingBar.setCanceledOnTouchOutside(true);
            loadingBar.show();
            mAuth.createUserWithEmailAndPassword(email, pwd)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful())//If account creation successful print message and send user to Login Activity
                            {
                                FirebaseUser user = mAuth.getCurrentUser();

                                User userObj = new User(email, password.getText().toString().trim(),userName.getText().toString().trim());
                                usersRef.child(user.getUid()).setValue(userObj);
                                sendUserToSignupInformationActivity();
                                Toast.makeText(RegisterActivity.this, "Account created successfully", Toast.LENGTH_SHORT).show();
                                loadingBar.dismiss();
                            } else//Print the error message incase of failure
                            {
                                String msg = task.getException().toString();
                                Toast.makeText(RegisterActivity.this, "Error: " + msg, Toast.LENGTH_SHORT).show();
                                Log.e("Error Register", "Registration Error: " + msg);
                                loadingBar.dismiss();
                            }
                        }
                    });
        }

    }

    private void sendUserToLoginActivity() {
        //This is to send user to Login Activity.
        Intent loginIntent = new Intent(RegisterActivity.this, LoginActivity.class);
        startActivity(loginIntent);
    }
    private void sendUserToSignupInformationActivity()
    {
        Intent Intent = new Intent(RegisterActivity.this, SignUpInformationActivity.class);
        startActivity(Intent);
    }
}
