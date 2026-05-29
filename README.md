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

### 3. Verify Firebase Dependencies
Ensure the Firebase dependencies are properly added in your Gradle files.
* Add Google Services plugin in your Project-level and App-level `build.gradle`.
* Add the required Firebase dependencies (e.g., Realtime Database / Auth).

## 🚀 Getting Started
1. Clone this repository.
2. Add the `google-services.json` file as instructed above.
3. Open Android Studio and select **Open an Existing Project**.
4. Click **Sync Project with Gradle Files** and wait for the process to finish.
5. Run the app on an emulator or a physical Android device.
