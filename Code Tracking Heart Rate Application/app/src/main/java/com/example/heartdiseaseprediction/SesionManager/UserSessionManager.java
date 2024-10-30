package com.example.heartdiseaseprediction.SesionManager;


import android.content.Context;
import android.content.SharedPreferences;

import com.example.heartdiseaseprediction.Models.User;

public class UserSessionManager {
    private static final String PREF_NAME = "UserSession";
    private static final String KEY_USERNAME = "username";
    private static final String KEY_EMAIL = "email";
    private static final String KEY_AGE = "age";
    private static final String KEY_GENDER = "gender";
    private static final String KEY_HEIGHT = "height";
    private static final String KEY_WEIGHT = "weight";
    private static final String KEY_PASSWORD = "password";
    private static final String KEY_USERID = "userID";


    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private Context context;

    public UserSessionManager(Context context) {
        this.context = context;
        sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    // Lưu thông tin người dùng vào SharedPreferences
    public void createUserLoginSession(String userID,String email, String password, String username, String age, String height, String weight,String gender) {
        editor.putString(KEY_USERNAME, username);
        editor.putString(KEY_EMAIL, email);
        editor.putString(KEY_AGE, age);
        editor.putString(KEY_GENDER, gender);
        editor.putString(KEY_HEIGHT, height);
        editor.putString(KEY_WEIGHT, weight);
        editor.putString(KEY_PASSWORD,password);
        editor.putString(KEY_USERID,userID);
        editor.apply();
    }

    // Lấy thông tin người dùng từ SharedPreferences
    public User getUserDetails() {
        String username = sharedPreferences.getString(KEY_USERNAME, "");
        String email = sharedPreferences.getString(KEY_EMAIL, "");
        String age = sharedPreferences.getString(KEY_AGE, "0"); // Default value is 0
        String gender = sharedPreferences.getString(KEY_GENDER, "");
        String height = sharedPreferences.getString(KEY_HEIGHT, "0"); // Default value is 0
        String weight = sharedPreferences.getString(KEY_WEIGHT, "0"); // Default value is 0
        String password = sharedPreferences.getString(KEY_PASSWORD, "");
        String userID=sharedPreferences.getString(KEY_USERID, "");
        return new User(userID,email, password, username, age, height, weight, gender);
    }

    // Xóa thông tin người dùng khi đăng xuất
    public void logoutUser() {
        editor.clear();
        editor.apply();
    }
}

