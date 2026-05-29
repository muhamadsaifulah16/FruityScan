# FruityScan App

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
3. Verify Firebase Dependencies
Ensure the Firebase dependencies are properly added in your Gradle files (the project should already have these, just make sure they sync correctly upon opening):

Project-level build.gradle:

Groovy
  plugins {
      // Google services plugin
      id 'com.google.gms.google-services' version '4.4.1' apply false
  }
App-level build.gradle:

Groovy
  plugins {
      id 'com.android.application'
      id 'com.google.gms.google-services' // Google Services plugin
  }

  dependencies {
      // Import the Firebase BoM
      implementation platform('com.google.firebase:firebase-bom:33.1.0')
      
      // Add specific Firebase dependencies used (e.g., Realtime Database / Auth)
      implementation 'com.google.firebase:firebase-database'
      implementation 'com.google.firebase:firebase-auth'
  }
🚀 Getting Started
Clone this repository.

Add the google-services.json file as instructed above.

Open Android Studio and select Open an Existing Project.

Click Sync Project with Gradle Files and wait for the process to finish.

Run the app on an emulator or a physical Android device.

### 2. Add the Firebase Configuration File
1. After registering the app in Firebase, download the **`google-services.json`** file.
2. Open your project folder on your PC and place the file inside the **`app/`** directory.
```text
   FruityScan/
   ├── app/
   │   ├── google-services.json  <-- PLACE IT HERE
   │   ├── build.gradle
   │   └── src/
