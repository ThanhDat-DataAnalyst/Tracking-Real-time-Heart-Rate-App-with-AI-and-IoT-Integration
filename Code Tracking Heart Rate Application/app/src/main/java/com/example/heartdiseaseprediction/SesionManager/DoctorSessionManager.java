package com.example.heartdiseaseprediction.SesionManager;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.heartdiseaseprediction.Models.Doctor;

public class DoctorSessionManager {
    private static final String PREF_NAME = "DoctorSession";
    private static final String KEY_USERNAME = "doctor_username";
    private static final String KEY_PASSWORD = "doctor_password";

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private Context context;

    public DoctorSessionManager(Context context) {
        this.context = context;
        sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    // Lưu thông tin Doctor vào SharedPreferences
    public void createDoctorLoginSession(String username, String password) {
        editor.putString(KEY_USERNAME, username);
        editor.putString(KEY_PASSWORD, password);
        editor.apply();
    }

    // Lấy thông tin Doctor từ SharedPreferences
    public Doctor getDoctorDetails() {
        String username = sharedPreferences.getString(KEY_USERNAME, "");
        String password = sharedPreferences.getString(KEY_PASSWORD, "");
        return new Doctor(username, password);
    }

    // Xóa thông tin Doctor khi đăng xuất
    public void logoutDoctor() {
        editor.clear();
        editor.apply();
    }
}
