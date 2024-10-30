#include <Arduino.h>

#include <ESP8266WiFi.h>

#include <FirebaseESP8266.h>

#include <Wire.h>

#include "MAX30105.h"

#include "heartRate.h"

//Provide the token generation process info. chua doi
#include "addons/TokenHelper.h"
//Provide the RTDB payload printing info and other helper functions.
#include "addons/RTDBHelper.h"

// Wifi credential chua doi
#define WIFI_SSID "QuangSang123"
#define WIFI_PASSWORD "23112005CBL"

// Firebase API key
#define API_KEY "AIzaSyB31Vwzihcr0ub-SFr6wt2ypZ7x1PqdXGw"

// Firebase Realtime Database URL
#define DATABASE_URL "https://heart-rate-46b5c-default-rtdb.asia-southeast1.firebasedatabase.app/"

// Firebase Authentication chua doi
#define USER_EMAIL "tdp.uit@gmail.com"
#define USER_PASSWORD "Ptd074202001518"

// Define Firebase Data object
FirebaseData fbdo;
FirebaseAuth auth;
FirebaseConfig config;

unsigned long sendDataPrevMillis = 0;

const byte RATE_SIZE = 4; //Increase this for more averaging. 4 is good.
byte rates[RATE_SIZE]; //Array of heart rates
byte rateSpot = 0;
long lastBeat = 0; //Time at which the last beat occurred

// Define Sensor
MAX30105 particleSensor;

float beatsPerMinute;
int beatAvg;
String uid;
String databasePath;

void setup() {
    Serial.begin(115200);
    // Initialize sensor
    if (!particleSensor.begin(Wire, I2C_SPEED_FAST)) //Use default I2C port, 400kHz speed
    {
        Serial.println("MAX30105 was not found. Please check wiring/power. ");
        while (1);
    }
    Serial.println("Place your index finger on the sensor with steady pressure.");

    particleSensor.setup(); //Configure sensor with default settings
    particleSensor.setPulseAmplitudeRed(0x0A); //Turn Red LED to low to indicate sensor is running
    particleSensor.setPulseAmplitudeGreen(0); //Turn off Green LED

    /*
          CONNECT TO WIFI
    */
    WiFi.begin(WIFI_SSID, WIFI_PASSWORD);
    Serial.print("Connecting to Wi-Fi");
    while (WiFi.status() != WL_CONNECTED) {
        Serial.print(".");
        delay(300);
    }
    Serial.println();
    Serial.print("Connected with IP: ");
    Serial.println(WiFi.localIP());


    Serial.println();
    Serial.printf("Firebase Client v%s\n\n", FIREBASE_CLIENT_VERSION);

    /* Assign the api key (required) */
    config.api_key = API_KEY;

    /* Assign the user sign in credentials */
    auth.user.email = USER_EMAIL;
    auth.user.password = USER_PASSWORD;

    /* Assign the RTDB URL (required) */
    config.database_url = DATABASE_URL;

    /* Assign the callback function for the long running token generation task */
    config.token_status_callback = tokenStatusCallback; // see addons/TokenHelper.h

    Firebase.reconnectNetwork(true);

    Firebase.begin( & config, & auth);

    Firebase.setDoubleDigits(5);

}

void loop() {
    long irValue = particleSensor.getIR();
    beatsPerMinute = 0;
    if (checkForBeat(irValue) == true) {
        //We sensed a beat!
        long delta = millis() - lastBeat;
        lastBeat = millis();

        beatsPerMinute = 60 / (delta / 1000.0);

        if (beatsPerMinute < 255 && beatsPerMinute > 20) {
            rates[rateSpot++] = (byte) beatsPerMinute; //Store this reading in the array
            rateSpot %= RATE_SIZE; //Wrap variable

            //Take average of readings
            beatAvg = 0;
            for (byte x = 0; x < RATE_SIZE; x++)
                beatAvg += rates[x];
        }

        /*
              SEND BEAT TO FIREBASE
        */
        if (Firebase.ready() && (millis() - sendDataPrevMillis > 100 || sendDataPrevMillis == 0)) {
            sendDataPrevMillis = millis();

            Serial.println(beatsPerMinute);

            uid = auth.token.uid.c_str();
            databasePath = "/data/" + uid + "/beat";
            delay(0);
            if (Firebase.setFloat(fbdo, databasePath, beatsPerMinute)) {
                // This command will be executed even if you dont serial print but we will make sure its working
                Serial.print("beat : ");
                Serial.println(beatsPerMinute);
            } else {
                Serial.println("Failed to Read from the Sensor");
                Serial.println("REASON: " + fbdo.errorReason());
            }
        }
    }
    //
}