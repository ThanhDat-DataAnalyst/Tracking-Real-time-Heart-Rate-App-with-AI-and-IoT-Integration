# Tracking Real-time Heart Rate App with AI and IoT Integration

A comprehensive application that leverages Artificial Intelligence and IoT technologies to track, analyze, and categorize heart rates in real time, aiming to support early detection of potential heart disease.

## Overview

This project combines a MAX30102 heart rate sensor, an ESP8266 microcontroller, and a mobile app integrated with Firebase and a machine learning model. It uses real-time heart rate data to classify health conditions based on trained models, providing immediate feedback to users and healthcare providers.

## Features

- **Real-time Heart Rate Monitoring**: Tracks heart rate via MAX30102 sensor.
- **Data Transmission via ESP8266**: Sends heart rate data to Firebase Realtime Database.
- **Heart Health Classification**: Uses a deep learning model deployed within the mobile app for analyzing heart rate patterns.
- **User Roles**: Differentiates access and views for patients and doctors.
- **Alerts and Notifications**: Sends notifications based on health status, improving monitoring efficiency.

## Technology Stack

- **Hardware**: MAX30102 Heart Rate Sensor, ESP8266 Microcontroller
- **Software**: Android Application (Java), Google Firebase, TensorFlow Lite
- **Machine Learning Model**: Multilayer Perceptron (MLP) for binary classification of heart health status
- **Database**: Firebase Realtime Database

## System Architecture

The system consists of several components working together:

1. **ESP8266 & MAX30102 Sensor**: The ESP8266 retrieves heart rate data from the MAX30102 sensor and sends it to Firebase Realtime Database.
2. **Firebase Realtime Database**: Collects and stores heart rate data securely in real-time.
3. **Mobile Application**: The app retrieves data from Firebase and processes it through a TensorFlow Lite model for health risk classification.
4. **Deep Learning Model**: The model identifies potential abnormalities by classifying data into risk categories, supporting early detection.

## Project Setup

### Prerequisites

- **Hardware**: ESP8266, MAX30102 Sensor
- **Software**: Android Studio, Firebase account, and TensorFlow Lite setup

### Installation

1. **Hardware Setup**
   - Connect MAX30102 to ESP8266 with the following wiring:
     - **VCC** to **3.3V**
     - **SCL** to **D1**
     - **SDA** to **D2**
     - **GND** to **GND**

2. **Firebase Realtime Database**
   - Create a Firebase project and set up Realtime Database.
   - Allow read/write permissions in Firebase during development for easy access.
   - Configure ESP8266 with Firebase credentials.

3. **ESP8266 Code Configuration**
   - Load the ESP8266 firmware with necessary libraries for sensor data collection and Firebase integration.
   - Deploy code to calculate heart rate from MAX30102 signals and send the processed data to Firebase.

4. **Android Application Setup**
   - Open the project in Android Studio and set up Firebase and TensorFlow Lite dependencies.
   - Configure Firebase Authentication for secure access.
   - Build and run the app on an Android device.

### Model Integration

1. **Data Collection & Preprocessing**: Data is preprocessed using the heart disease dataset, and the features are normalized.
2. **Model Training**: A Multilayer Perceptron (MLP) model is trained and optimized for low-resource mobile deployment.
3. **Deployment**: Convert the trained model to TensorFlow Lite and integrate it within the app for real-time inference.

## Usage

1. **User Registration and Login**: Sign up or log in via Firebase Authentication.
2. **Real-time Monitoring**: Connect the device to view live heart rate data on the app.
3. **Health Analysis**: The app uses the AI model to classify heart rate data and assess risk.
4. **Notifications**: Receive immediate feedback and alerts on heart health.

## Demo Video

For a visual walkthrough, please refer to our [YouTube Demo Playlist](https://youtube.com/playlist?list=PLukNUykQ9UezldMhODX74kLT8UpjAlQuM&si=emFILo99nSx4E1n8) showcasing the application setup, functionalities, and real-time usage examples.

## Future Enhancements

- **Advanced Models**: Explore additional deep learning models for improved classification accuracy.
- **Additional Health Metrics**: Extend monitoring to include blood oxygen levels and other cardiovascular indicators.
- **Remote Health Monitoring**: Implement features for remote monitoring by healthcare professionals.
