package com.example.heartdiseaseprediction.SesionManager;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.heartdiseaseprediction.Models.Appointment;

public class AppointmentSessionManager {
    private static final String PREF_NAME = "AppointmentSession";
    private static final String KEY_USER_ID = "user_id";
    private static final String KEY_DATE = "date";
    private static final String KEY_DOCTOR = "doctor";
    private static final String KEY_NOTE = "note";
    private static final String KEY_STATUS = "status";
    private static final String KEY_SERVICE = "service";
    private static final String KEY_USERID_STATUS = "userid_status";
    private static final String KEY_TEMPERATURE = "temperature";
    private static final String KEY_BLOOD_PRESSURE = "blood_pressure";
    private static final String KEY_MEDICAL_HISTORY = "medical_history";
    private static final String KEY_DIAGNOSTIC = "diagnostic";

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private Context context;

    public AppointmentSessionManager(Context context) {
        this.context = context;
        sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    // Lưu thông tin cuộc hẹn vào SharedPreferences
    public void createAppointmentSession(Appointment appointment) {
        editor.putString(KEY_DATE, appointment.getDate());
        editor.putString(KEY_DOCTOR, appointment.getDoctor());
        editor.putString(KEY_NOTE, appointment.getNote());
        editor.putString(KEY_STATUS, appointment.getStatus());
        editor.putString(KEY_SERVICE, appointment.getService());
        editor.putString(KEY_USERID_STATUS, appointment.getUserid_status());
        editor.putString(KEY_TEMPERATURE, appointment.getTemperature());
        editor.putString(KEY_BLOOD_PRESSURE, appointment.getBlood_pressure());
        editor.putString(KEY_MEDICAL_HISTORY, appointment.getMedical_history());
        editor.putString(KEY_DIAGNOSTIC, appointment.getDiagnostic());
        editor.apply();
    }

     //Lấy thông tin cuộc hẹn từ SharedPreferences
    public Appointment getAppointmentDetails() {
        String date = sharedPreferences.getString(KEY_DATE, "");
        String doctor = sharedPreferences.getString(KEY_DOCTOR, "");
        String note = sharedPreferences.getString(KEY_NOTE, "");
        String status = sharedPreferences.getString(KEY_STATUS, "");
        String service = sharedPreferences.getString(KEY_SERVICE, "");
        String userIdStatus = sharedPreferences.getString(KEY_USERID_STATUS, "");
        String temperature = sharedPreferences.getString(KEY_TEMPERATURE, "");
        String bloodPressure = sharedPreferences.getString(KEY_BLOOD_PRESSURE, "");
        String medicalHistory = sharedPreferences.getString(KEY_MEDICAL_HISTORY, "");
        String diagnostic = sharedPreferences.getString(KEY_DIAGNOSTIC, "");
        return new Appointment(date, doctor, note, status, service, userIdStatus, temperature, bloodPressure, medicalHistory, diagnostic);
    }

    // Xóa thông tin cuộc hẹn khi không cần thiết
    public void clearAppointmentSession() {
        editor.clear();
        editor.apply();
    }
}
