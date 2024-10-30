package com.example.heartdiseaseprediction.Models;

public class Medicine {
    private String name;
    private String routine;
    private String amount;

    // Constructor
    public Medicine(String name, String routine, String amount) {
        this.name = name;
        this.routine = routine;
        this.amount = amount;
    }

    // Getter và Setter cho name
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    // Getter và Setter cho routine
    public String getRoutine() {
        return routine;
    }

    public void setRoutine(String routine) {
        this.routine = routine;
    }

    // Getter và Setter cho amount
    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }
}
