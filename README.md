# FruityScan App

FruityScan is an Android application designed to integrate with an automated tomato sorting conveyor system. This app displays scanning data, weight readings, and sorting status in real-time.

## 📌 Important Prerequisites

This project utilizes **Firebase** as its backend. For security reasons, the Firebase configuration file (`google-services.json`) is **not included** in this Git repository.

Before building or running this project in Android Studio, you **MUST** complete the following steps:

### 1. Set Up a Firebase Project
1. Go to the [Firebase Console](https://console.firebase.google.com/).
2. Create a new project (e.g., `FruityScan-App`).
3. Register your Android app using this project's package name (e.g., `com.example.fruityscan`).

### 2. Add the Firebase Configuration File
1. After registering the app in Firebase, download the **`google-services.json`** file.
2. Open your project folder on your PC and place the file inside the **`app/`** directory.
```text
   FruityScan/
   ├── app/
   │   ├── google-services.json  <-- PLACE IT HERE
   │   ├── build.gradle
   │   └── src/
