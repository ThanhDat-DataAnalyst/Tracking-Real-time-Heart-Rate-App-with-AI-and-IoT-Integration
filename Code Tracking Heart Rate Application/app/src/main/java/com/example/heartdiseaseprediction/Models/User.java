package com.example.heartdiseaseprediction.Models;

public class User {
    private String userID;
    private String email;
    private String password; // Lưu ý: Không nên lưu trữ mật khẩu trong database
    private String username;
    private String age;
    private String height;
    private  String weight;
    private String gender;


    // Constructor rỗng được yêu cầu bởi Firebase
    public User() {
    }

    // Constructor với các tham số
    public User(String userID,String email, String password, String username, String age, String height, String weight,String gender) {
        this.email = email;
        this.password = password;
        this.username = username;
        this.age=age;
        this.height=height;
        this.weight=weight;
        this.gender=gender;
        this.userID=userID;
    }
    public User(String email, String password, String username) {
        this.email=email;
        this.password=password;
        this.username = username;
    }
    public User(String age, String height, String weight,String gender) {
        this.age=age;
        this.height=height;
        this.weight = weight;
        this.gender=gender;
    }


    // Getter và Setter cho email
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    // Getter và Setter cho password
    // Lưu ý: Cân nhắc việc lưu trữ và truyền mật khẩu
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    // Getter và Setter cho username
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getHeight() {
        return height;
    }

    public void setHeight(String height) {
        this.height = height;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }
    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }
    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }
}

