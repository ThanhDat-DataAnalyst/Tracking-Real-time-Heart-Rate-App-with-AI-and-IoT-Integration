package com.example.heartdiseaseprediction.Activities.User;

import android.content.Intent;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.heartdiseaseprediction.R;
import com.example.heartdiseaseprediction.ml.Model;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mikhaellopez.circularprogressbar.CircularProgressBar;

import org.tensorflow.lite.DataType;
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;


public class HomeFragment extends Fragment {
    FirebaseDatabase firebaseDatabase;
    // creasting a variable for our
    // Database Reference for Firebase.
    DatabaseReference databaseReference;
    MediaPlayer mediaPlayer;
    // variable for Text view.
    TextView tv_HeartRate;
    TextView tv_timeStamp;
    CircularProgressBar prog_Bar;
    Button btn_ShowProfile,btn_ShowChart,btn_ShowHistory;

    long k = 1000;
    float[] mean = {-6.8565e-08F, -5.0756e-08F,-5.4763e-08F,
            1.6562e-07F,-1.5583e-08F, -1.2466e-08F, -6.6784e-08F,
            -5.3205e-08F, -2.7604e-08F, -4.2742e-08F, 9.7059e-08F};
    float[] variance = {1.0000F, 1.0000F, 1.0000F, 1.0000F, 1.0000F, 1.0000F, 1.0000F, 1.0000F, 1.0000F,
            1.0000F, 1.0000F};
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    FirebaseUser currentUser = mAuth.getCurrentUser();
    String userId = currentUser.getUid();

    View rootView;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_home, container, false);
        tv_HeartRate = rootView.findViewById(R.id.textView_HeartRate);
        tv_timeStamp = rootView.findViewById(R.id.lastResultDate);
        prog_Bar = rootView.findViewById(R.id.circularProgressBar);
        //tv_HeartRate = findViewById(R.id.textView_HeartRate);
        //btn_ShowProfile = findViewById(R.id.btn_ShowProfile);
        //result = findViewById(R.id.tv_result);
        // below line is used to get the instance
        // of our Firebase database.
        firebaseDatabase = FirebaseDatabase.getInstance();

        // below line is used to get
        // reference for our database.
        databaseReference = firebaseDatabase.getReference("data");



        // initializing our object class variable.
        // calling method
        // for getting data.
        getdata();

       return rootView;
    }
    private void getdata() {

        // calling add value event listener method
        // for getting the values from database.
        databaseReference.child("93eXberrMRh04kseFhXvBNegRvI3").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                // this method is call to get the realtime
                // updates in the data.
                // this method is called when the data is
                // changed in our Firebase console.
                // below line is for getting the data from
                // snapshot of our database.
                String beatValue = String.valueOf(snapshot.child("beat").getValue());

                String currentDate = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());
                String currentTime = new SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(new Date());
                String timestamp = new SimpleDateFormat("yyyyMMddHHmmss", Locale.getDefault()).format(new Date());
                databaseReference = firebaseDatabase.getReference("users");
                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
                String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
                DatabaseReference userCardiacInfoRef = FirebaseDatabase.getInstance().getReference("users")
                        .child(userId)
                        .child("cardiacInfo");



                databaseReference.child("users").child(userId).child("cardiacInfo").child("lastUpdateTimestamp").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DataSnapshot> task) {
                        if (task.isSuccessful()) {
                            // Tạo một đối tượng Date đại diện cho thời điểm hiện tại
                            Date now = new Date();

                            // Định dạng thời gian hiện tại theo kiểu yyyyMMddHH
                            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHH");

                            // Chuyển đổi thời gian hiện tại sang chuỗi theo định dạng đã chỉ định
                            final String timestampFinal = sdf.format(now);

                            databaseReference.child("users").child(userId).child("cardiacInfo").child(timestampFinal).setValue(beatValue)
                                    .addOnSuccessListener(aVoid -> {
                                        Log.d("Firebase", "Heart rate successfully written!");
                                        // Cập nhật lastUpdateTimestamp với biến final

                                    })
                                    .addOnFailureListener(e -> Log.w("Firebase", "Error writing heart rate", e));
                        } else {
                            Log.w("Firebase", "Failed to get last update timestamp", task.getException());
                        }

                    }
                });
                // our value to our text view in below line.
                tv_HeartRate.setText(beatValue);
                tv_timeStamp.setText("Last result: " + currentDate + " at " + currentTime);
                float beat = Float.parseFloat(beatValue);
                float beatid = 100 * (beat / 250);
                prog_Bar.setProgressWithAnimation((int)beatid, k); // =1s
                //calPrediction();
                try {
                    Predict(40, 1, 2, 140, 289, 0, 0, (int)beat, 0, 0, 1);
                }
                catch(IOException e)
                {
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // calling on cancelled method when we receive
                // any error or we are not able to get the data.
                Toast.makeText(getActivity(), "Fail to get data.", Toast.LENGTH_SHORT).show();
            }
        });


    }
    private void Predict(int Age, int Sex, int Cp, int Restbps, int Chol,
                         int Fbs, int Rest_ecg, int Heart_rate, int Agina, float Oldpeak, int Slope) throws IOException {
        Model model = Model.newInstance(getActivity());
        float[] floatArr = {(float)Age, (float)Sex, (float)Cp, (float)Restbps, (float)Chol, (float)Fbs
                , (float)Rest_ecg, (float)Heart_rate, (float)Agina, (float)Oldpeak, (float)Slope};
        // Creates inputs for reference.
        TensorBuffer inputFeature0 = TensorBuffer.createFixedSize(new int[]{1, 11}, DataType.FLOAT32);

        //Scale input
        scaleTensorBuffer(inputFeature0, mean, variance);

        inputFeature0.loadArray(floatArr);

        // Runs model inference and gets result.
        Model.Outputs outputs = model.process(inputFeature0);
        TensorBuffer outputFeature0 = outputs.getOutputFeature0AsTensorBuffer();
        TextView tv= rootView.findViewById(R.id.textView_PredictResult);
        float[] data1=outputFeature0.getFloatArray();
        if (data1[0] >= data1[1])
        {
            tv.setTextColor(Color.BLACK);
            tv.setText("Your Heart health is NORMAL now!");
        }
        else {
            tv.setTextColor(Color.RED);
            tv.setText("Your Heart health is Dangerous!!!");
            mediaPlayer = MediaPlayer.create(getActivity(), R.raw.beep_warning);
            mediaPlayer.start();
        }
        // Releases model resources if no longer used.
        model.close();
    }
    public void scaleTensorBuffer(TensorBuffer tensorBuffer, float[] mean, float[] variance) {
        float[] floatValues = tensorBuffer.getFloatArray();
        for (int i = 0; i < floatValues.length; i++) {
            float stdDev = (float) Math.sqrt(variance[i]);
            floatValues[i] = (floatValues[i] - mean[i]) / stdDev;
        }
        tensorBuffer.loadArray(floatValues);
    }
}