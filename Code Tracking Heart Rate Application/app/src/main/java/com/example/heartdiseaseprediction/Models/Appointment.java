package com.example.heartdiseaseprediction.Models;

import com.example.heartdiseaseprediction.Models.User;

public class Appointment {
    private User user;
    private String date;
    private String doctor;
    private String note;
    private String status;
    private String service;
    private String userid_status;
    private String temperature;
    private String blood_pressure;
    private String medical_history;
    private String diagnostic;
    private String sypptom;
    public Appointment() {
    }

    public Appointment(String date, String doctor, String note, String status, String service,String userid_status,String temperature, String blood_pressure,String medical_history, String diagnostic)
    {
        this.date = date;
        this.doctor = doctor;
        this.note = note;
        this.status=status;
        this.service = service;
        this.userid_status=userid_status;
        this.temperature=temperature;
        this.blood_pressure=blood_pressure;
        this.medical_history=medical_history;
        this.diagnostic=diagnostic;

    }
    public Appointment(String date, String doctor, String note, String status, String service, User user,String userid_status) {
        this.date = date;
        this.doctor = doctor;
        this.note = note;
        this.status=status;
        this.service = service;
        this.user=user;
        this.userid_status=userid_status;
    }
    public Appointment( String temperature, String blood_pressure,String sypptom,String medical_history, String diagnostic ){
        this.temperature=temperature;
        this.blood_pressure=blood_pressure;
        this.medical_history=medical_history;
        this.diagnostic=diagnostic;
        this.sypptom=sypptom;
    }
    public Appointment(String date,String service){
        this.date=date;
        this.service=service;
    }
    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDoctor() {
        return doctor;
    }

    public void setDoctor(String doctor) {
        this.doctor = doctor;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getService() {
        return service;
    }

    public void setService(String service) {
        this.service = service;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
    public String getUserid_status() {
        return userid_status;
    }

    public void setUserid_status(String userid_status) {
        this.userid_status = userid_status;
    }

    public String getTemperature() {
        return temperature;
    }

    public void setTemperature(String temperature) {
        this.temperature= temperature;
    }
    public String getBlood_pressure () {
        return blood_pressure ;
    }

    public void setBlood_pressure (String blood_pressure ) {
        this.blood_pressure  = blood_pressure ;
    }
    public String getMedical_history () {
        return medical_history ;
    }

    public void setMedical_history (String medical_history ) {
        this.medical_history  = medical_history ;
    }
    public String getDiagnostic () {
        return diagnostic ;
    }

    public void setDiagnostic (String diagnostic ) {
        this.diagnostic  = diagnostic ;
    }

    public String getSypptom () {
        return sypptom ;
    }

    public void setSypptom (String sypptom ) {
        this.sypptom  = sypptom ;
    }


}
